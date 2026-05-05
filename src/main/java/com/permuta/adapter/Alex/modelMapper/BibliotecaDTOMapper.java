package com.permuta.adapter.Alex.modelMapper;

import com.permuta.adapter.teacherCode.model.dto.BibliotecaDTO;
import com.permuta.adapter.teacherCode.model.enums.EstadoInstalacion;
import org.alexyivan.modelo.dto.BibliotecaDto;
import org.alexyivan.modelo.enums.EstadoInstalacionEnum;

/**
 * Mapea BibliotecaDto del alumno a BibliotecaDTO del profesor.
 *
 * Diferencias encontradas:
 *  - horasJugadasTotal float (alumno) → tiempoJuegoTotal double (profesor): diferente nombre y tipo
 *  - ultimaFechaDeJuego (alumno) → ultimaFechaJuego (profesor): diferente nombre de campo
 *  - estadoInstalacion EstadoInstalacionEnum (alumno) → EstadoInstalacion (profesor): mismo valor, diferente tipo
 *  - usuario UsuarioDto (alumno) → UsuarioDTO (profesor): requiere mapeo con UsuarioDTOMapper
 *  - juego JuegoDto (alumno) → JuegoDTO (profesor): requiere mapeo con JuegoDTOMapper
 */
public class BibliotecaDTOMapper {

    public static BibliotecaDTO toTeacher(BibliotecaDto dto) {
        if (dto == null) return null;
        return new BibliotecaDTO(
                dto.getId(),
                dto.getIdUsuario(),
                UsuarioDTOMapper.toTeacher(dto.getUsuario()),
                dto.getIdJuego(),
                JuegoDTOMapper.toTeacher(dto.getJuego()),
                dto.getFechaAdquisicion(),
                dto.getHorasJugadasTotal(),                     // horasJugadasTotal → tiempoJuegoTotal
                dto.getUltimaFechaDeJuego(),                    // ultimaFechaDeJuego → ultimaFechaJuego
                toEstadoInstalacion(dto.getEstadoInstalacion())
        );
    }

    private static EstadoInstalacion toEstadoInstalacion(EstadoInstalacionEnum estado) {
        if (estado == null) return null;
        return switch (estado) {
            case INSTALADO -> EstadoInstalacion.INSTALADO;
            case NO_INSTALADO -> EstadoInstalacion.NO_INSTALADO;
        };
    }
}
