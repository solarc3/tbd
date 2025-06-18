-- ** PROCEDIMIENTOS ALMACENADOS **

-- [07] - Registrar un pedido completo
CREATE OR REPLACE PROCEDURE registrar_pedido_completo(
    p_monto INTEGER,
    p_fecha_pedido TIMESTAMP,
    p_es_urgente BOOLEAN,
    p_estado_pedido estado_pedido,
    p_id_cliente BIGINT,
    p_id_farmacia BIGINT,
    p_id_productos BIGINT[],
    p_cantidades INTEGER[],
    p_recetas_validadas BOOLEAN[]
)
    LANGUAGE plpgsql AS $$
DECLARE
    v_id_pedido BIGINT;
    producto_id BIGINT;
    cantidad INTEGER;
    receta_validada BOOLEAN;
BEGIN
    -- Insertar en pedido
    INSERT INTO pedido (monto, fecha_pedido, es_urgente, estado_pedido, id_cliente, id_farmacia)
    VALUES (p_monto, p_fecha_pedido, p_es_urgente, p_estado_pedido, p_id_cliente, p_id_farmacia)
    RETURNING id_pedido INTO v_id_pedido;

    -- Insertar en producto_pedido
    FOR i IN 1..array_length(p_id_productos, 1) LOOP
        producto_id := p_id_productos[i];
        cantidad := p_cantidades[i];
        receta_validada := p_recetas_validadas[i];

        INSERT INTO producto_pedido (id_pedido, id_producto, cantidad, receta_validada)
        VALUES (v_id_pedido, producto_id, cantidad, receta_validada);
    END LOOP;
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

CREATE TRIGGER trg_verificar_receta
    AFTER INSERT ON producto_pedido
    FOR EACH ROW
EXECUTE FUNCTION verificar_receta();

-- [12] - Insertar califiacion automatica si no se recibe en 48 hrs
CREATE OR REPLACE FUNCTION update_calificacion_after_delivery()
    RETURNS TRIGGER AS $func$
DECLARE
    v_cliente_id BIGINT;
    v_detalle_id BIGINT;
BEGIN
    IF NEW.estado_pedido = 'ENTREGADO' AND OLD.estado_pedido <> 'ENTREGADO' THEN
        IF (CURRENT_TIMESTAMP - NEW.fecha_pedido) >= INTERVAL '48 hours' THEN
            -- extraer el id del cliente del pedido
            SELECT id_cliente INTO v_cliente_id
            FROM pedido
            WHERE id_pedido = NEW.id_pedido;

            -- obtener el id del detalle del pedido
            SELECT id_detalle_pedido INTO v_detalle_id
            FROM detalle_pedido
            WHERE id_pedido = NEW.id_pedido;

            -- si es que no existe una calificacion
            IF NOT EXISTS (SELECT 1 FROM calificacion WHERE id_detalle_pedido = v_detalle_id) THEN
                -- se inserta una nueva calificacion con los valores predeterminados
                INSERT INTO calificacion (id_detalle_pedido, cliente_id, puntuacion)
                VALUES (v_detalle_id, v_cliente_id, 1);
            ELSE
                -- si no, se actualiza
                UPDATE calificacion
                SET puntuacion = 1
                WHERE id_detalle_pedido = v_detalle_id;
            END IF;
        END IF;
    END IF;
    RETURN NEW;
END;
$func$ LANGUAGE plpgsql;

CREATE TRIGGER trg_update_calificacion
    AFTER UPDATE ON pedido
    FOR EACH ROW
EXECUTE FUNCTION update_calificacion_after_delivery();

-- Trigger para insertar automáticamente la ruta estimada al crear un pedido.
CREATE OR REPLACE FUNCTION insert_estimated_route()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE pedido
    -- basta con unir los puntos de ubicacion entre farmacia y cliente.
    SET ruta_estimada = ST_MakeLine(
            (SELECT ubicacion FROM farmacia WHERE id_farmacia = NEW.id_farmacia),
            (SELECT location FROM users WHERE id = NEW.id_cliente)
    )
    WHERE id_pedido = NEW.id_pedido;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_insert_estimated_route
    AFTER INSERT ON pedido
    FOR EACH ROW
EXECUTE FUNCTION insert_estimated_route();