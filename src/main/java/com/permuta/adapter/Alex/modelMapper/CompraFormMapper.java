package com.permuta.adapter.Alex.modelMapper;

import com.permuta.adapter.teacherCode.model.enums.MetodoPago;
import com.permuta.adapter.teacherCode.model.enums.EstadoCompra;
import com.permuta.adapter.teacherCode.model.form.CompraForm;
import org.alexyivan.modelo.enums.MetodoPagoEnum;
import org.alexyivan.modelo.enums.EstadoCompraEnum;

import java.time.LocalDateTime;

/**
 * Mapea CompraForm del profesor a CompraForm del alumno.
 *
 * Diferencias encontradas:
 *  - idUsuario (profesor) → usuarioId (alumno): diferente nombre de campo
 *  - idJuego (profesor) → juegoId (alumno): diferente nombre de campo
 *  - fechaCompra: no existe en el formulario del profesor → se asigna LocalDateTime.now()
 *  - precioSinDescuento double (profesor) → Float (alumno): diferente tipo
 *  - descuentoAplicado (profesor) → descuento (alumno): diferente nombre de campo
 *  - precioFinal: no existe en el formulario del profesor → se calcula como precioSinDescuento*(1-descuento/100)
 *  - estado EstadoCompra.PENDIENTE (profesor) → no existe en alumno: se asigna null
 *  - estado EstadoCompra.COMPLETADA (profesor) → EstadoCompraEnum.COMPLETADO (alumno): nombre diferente
 *  - estado EstadoCompra.REEMBOLSADA (profesor) → EstadoCompraEnum.REEMBOLSADO (alumno): nombre diferente
 */
public class CompraFormMapper {

    public static org.alexyivan.modelo.form.CompraForm toStudent(CompraForm form) {
        float precio = (float) form.precioSinDescuento();
        double precioFinal = form.precioSinDescuento() * (1.0 - form.descuentoAplicado() / 100.0);
        return new org.alexyivan.modelo.form.CompraForm(
                form.idUsuario(),                           // idUsuario → usuarioId
                form.idJuego(),                             // idJuego → juegoId
                LocalDateTime.now(),                        // no existe en el profesor → LocalDateTime.now()
                toMetodoPagoEnum(form.metodoPago()),
                precio,                                     // double → Float
                form.descuentoAplicado(),                   // descuentoAplicado → descuento
                precioFinal,                                // no existe en el profesor → calculado
                toEstadoCompraEnum(form.estado())
        );
    }

    private static MetodoPagoEnum toMetodoPagoEnum(MetodoPago metodo) {
        if (metodo == null) return null;
        return switch (metodo) {
            case TARJETA_CREDITO -> MetodoPagoEnum.TARJETA_CREDITO;
            case PAYPAL -> MetodoPagoEnum.PAYPAL;
            case CARTERA_STEAM -> MetodoPagoEnum.CARTERA_STEAM;
            case TRANSFERENCIA -> MetodoPagoEnum.TRANSFERENCIA;
            case OTROS -> MetodoPagoEnum.OTROS;
        };
    }

    // @NoCampoProfesor: EstadoCompra.PENDIENTE no existe en el alumno → null
    private static EstadoCompraEnum toEstadoCompraEnum(EstadoCompra estado) {
        if (estado == null) return null;
        return switch (estado) {
            case PENDIENTE -> EstadoCompraEnum.PENDIENTE;                         // no existe en alumno
            case COMPLETADA -> EstadoCompraEnum.COMPLETADO; // COMPLETADA → COMPLETADO
            case REEMBOLSADA -> EstadoCompraEnum.REEMBOLSADA;

        };
    }
}
