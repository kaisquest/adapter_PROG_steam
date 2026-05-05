package com.permuta.adapter.Alex.modelMapper;

import com.permuta.adapter.teacherCode.model.form.UsuarioForm;
import org.alexyivan.modelo.enums.EstadoCuentaEmun;

import java.time.LocalDate;

/**
 * Mapea UsuarioForm del profesor a UsuarioForm del alumno.
 *
 * Diferencias encontradas:
 *  - fechaRegistro: no existe en el formulario del profesor → se asigna LocalDate.now()
 *  - saldo: no existe en el formulario del profesor → se asigna 0f
 *  - estado: no existe en el formulario del profesor → se asigna EstadoCuentaEmun.ACTIVA
 */
public class UsuarioFormMapper {

    public static org.alexyivan.modelo.form.UsuarioForm toStudent(UsuarioForm form) {
        return new org.alexyivan.modelo.form.UsuarioForm(
                form.nombreUsuario(),
                form.email(),
                form.contrasena(),
                form.nombreReal(),
                form.pais(),
                form.fechaNacimiento(),
                LocalDate.now(),            // fechaRegistro: no existe en el profesor → LocalDate.now()
                form.avatar(),
                0f,                         // saldo: no existe en el profesor → 0f
                EstadoCuentaEmun.ACTIVA     // estado: no existe en el profesor → ACTIVA
        );
    }
}
