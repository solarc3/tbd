-- [XX] - Registrar un pedido completo
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