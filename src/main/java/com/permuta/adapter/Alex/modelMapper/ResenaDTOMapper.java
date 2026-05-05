package com.permuta.adapter.Alex.modelMapper;

import com.permuta.adapter.teacherCode.model.dto.ResenaDTO;
import com.permuta.adapter.teacherCode.model.enums.EstadoCompra;
import com.permuta.adapter.teacherCode.model.enums.EstadoResena;
import org.alexyivan.modelo.dto.ResenhaDto;
import org.alexyivan.modelo.enums.EstadoCompraEnum;
import org.alexyivan.modelo.enums.EstadoResenhaEnum;

/**
 * Mapea ResenhaDto del alumno a ResenaDTO del profesor.
 *
 * Diferencias encontradas:
 *  - textoAnalisis (alumno) → texto (profesor): diferente nombre de campo
 *  - horasJugadas float (alumno) → double (profesor): diferente tipo
 *  - ultimaFechaEdicion (alumno) → fechaUltimaEdicion (profesor): diferente nombre de campo
 *  - estado EstadoResenhaEnum.BORRADA (alumno) → EstadoResena.ELIMINADA (profesor): nombre diferente
 *  - usuario UsuarioDto (alumno) → UsuarioDTO (profesor): requiere mapeo con UsuarioDTOMapper
 *  - juego JuegoDto (alumno) → JuegoDTO (profesor): requiere mapeo con JuegoDTOMapper
 */
public class ResenaDTOMapper {

    public static ResenaDTO toTeacher(ResenhaDto dto) {
        if (dto == null) return null;
        return new ResenaDTO(
                dto.getId(),
                dto.getIdUsuario(),
                UsuarioDTOMapper.toTeacher(dto.getUsuario()),
                dto.getIdJuego(),
                JuegoDTOMapper.toTeacher(dto.getJuego()),
                dto.isRecomendado(),
                dto.getTextoAnalisis(),                         // textoAnalisis → texto
                dto.getHorasJugadas(),                          // float → double
                dto.getFechaPublicacion(),
                dto.getUltimaFechaEdicion(),                    // ultimaFechaEdicion → fechaUltimaEdicion
                toEstadoResenhaEnum(dto.getEstado())                                             // estado: sin getter en ResenhaDto del alumno → null
        );
    }

    private static EstadoResena toEstadoResenhaEnum(EstadoResenhaEnum estado) {
        if (estado == null) return null;
        return switch (estado) {
            case PUBLICADA -> EstadoResena.PUBLICADA;
            case OCULTA -> EstadoResena.OCULTA;
            case BORRADA -> EstadoResena.ELIMINADA;




        };
    }
    // Nota: ResenhaDto del alumno no tiene getter para estado → se mapea a null
}
