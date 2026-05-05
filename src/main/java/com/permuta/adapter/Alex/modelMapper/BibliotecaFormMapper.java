package com.permuta.adapter.Alex.modelMapper;

import com.permuta.adapter.teacherCode.model.enums.EstadoInstalacion;
import com.permuta.adapter.teacherCode.model.form.BibliotecaForm;
import org.alexyivan.modelo.enums.EstadoInstalacionEnum;

/**
 * Mapea BibliotecaForm del profesor a BibliotecaForm del alumno.
 *
 * Diferencias encontradas:
 *  - idUsuario long (profesor) → usuarioId Long (alumno): diferente nombre y tipo (primitivo vs objeto)
 *  - idJuego long (profesor) → juegoId Long (alumno): diferente nombre y tipo (primitivo vs objeto)
 *  - tiempoJuegoTotal double (profesor) → Float (alumno): diferente tipo
 *  - estadoInstalacion EstadoInstalacion (profesor) → EstadoInstalacionEnum (alumno): mismo valor, diferente tipo
 */
public class BibliotecaFormMapper {

    public static org.alexyivan.modelo.form.BibliotecaForm toStudent(BibliotecaForm form) {
        return new org.alexyivan.modelo.form.BibliotecaForm(
                form.idUsuario(),                               // long → Long
                form.idJuego(),                                 // long → Long
                form.fechaAdquisicion(),
                (float) form.tiempoJuegoTotal(),               // double → float
                form.ultimaFechaJuego(),
                toEstadoInstalacionEnum(form.estadoInstalacion())
        );
    }

    private static EstadoInstalacionEnum toEstadoInstalacionEnum(EstadoInstalacion estado) {
        if (estado == null) return null;
        return switch (estado) {
            case INSTALADO -> EstadoInstalacionEnum.INSTALADO;
            case NO_INSTALADO -> EstadoInstalacionEnum.NO_INSTALADO;
        };
    }
}
