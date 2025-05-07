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