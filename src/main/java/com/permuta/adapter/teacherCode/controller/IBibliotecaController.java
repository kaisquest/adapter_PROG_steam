package com.permuta.adapter.teacherCode.controller;

import java.util.List;

import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.BibliotecaDTO;


/**
 * Interfaz para el controlador de biblioteca de usuario.
 */
public interface IBibliotecaController {

    /**
     * Obtiene todos los juegos de la biblioteca de un usuario.
     *
     * @param idUsuario identificador del usuario
     * @return lista de entradas de biblioteca
     * @throws ValidationException si el usuario no existe
     */
    List<BibliotecaDTO> obtenerJuegosUsuario(Long idUsuario) throws ValidationException;

    /**
     * Agrega un juego a la biblioteca de un usuario.
     *
     * @param idUsuario identificador del usuario
     * @param idJuego   identificador del juego
     * @return true si se agregó correctamente
     * @throws ValidationException si el usuario o juego no existen, o el juego ya está en la biblioteca
     */
    boolean agregarJuegoBiblioteca(Long idUsuario, Long idJuego) throws ValidationException;

    /**
     * Elimina un juego de la biblioteca de un usuario.
     *
     * @param idUsuario identificador del usuario
     * @param idJuego   identificador del juego
     * @throws ValidationException si el usuario, juego o entrada de biblioteca no existen
     */
    void eliminarJuegoBiblioteca(Long idUsuario, Long idJuego) throws ValidationException;

    /**
     * Actualiza el tiempo de juego de un juego en la biblioteca de un usuario.
     *
     * @param idUsuario    identificador del usuario
     * @param idJuego      identificador del juego
     * @param tiempoJugado nuevo tiempo jugado total
     * @return la entrada de biblioteca actualizada
     * @throws ValidationException si el usuario, juego o entrada de biblioteca no existen
     */
    BibliotecaDTO actualizarTiempoJuego(long idUsuario, long idJuego, int tiempoJugado) throws ValidationException;

    /**
     * Consulta la entrada de biblioteca con la última sesión de juego registrada.
     *
     * @return la entrada con la sesión más reciente, o null si no hay entradas
     */
    BibliotecaDTO consultaUltimaSesion();
}
