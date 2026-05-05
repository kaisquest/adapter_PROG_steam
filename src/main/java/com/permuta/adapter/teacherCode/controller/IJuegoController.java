package com.permuta.adapter.teacherCode.controller;

import java.util.List;

import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.JuegoDTO;
import com.permuta.adapter.teacherCode.model.enums.EstadoJuego;
import com.permuta.adapter.teacherCode.model.form.BusquedaForm;
import com.permuta.adapter.teacherCode.model.form.JuegoForm;


/**
 * Interfaz para el controlador de juegos.
 */
public interface IJuegoController {

    /**
     * Crea un nuevo juego en el catálogo.
     *
     * @param form datos del juego
     * @return el juego creado
     * @throws ValidationException si los datos son inválidos o el título ya existe
     */
    JuegoDTO crearJuego(JuegoForm form) throws ValidationException;

    /**
     * Busca juegos aplicando los filtros del formulario de búsqueda.
     *
     * @param form criterios de búsqueda
     * @return lista de juegos que cumplen los criterios
     */
    List<JuegoDTO> buscarJuegos(BusquedaForm form);

    /**
     * Lista todos los juegos disponibles en el catálogo.
     *
     * @return lista de juegos con estado DISPONIBLE
     */
    List<JuegoDTO> listarCatalogo();

    /**
     * Aplica un descuento a un juego.
     *
     * @param id        identificador del juego
     * @param descuento porcentaje de descuento (0-100)
     * @return el juego actualizado
     * @throws ValidationException si el juego no existe o el descuento está fuera de rango
     */
    JuegoDTO aplicarDescuento(long id, int descuento) throws ValidationException;

    /**
     * Cambia el estado de un juego.
     *
     * @param id          identificador del juego
     * @param nuevoEstado nuevo estado del juego
     * @return el juego actualizado
     * @throws ValidationException si el juego no existe
     */
    JuegoDTO cambiarEstado(long id, EstadoJuego nuevoEstado) throws ValidationException;
}
