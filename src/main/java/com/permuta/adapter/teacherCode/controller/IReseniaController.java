package com.permuta.adapter.teacherCode.controller;

import java.util.List;

import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.ResenaDTO;
import com.permuta.adapter.teacherCode.model.form.ResenaForm;

/**
 * Interfaz para el controlador de reseñas.
 */
public interface IReseniaController {

    /**
     * Crea una nueva reseña para un juego.
     *
     * @param form datos de la reseña
     * @return la reseña creada
     * @throws ValidationException si los datos son inválidos o no se cumplen las reglas de negocio
     */
    ResenaDTO crearResena(ResenaForm form) throws ValidationException;

    /**
     * Elimina una reseña existente.
     *
     * @param id        identificador de la reseña
     * @param idUsuario identificador del usuario propietario
     * @throws ValidationException si la reseña no existe o no pertenece al usuario
     */
    void eliminarResena(long id, long idUsuario) throws ValidationException;

    /**
     * Lista todas las reseñas activas de un juego.
     *
     * @param idJuego identificador del juego
     * @return lista de reseñas visibles del juego
     * @throws ValidationException si el juego no existe
     */
    List<ResenaDTO> listarResenasJuego(long idJuego) throws ValidationException;

    /**
     * Oculta una reseña existente.
     *
     * @param id        identificador de la reseña
     * @param idUsuario identificador del usuario propietario
     * @throws ValidationException si la reseña no existe o no pertenece al usuario
     */
    void ocultarResena(long id, long idUsuario) throws ValidationException;

    /**
     * Lista todas las reseñas activas de un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return lista de reseñas visibles del usuario
     * @throws ValidationException si el usuario no existe
     */
    List<ResenaDTO> listarResenasPorUsuario(long idUsuario) throws ValidationException;
}
