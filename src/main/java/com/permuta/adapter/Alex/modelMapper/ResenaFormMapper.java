package com.permuta.adapter.Alex.modelMapper;

import com.permuta.adapter.teacherCode.model.enums.EstadoResena;
import com.permuta.adapter.teacherCode.model.form.ResenaForm;
import org.alexyivan.modelo.enums.EstadoResenhaEnum;

import java.time.LocalDate;

/**
 * Mapea ResenaForm del profesor a ResenhaForm del alumno.
 *
 * Diferencias encontradas:
 *  - texto (profesor) → textoAnalisis (alumno): diferente nombre de campo
 *  - horasJugadas double (profesor) → float (alumno): diferente tipo
 *  - usuario: no existe en el formulario del profesor → se asigna null
 *  - juego: no existe en el formulario del profesor → se asigna null
 *  - fechaPublicacion: no existe en el formulario del profesor → se asigna LocalDate.now()
 *  - ultimaFechaEdicion: no existe en el formulario del profesor → se asigna null
 *  - estado EstadoResena.ELIMINADA (profesor) → EstadoResenhaEnum.BORRADA (alumno): nombre diferente
 */
public class ResenaFormMapper {

    public static org.alexyivan.modelo.form.ResenhaForm toStudent(ResenaForm form) {
        return new org.alexyivan.modelo.form.ResenhaForm(
                form.idUsuario(),
                null,                           // usuario: no existe en el profesor → null
                form.idJuego(),
                null,                           // juego: no existe en el profesor → null
                form.recomendado(),
                form.texto(),                   // texto → textoAnalisis
                (float) form.horasJugadas(),    // double → float
                LocalDate.now(),                // fechaPublicacion: no existe en el profesor → LocalDate.now()
                null,                           // ultimaFechaEdicion: no existe en el profesor → null
                toEstadoResenhaEnum(form.estado())
        );
    }

    // @NoCampoProfesor: EstadoResena.ELIMINADA (profesor) → EstadoResenhaEnum.BORRADA (alumno): nombre diferente
    private static EstadoResenhaEnum toEstadoResenhaEnum(EstadoResena estado) {
        if (estado == null) return null;
        return switch (estado) {
            case PUBLICADA -> EstadoResenhaEnum.PUBLICADA;
            case OCULTA -> EstadoResenhaEnum.OCULTA;
            case ELIMINADA -> EstadoResenhaEnum.BORRADA;    // ELIMINADA → BORRADA
        };
    }
}
