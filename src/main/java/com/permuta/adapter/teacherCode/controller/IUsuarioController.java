package com.permuta.adapter.teacherCode.controller;

import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.UsuarioDTO;
import com.permuta.adapter.teacherCode.model.form.UsuarioForm;

/**
 * Interfaz para el controlador de usuarios.
 */
public interface IUsuarioController {

    /**
     * Crea un nuevo usuario.
     *
     * @param form datos del usuario
     * @return el usuario creado
     * @throws ValidationException si los datos son inválidos o el nombre de usuario / email ya existen
     */
    UsuarioDTO creaUsuarioDTO(UsuarioForm form) throws ValidationException;

    /**
     * Consulta el perfil de un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return el perfil del usuario
     */
    UsuarioDTO consultarPerfil(long id);

    /**
     * Consulta el perfil de un usuario por su nombre de usuario.
     *
     * @param nombreUsuario nombre de usuario
     * @return el perfil del usuario
     */
    UsuarioDTO consultarPerfil(String nombreUsuario);

    /**
     * Añade saldo a la cartera de un usuario.
     *
     * @param id      identificador del usuario
     * @param cantidad cantidad a añadir (debe ser mayor que 0)
     * @return el usuario con el saldo actualizado
     * @throws ValidationException si el usuario no existe o la cantidad no es válida
     */
    UsuarioDTO aniadirSaldo(long id, double cantidad) throws ValidationException;

    /**
     * Consulta el saldo de la cartera de un usuario.
     *
     * @param id identificador del usuario
     * @return el usuario con su información de saldo
     * @throws ValidationException si el usuario no existe
     */
    double consultarSaldo(long id) throws ValidationException;
}
