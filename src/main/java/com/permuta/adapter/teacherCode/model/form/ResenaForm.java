package com.permuta.adapter.teacherCode.model.form;



import java.util.ArrayList;
import java.util.List;

import com.permuta.adapter.teacherCode.model.dto.ErrorDto;
import com.permuta.adapter.teacherCode.model.enums.ErrorType;
import com.permuta.adapter.teacherCode.model.enums.EstadoResena;

public record ResenaForm(
        long idUsuario,
        long idJuego,
        boolean recomendado,
        String texto,
        double horasJugadas,
        EstadoResena estado
) {
    public List<ErrorDto> validar() {
        var errores = new ArrayList<ErrorDto>();

        // idUsuario
        if (idUsuario <= 0) {
            errores.add(new ErrorDto("idUsuario", ErrorType.REQUERIDO));
        }

        // idJuego
        if (idJuego <= 0) {
            errores.add(new ErrorDto("idJuego", ErrorType.REQUERIDO));
        }

        // texto
        if (texto == null || texto.isBlank()) {
            errores.add(new ErrorDto("texto", ErrorType.REQUERIDO));
        } else {
            if (texto.length() < 50) {
                errores.add(new ErrorDto("texto", ErrorType.LONGITUD_MINIMA));
            } else if (texto.length() > 8000) {
                errores.add(new ErrorDto("texto", ErrorType.LONGITUD_MAXIMA));
            }
        }

        // horasJugadas
        if (horasJugadas < 0) {
            errores.add(new ErrorDto("horasJugadas", ErrorType.VALOR_DEMASIADO_BAJO));
        }

        // estado
        if (estado == null) {
            errores.add(new ErrorDto("estado", ErrorType.REQUERIDO));
        }

        return errores;
    }
}
