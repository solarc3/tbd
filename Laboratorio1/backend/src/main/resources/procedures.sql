-- ** PROCEDIMIENTOS ALMACENADOS **

-- [07] - Registrar un pedido completo
CREATE OR REPLACE PROCEDURE registrar_pedido_completo(
    p_monto INTEGER,
    p_fecha_pedido TIMESTAMP,
    p_es_urgente BOOLEAN,
    p_estado_pedido estado_pedido,
    p_id_cliente BIGINT,
    p_id_farmacia BIGINT,
    p_id_repartidor BIGINT,
    p_metodo_pago VARCHAR(50),
    p_fecha_entrega TIMESTAMP,
    p_id_productos BIGINT[]
)
    LANGUAGE plpgsql AS $$
DECLARE
    v_id_pedido BIGINT;
    v_id_detalle_pedido BIGINT;
    producto_id BIGINT;
BEGIN
    -- Insertar en pedido
    INSERT INTO pedido (monto, fecha_pedido, es_urgente, estado_pedido, id_cliente, id_farmacia)
    VALUES (p_monto, p_fecha_pedido, p_es_urgente, p_estado_pedido, p_id_cliente, p_id_farmacia)
    RETURNING id_pedido INTO v_id_pedido;

    -- Insertar en detalle_pedido
    INSERT INTO detalle_pedido (id_pedido, id_repartidor, metodo_pago, fecha_entrega)
    VALUES (v_id_pedido, p_id_repartidor, p_metodo_pago, p_fecha_entrega)
    RETURNING id_detalle_pedido INTO v_id_detalle_pedido; -- Optional if you need this ID back

    -- Insertar en producto_pedido
    IF array_length(p_id_productos, 1) > 0 THEN
        FOREACH producto_id IN ARRAY p_id_productos
            LOOP
                INSERT INTO producto_pedido (id_pedido, id_producto)
                VALUES (v_id_pedido, producto_id);
            END LOOP;
    END IF;
END;
$$;

-- [08] - Cambiar el estado de un pedido con validacion
CREATE OR REPLACE PROCEDURE cambiar_estado_pedido(
    p_id_pedido BIGINT,
    p_nuevo_estado estado_pedido
)
    LANGUAGE plpgsql AS $$
BEGIN
    UPDATE pedido
    SET estado_pedido = p_nuevo_estado
    WHERE id_pedido = p_id_pedido;
END;
$$;

-- [09] - Descontar el stock al confirmar el pedido
CREATE OR REPLACE PROCEDURE actualizarStock(
    IN id_productoN BIGINT,
    IN id_FarmaciaN BIGINT,
    IN cant_pedido INTEGER
)
    LANGUAGE plpgsql AS $$
    -- Actualizar stock del producto según el id.
BEGIN
    UPDATE producto_farmacia
    SET stock_producto = stock_producto - cant_pedido
    WHERE id_producto = id_ProductoN and id_farmacia = id_FarmaciaN;
END;
$$;

-- ** TRIGGERS **

-- [10] - Insertar automática fecha_entrega en detalle_pedido cuando el pedido se marca como ENTREGADO
CREATE OR REPLACE FUNCTION actualizar_fecha_entrega_trigger_func()
    RETURNS TRIGGER AS $$
BEGIN
    -- Verificar si el nuevo estado del pedido es 'ENTREGADO'
    -- y si el estado anterior no era 'ENTREGADO' (para evitar actualizaciones repetidas si ya estaba entregado)
    IF NEW.estado_pedido = 'ENTREGADO' AND (OLD IS NULL OR OLD.estado_pedido != 'ENTREGADO') THEN
        -- Actualizar la fecha_entrega en la tabla detalle_pedido
        -- Asumimos que solo hay un detalle_pedido por pedido. Si puede haber más, esta lógica necesitaría ajuste.
        UPDATE detalle_pedido
        SET fecha_entrega = NOW()
        WHERE id_pedido = NEW.id_pedido;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_actualizar_fecha_entrega_pedido
    AFTER UPDATE ON pedido
    FOR EACH ROW
EXECUTE FUNCTION actualizar_fecha_entrega_trigger_func();

-- [11] - Registrar notificacion si un medicamento con receta es pedido sin validación
-- Al generarse un pedido, particularmente sus productos (tabla producto_pedido), se hace la validacion
CREATE OR REPLACE FUNCTION verificar_receta()
    RETURNS TRIGGER AS $$
BEGIN
    -- si el producto requiere receta y no se ha validado
    IF EXISTS (SELECT 1 FROM producto WHERE id_producto = NEW.id_producto AND requiere_receta = TRUE)
        AND NEW.receta_validada = FALSE THEN
        -- crear notificacion
        INSERT INTO notificacion(id_pedido, id_producto, mensaje)
        VALUES (NEW.id_pedido, NEW.id_producto, 'Receta no validada');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_verificar_receta
    AFTER INSERT ON producto_pedido
    FOR EACH ROW
EXECUTE FUNCTION verificar_receta();

-- [12] - Insertar califiacion automatica si no se recibe en 48 hrs
CREATE OR REPLACE FUNCTION update_calificacion_after_delivery()
    RETURNS TRIGGER AS $func$
BEGIN
    IF NEW.estado_pedido = 'ENTREGADO' AND OLD.estado_pedido <> 'ENTREGADO' THEN
        IF (CURRENT_TIMESTAMP - NEW.fecha_pedido) >= INTERVAL '48 hours' THEN
UPDATE calificacion
SET puntuacion = 5
WHERE id_detalle_pedido IN (
    SELECT id_detalle_pedido
    FROM detalle_pedido
    WHERE id_pedido = NEW.id_pedido
);
END IF;
END IF;
RETURN NEW;
END;
$func$ LANGUAGE plpgsql;

CREATE TRIGGER trig_update_calificacion
    AFTER UPDATE ON pedido
    FOR EACH ROW
    EXECUTE FUNCTION update_calificacion_after_delivery();