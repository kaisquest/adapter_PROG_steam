package com.permuta.adapter.teacherCode.model.form;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.permuta.adapter.teacherCode.model.dto.ErrorDto;
import com.permuta.adapter.teacherCode.model.enums.ErrorType;

public record UsuarioForm(
        String nombreUsuario,
        String email,
        String contrasena,
        String nombreReal,
        String pais,
        LocalDate fechaNacimiento,
        String avatar
) {
    public List<ErrorDto> validar() {
        var errores = new ArrayList<ErrorDto>();

        // nombreUsuario
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            errores.add(new ErrorDto("nombreUsuario", ErrorType.REQUERIDO));
        } else {
            if (nombreUsuario.length() < 3) {
                errores.add(new ErrorDto("nombreUsuario", ErrorType.LONGITUD_MINIMA));
            } else if (nombreUsuario.length() > 20) {
                errores.add(new ErrorDto("nombreUsuario", ErrorType.LONGITUD_MAXIMA));
            } else if (!nombreUsuario.matches("[a-zA-Z][a-zA-Z0-9_-]*")) {
                errores.add(new ErrorDto("nombreUsuario", ErrorType.FORMATO_INVALIDO));
            }
        }

        // email
        if (email == null || email.isBlank()) {
            errores.add(new ErrorDto("email", ErrorType.REQUERIDO));
        } else if (!email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) {
            errores.add(new ErrorDto("email", ErrorType.FORMATO_INVALIDO));
        }

        // contrasena
        if (contrasena == null || contrasena.isBlank()) {
            errores.add(new ErrorDto("contrasena", ErrorType.REQUERIDO));
        } else {
            if (contrasena.length() < 8) {
                errores.add(new ErrorDto("contrasena", ErrorType.LONGITUD_MINIMA));
            } else if (!contrasena.matches(".*[A-Z].*") || !contrasena.matches(".*[a-z].*") || !contrasena.matches(".*[0-9].*")) {
                errores.add(new ErrorDto("contrasena", ErrorType.FORMATO_INVALIDO));
            }
        }

        // nombreReal
        if (nombreReal == null || nombreReal.isBlank()) {
            errores.add(new ErrorDto("nombreReal", ErrorType.REQUERIDO));
        } else {
            if (nombreReal.length() < 2) {
                errores.add(new ErrorDto("nombreReal", ErrorType.LONGITUD_MINIMA));
            } else if (nombreReal.length() > 50) {
                errores.add(new ErrorDto("nombreReal", ErrorType.LONGITUD_MAXIMA));
            }
        }

        // pais
        if (pais == null || pais.isBlank()) {
            errores.add(new ErrorDto("pais", ErrorType.REQUERIDO));
        }

        // fechaNacimiento
        if (fechaNacimiento == null) {
            errores.add(new ErrorDto("fechaNacimiento", ErrorType.REQUERIDO));
        } else {
            if (fechaNacimiento.isAfter(LocalDate.now())) {
                errores.add(new ErrorDto("fechaNacimiento", ErrorType.FORMATO_INVALIDO));
            } else if (fechaNacimiento.isAfter(LocalDate.now().minusYears(13))) {
                errores.add(new ErrorDto("fechaNacimiento", ErrorType.VALOR_DEMASIADO_ALTO));
            }
        }

        // avatar (opcional)
        if (avatar != null && avatar.length() > 100) {
            errores.add(new ErrorDto("avatar", ErrorType.LONGITUD_MAXIMA));
        }

        return errores;
    }
}
