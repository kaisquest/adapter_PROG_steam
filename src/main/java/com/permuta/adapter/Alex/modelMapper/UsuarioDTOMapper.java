package com.permuta.adapter.Alex.modelMapper;

import com.permuta.adapter.teacherCode.model.dto.UsuarioDTO;
import com.permuta.adapter.teacherCode.model.enums.EstadoCuenta;
import com.permuta.adapter.teacherCode.model.enums.EstadoInstalacion;
import org.alexyivan.modelo.dto.UsuarioDto;
import org.alexyivan.modelo.enums.EstadoCuentaEmun;

/**
 * Mapea UsuarioDto del alumno a UsuarioDTO del profesor.
 * <p>
 * Diferencias encontradas:
 * - contrasena: no existe en UsuarioDto del alumno → se asigna ""
 * - cumpleanhos (alumno) → fechaNacimiento (profesor): diferente nombre de campo
 * - creditoSteam float (alumno) → saldoCartera double (profesor): diferente nombre y tipo
 * - estadoCuenta EstadoCuentaEmun (alumno) → EstadoCuenta (profesor): mismos valores, diferente tipo
 */
public class UsuarioDTOMapper {

    public static UsuarioDTO toTeacher(UsuarioDto dto) {
        if (dto == null) return null;
        return new UsuarioDTO(
                dto.getId(),
                dto.getNombreUsuario(),
                dto.getEmail(),
                "",                                     // contrasena: no existe en alumno → ""
                dto.getNombreReal(),
                dto.getPais(),
                dto.getCumpleanhos(),                   // cumpleanhos → fechaNacimiento
                dto.getFechaRegistro(),
                dto.getAvatar(),
                dto.getCreditoSteam(),                // creditoSteam → saldoCartera
                toEstadoCuenta(dto.getEstadoCuenta())                                    // estadoCuenta: sin getter en UsuarioDto del alumno → null
        );
    }

    private static EstadoCuenta toEstadoCuenta(EstadoCuentaEmun estado) {

        if (estado == null) return null;
        return switch (estado) {
            case ACTIVA -> EstadoCuenta.ACTIVA;
            case BANEADA -> EstadoCuenta.BANEADA;
            case SUSPENDIDA -> EstadoCuenta.SUSPENDIDA;
        };

    }
    // Nota: UsuarioDto del alumno no tiene getter para estadoCuenta → se mapea a null
}
