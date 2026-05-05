package com.permuta.adapter.Alex.modelMapper;

import com.permuta.adapter.teacherCode.model.enums.ClasificacionEdad;
import com.permuta.adapter.teacherCode.model.enums.EstadoJuego;
import com.permuta.adapter.teacherCode.model.form.JuegoForm;
import org.alexyivan.modelo.enums.EstadoJuegoEnum;
import org.alexyivan.modelo.enums.PegiEnum;

/**
 * Mapea JuegoForm del profesor a JuegoForm del alumno.
 *
 * Diferencias encontradas:
 *  - desarrollador (profesor) → desarrolladora (alumno): diferente nombre de campo
 *  - fechaLanzamiento (profesor) → fechaPublicacion (alumno): diferente nombre de campo
 *  - categoria (profesor) → genero (alumno): diferente nombre de campo
 *  - clasificacionEdad ClasificacionEdad (profesor) → rangoEdad PegiEnum (alumno): mismos valores, diferente tipo
 *  - idiomasDisponibles String[] (profesor) → idiomasDisponibles String (alumno): array vs cadena delimitada por comas
 *  - estado EstadoJuego.PREVENTA (profesor) → EstadoJuegoEnum.PRECOMPRA (alumno): valor con nombre diferente
 */
public class JuegoFormMapper {

    public static org.alexyivan.modelo.form.JuegoForm toStudent(JuegoForm form) {
        return new org.alexyivan.modelo.form.JuegoForm(
                form.titulo(),
                form.descripcion(),
                form.desarrollador(),        // desarrollador → desarrolladora
                form.fechaLanzamiento(),     // fechaLanzamiento → fechaPublicacion
                (float) form.precioBase(),
                form.descuentoActual(),
                form.categoria(),            // categoria → genero
                toPegiEnum(form.clasificacionEdad()),
                idiomasToString(form.idiomasDisponibles()),  // String[] → String
                toEstadoJuegoEnum(form.estado())
        );
    }

    private static PegiEnum toPegiEnum(ClasificacionEdad edad) {
        if (edad == null) return null;
        return switch (edad) {
            case PEGI_3 -> PegiEnum.PEGI_3;
            case PEGI_7 -> PegiEnum.PEGI_7;
            case PEGI_12 -> PegiEnum.PEGI_12;
            case PEGI_16 -> PegiEnum.PEGI_16;
            case PEGI_18 -> PegiEnum.PEGI_18;
        };
    }

    public static EstadoJuegoEnum estadoJuegoToStudent(EstadoJuego estado) {
        return toEstadoJuegoEnum(estado);
    }

    private static EstadoJuegoEnum toEstadoJuegoEnum(EstadoJuego estado) {
        if (estado == null) return EstadoJuegoEnum.DISPONIBLE;
        return switch (estado) {
            case DISPONIBLE -> EstadoJuegoEnum.DISPONIBLE;
            case PREVENTA -> EstadoJuegoEnum.PRECOMPRA;         // PREVENTA → PRECOMPRA
            case ACCESO_ANTICIPADO -> EstadoJuegoEnum.ACCESO_ANTICIPADO;
            case NO_DISPONIBLE -> EstadoJuegoEnum.NO_DISPONIBLE;
        };
    }

    // @NoCampoProfesor: idiomasDisponibles en el profesor es String[]; 
    //  el alumno usa String (concatenación con coma)
    private static String idiomasToString(String[] idiomas) {
        if (idiomas == null || idiomas.length == 0) return null;
        return String.join(",", idiomas);
    }
}
