package com.permuta.adapter.teacherCode.controller;

import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.CompraDTO;
import com.permuta.adapter.teacherCode.model.form.CompraForm;

/**
 * Interfaz para el controlador de compras.
 */
public interface ICompraController {

    /**
     * Realiza una nueva compra.
     *
     * @param form datos de la compra
     * @return la compra creada
     * @throws ValidationException si los datos son inválidos o no se cumplen las reglas de negocio
     */
    CompraDTO realizarCompra(CompraForm form) throws ValidationException;

    /**
     * Procesa el pago de una compra pendiente.
     *
     * @param idCompra identificador de la compra
     * @return la compra actualizada con estado COMPLETADA
     * @throws ValidationException si la compra no existe o no está en estado PENDIENTE
     */
    CompraDTO procesarPago(long idCompra) throws ValidationException;

    /**
     * Consulta una compra verificando que pertenezca al usuario indicado.
     *
     * @param id        identificador de la compra
     * @param idUsuario identificador del usuario
     * @return la compra encontrada, o null si no existe
     * @throws ValidationException si la compra no pertenece al usuario
     */
    CompraDTO consultarCompra(long id, long idUsuario) throws ValidationException;

    /**
     * Solicita el reembolso de una compra completada.
     *
     * @param idCompra identificador de la compra
     * @return la compra actualizada con estado REEMBOLSADA
     * @throws ValidationException si la compra no existe, no está completada o no cumple las condiciones de reembolso
     */
    CompraDTO solicitarReembolso(long idCompra) throws ValidationException;
}
