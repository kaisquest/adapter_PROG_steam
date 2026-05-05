package com.permuta.adapter.teacherCode.model.form;



import java.util.ArrayList;
import java.util.List;

import com.permuta.adapter.teacherCode.model.dto.ErrorDto;
import com.permuta.adapter.teacherCode.model.enums.ErrorType;
import com.permuta.adapter.teacherCode.model.enums.EstadoCompra;
import com.permuta.adapter.teacherCode.model.enums.MetodoPago;

public record CompraForm(
        long idUsuario,
        long idJuego,
        MetodoPago metodoPago,
        double precioSinDescuento,
        int descuentoAplicado,
        EstadoCompra estado
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

        // metodoPago
        if (metodoPago == null) {
            errores.add(new ErrorDto("metodoPago", ErrorType.REQUERIDO));
        }

        // precioSinDescuento
        if (precioSinDescuento <= 0) {
            errores.add(new ErrorDto("precioSinDescuento", ErrorType.VALOR_DEMASIADO_BAJO));
        }

        // descuentoAplicado
        if (descuentoAplicado < 0) {
            errores.add(new ErrorDto("descuentoAplicado", ErrorType.VALOR_DEMASIADO_BAJO));
        } else if (descuentoAplicado > 100) {
            errores.add(new ErrorDto("descuentoAplicado", ErrorType.VALOR_DEMASIADO_ALTO));
        }

        // estado
        if (estado == null) {
            errores.add(new ErrorDto("estado", ErrorType.REQUERIDO));
        }

        return errores;
    }
}
