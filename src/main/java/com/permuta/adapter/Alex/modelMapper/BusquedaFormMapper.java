package com.permuta.adapter.Alex.modelMapper;

import com.permuta.adapter.teacherCode.model.enums.ClasificacionEdad;
import com.permuta.adapter.teacherCode.model.enums.EstadoCompra;
import com.permuta.adapter.teacherCode.model.enums.EstadoJuego;
import com.permuta.adapter.teacherCode.model.form.BusquedaForm;
import org.alexyivan.modelo.enums.EstadoCompraEnum;
import org.alexyivan.modelo.enums.EstadoJuegoEnum;
import org.alexyivan.modelo.form.BusquedaJuegosForm;

/**
 * Mapea BusquedaForm del profesor a BusquedaJuegosForm del alumno.
 *
 * Diferencias encontradas:
 *  - texto (profesor) → titulo (alumno): diferente nombre de campo
 *  - categoria (profesor) → genero (alumno): diferente nombre de campo
 *  - precio: no existe en el formulario del profesor → se asigna null
 *  - clasificacionEdad ClasificacionEdad (profesor) → pegi String (alumno): enum vs String
 *  - estado EstadoJuego (profesor) → estado String (alumno): enum vs String
 */
public class BusquedaFormMapper {

    public static BusquedaJuegosForm toStudent(BusquedaForm form) {
        return new BusquedaJuegosForm(
                form.texto() != null ? form.texto() : "",       // texto → titulo
                form.categoria() != null ? form.categoria() : "", // categoria → genero
                null,                                            // precio: no existe en el profesor → null
                toStringPegi(form.clasificacionEdad()),          // ClasificacionEdad → String
                toEstadoJuegoEnum(form.estado())                    // EstadoJuego → String
        );
    }

    // @NoCampoProfesor: precio no existe en BusquedaForm del profesor → null
    private static String toStringPegi(ClasificacionEdad edad) {
        if (edad == null) return "";
        return edad.name(); // PEGI_3, PEGI_7, etc.
    }

    private static String toStringEstado(EstadoJuego estado) {
        if (estado == null) return "";
        return estado.name();
    }
    private static EstadoJuegoEnum toEstadoJuegoEnum(EstadoJuego estado) {
        if (estado == null) return null;
        return switch (estado) {
            case DISPONIBLE -> EstadoJuegoEnum.DISPONIBLE;                         // no existe en alumno
            case PREVENTA -> EstadoJuegoEnum.PRECOMPRA; // COMPLETADA → COMPLETADO
            case ACCESO_ANTICIPADO -> EstadoJuegoEnum.ACCESO_ANTICIPADO;
            case NO_DISPONIBLE -> EstadoJuegoEnum.NO_DISPONIBLE;// REEMBOLSADA → REEMBOLSADO
        };
    }
}
