package com.permuta.adapter.Alex.modelMapper;

import com.permuta.adapter.teacherCode.model.dto.CompraDTO;
import com.permuta.adapter.teacherCode.model.enums.EstadoCompra;
import com.permuta.adapter.teacherCode.model.enums.MetodoPago;
import org.alexyivan.modelo.dto.CompraDto;
import org.alexyivan.modelo.enums.EstadoCompraEnum;
import org.alexyivan.modelo.enums.MetodoPagoEnum;

/**
 * Mapea CompraDto del alumno a CompraDTO del profesor.
 *
 * Diferencias encontradas:
 *  - metodoDePago MetodoPagoEnum (alumno) → metodoPago MetodoPago (profesor): diferente nombre y tipo
 *  - precioSinDescuento float (alumno) → double (profesor): diferente tipo
 *  - descuentoAplicado int (alumno) → double (profesor): diferente tipo
 *  - fechaCompra: no existe en CompraDto del alumno → se asigna null
 *  - estado EstadoCompraEnum.COMPLETADO (alumno) → EstadoCompra.COMPLETADA (profesor): nombre diferente
 *  - estado EstadoCompraEnum.REEMBOLSADO (alumno) → EstadoCompra.REEMBOLSADA (profesor): nombre diferente
 *  - usuario CompraDto (alumno) → UsuarioDTO (profesor): requiere mapeo con UsuarioDTOMapper
 *  - juego JuegoDto (alumno) → JuegoDTO (profesor): requiere mapeo con JuegoDTOMapper
 */
public class CompraDTOMapper {

    public static CompraDTO toTeacher(CompraDto dto) {
        if (dto == null) return null;
        return new CompraDTO(
                dto.getId(),
                dto.getIdUsuario(),
                UsuarioDTOMapper.toTeacher(dto.getUsuario()),
                dto.getIdJuego(),
                JuegoDTOMapper.toTeacher(dto.getJuego()),
                dto.getFechaCompra().toLocalDate(),                                           // fechaCompra: no existe en alumno → null
                null,                                           // metodoDePago: sin getter en CompraDto del alumno → null
                dto.getPrecioSinDescuento(),                    // float → double
                dto.getDescuentoAplicado(),                     // int → double
                toEstadoCompraEnum(dto.getEstado())                                            // estado: sin getter en CompraDto del alumno → null
        );
    }

    // Nota: CompraDto del alumno no tiene getters para metodoDePago ni estado → se mapean a null
    private static EstadoCompra toEstadoCompraEnum(EstadoCompraEnum estado) {
        if (estado == null) return null;
        return switch (estado) {
            case PENDIENTE -> EstadoCompra.PENDIENTE;                         // no existe en alumno
            case COMPLETADO -> EstadoCompra.COMPLETADA; // COMPLETADA → COMPLETADO
            case REEMBOLSADA -> EstadoCompra.REEMBOLSADA;


        };
    }
}
