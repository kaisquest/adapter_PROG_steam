package com.permuta.adapter.Alex.modelMapper;

import com.permuta.adapter.teacherCode.model.dto.JuegoDTO;
import com.permuta.adapter.teacherCode.model.enums.ClasificacionEdad;
import com.permuta.adapter.teacherCode.model.enums.EstadoJuego;
import org.alexyivan.modelo.dto.JuegoDto;
import org.alexyivan.modelo.enums.EstadoJuegoEnum;
import org.alexyivan.modelo.enums.PegiEnum;

/**
 * Mapea JuegoDto del alumno a JuegoDTO del profesor.
 *
 * Diferencias encontradas:
 *  - desarrolladora (alumno) → desarrollador (profesor): diferente nombre de campo
 *  - fechaPublicacion (alumno) → fechaLanzamiento (profesor): diferente nombre de campo
 *  - precioBase float (alumno) → double (profesor): diferente tipo
 *  - descuentoActual int (alumno) → double (profesor): diferente tipo
 *  - genero (alumno) → categoria (profesor): diferente nombre de campo
 *  - rangoEdad PegiEnum (alumno) → clasificacionEdad ClasificacionEdad (profesor): mismos valores, diferente tipo
 *  - idiomasDisponibles: no existe en JuegoDto del alumno → se asigna null
 *  - estado EstadoJuegoEnum.PRECOMPRA (alumno) → EstadoJuego.PREVENTA (profesor): nombre diferente
 */
public class JuegoDTOMapper {

    public static JuegoDTO toTeacher(JuegoDto dto) {
        if (dto == null) return null;
        return new JuegoDTO(
                dto.getId(),
                dto.getTitulo(),
                dto.getDescripcion(),
                dto.getDesarrolladora(),                // desarrolladora → desarrollador
                dto.getFechaPublicacion(),              // fechaPublicacion → fechaLanzamiento
                dto.getPrecioBase(),                    // float → double
                dto.getDescuentoActual(),               // int → double
                dto.getGenero(),                        // genero → categoria
                toClasificacionEdad(dto.getRangoEdad()),
                null,                                   // idiomasDisponibles: no existe en alumno → null
                toEstadoJuego(dto.getEstado())
        );
    }

    private static ClasificacionEdad toClasificacionEdad(PegiEnum pegi) {
        if (pegi == null) return null;
        return switch (pegi) {
            case PEGI_3 -> ClasificacionEdad.PEGI_3;
            case PEGI_7 -> ClasificacionEdad.PEGI_7;
            case PEGI_12 -> ClasificacionEdad.PEGI_12;
            case PEGI_16 -> ClasificacionEdad.PEGI_16;
            case PEGI_18 -> ClasificacionEdad.PEGI_18;
        };
    }

    private static EstadoJuego toEstadoJuego(EstadoJuegoEnum estado) {
        if (estado == null) return null;
        return switch (estado) {
            case DISPONIBLE -> EstadoJuego.DISPONIBLE;
            case PRECOMPRA -> EstadoJuego.PREVENTA;     // PRECOMPRA → PREVENTA
            case ACCESO_ANTICIPADO -> EstadoJuego.ACCESO_ANTICIPADO;
            case NO_DISPONIBLE -> EstadoJuego.NO_DISPONIBLE;
        };
    }
}
