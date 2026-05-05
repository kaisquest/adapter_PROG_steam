package com.permuta.adapter.teacherCode.model.form;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.permuta.adapter.teacherCode.model.dto.ErrorDto;
import com.permuta.adapter.teacherCode.model.enums.ErrorType;
import com.permuta.adapter.teacherCode.model.enums.EstadoInstalacion;

public record BibliotecaForm(
        long idUsuario,
        long idJuego,
        LocalDate fechaAdquisicion,
        double tiempoJuegoTotal,
        LocalDate ultimaFechaJuego,
        EstadoInstalacion estadoInstalacion
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

        // fechaAdquisicion
        if (fechaAdquisicion == null) {
            errores.add(new ErrorDto("fechaAdquisicion", ErrorType.REQUERIDO));
        } else if (fechaAdquisicion.isAfter(LocalDate.now())) {
            errores.add(new ErrorDto("fechaAdquisicion", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        // tiempoJuegoTotal
        if (tiempoJuegoTotal < 0) {
            errores.add(new ErrorDto("tiempoJuegoTotal", ErrorType.VALOR_DEMASIADO_BAJO));
        }

        // ultimaFechaJuego (opcional)
        if (ultimaFechaJuego != null) {
            if (ultimaFechaJuego.isAfter(LocalDate.now())) {
                errores.add(new ErrorDto("ultimaFechaJuego", ErrorType.VALOR_DEMASIADO_ALTO));
            } else if (fechaAdquisicion != null && ultimaFechaJuego.isBefore(fechaAdquisicion)) {
                errores.add(new ErrorDto("ultimaFechaJuego", ErrorType.VALOR_DEMASIADO_BAJO));
            }
        }

        // estadoInstalacion
        if (estadoInstalacion == null) {
            errores.add(new ErrorDto("estadoInstalacion", ErrorType.REQUERIDO));
        }

        return errores;
    }
}
