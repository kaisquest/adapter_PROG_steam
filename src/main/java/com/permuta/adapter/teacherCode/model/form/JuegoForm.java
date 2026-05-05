package com.permuta.adapter.teacherCode.model.form;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.permuta.adapter.teacherCode.model.dto.ErrorDto;
import com.permuta.adapter.teacherCode.model.enums.ClasificacionEdad;
import com.permuta.adapter.teacherCode.model.enums.ErrorType;
import com.permuta.adapter.teacherCode.model.enums.EstadoJuego;

public record JuegoForm(
        String titulo,
        String descripcion,
        String desarrollador,
        LocalDate fechaLanzamiento,
        double precioBase,
        int descuentoActual,
        String categoria,
        ClasificacionEdad clasificacionEdad,
        String[] idiomasDisponibles,
        EstadoJuego estado
) {
    public List<ErrorDto> validar() {
        var errores = new ArrayList<ErrorDto>();

        // titulo
        if (titulo == null || titulo.isBlank()) {
            errores.add(new ErrorDto("titulo", ErrorType.REQUERIDO));
        } else {
            if (titulo.length() < 1) {
                errores.add(new ErrorDto("titulo", ErrorType.LONGITUD_MINIMA));
            } else if (titulo.length() > 100) {
                errores.add(new ErrorDto("titulo", ErrorType.LONGITUD_MAXIMA));
            }
        }

        // descripcion (opcional)
        if (descripcion != null && descripcion.length() > 2000) {
            errores.add(new ErrorDto("descripcion", ErrorType.LONGITUD_MAXIMA));
        }

        // desarrollador
        if (desarrollador == null || desarrollador.isBlank()) {
            errores.add(new ErrorDto("desarrollador", ErrorType.REQUERIDO));
        } else {
            if (desarrollador.length() < 2) {
                errores.add(new ErrorDto("desarrollador", ErrorType.LONGITUD_MINIMA));
            } else if (desarrollador.length() > 100) {
                errores.add(new ErrorDto("desarrollador", ErrorType.LONGITUD_MAXIMA));
            }
        }

        // fechaLanzamiento
        if (fechaLanzamiento == null) {
            errores.add(new ErrorDto("fechaLanzamiento", ErrorType.REQUERIDO));
        }

        // precioBase
        if (precioBase < 0) {
            errores.add(new ErrorDto("precioBase", ErrorType.VALOR_DEMASIADO_BAJO));
        } else if (precioBase > 999.99) {
            errores.add(new ErrorDto("precioBase", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        // descuentoActual
        if (descuentoActual < 0) {
            errores.add(new ErrorDto("descuentoActual", ErrorType.VALOR_DEMASIADO_BAJO));
        } else if (descuentoActual > 100) {
            errores.add(new ErrorDto("descuentoActual", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        // clasificacionEdad
        if (clasificacionEdad == null) {
            errores.add(new ErrorDto("clasificacionEdad", ErrorType.REQUERIDO));
        }

        // idiomasDisponibles (opcional): si se proporciona, al menos uno
        if (idiomasDisponibles != null && idiomasDisponibles.length == 0) {
            errores.add(new ErrorDto("idiomasDisponibles", ErrorType.LONGITUD_MINIMA));
        }

        return errores;
    }
}
