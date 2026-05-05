package com.permuta.adapter.Alex;

import com.permuta.adapter.Alex.modelMapper.UsuarioDTOMapper;
import com.permuta.adapter.Alex.modelMapper.UsuarioFormMapper;
import com.permuta.adapter.teacherCode.controller.IUsuarioController;
import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.ErrorDto;
import com.permuta.adapter.teacherCode.model.dto.UsuarioDTO;
import com.permuta.adapter.teacherCode.model.enums.ErrorType;
import com.permuta.adapter.teacherCode.model.form.UsuarioForm;
import org.alexyivan.controlador.IUsuarioControlador;
import org.alexyivan.exception.ValidacionException;

import java.util.List;

public class UsuarioAdapter implements IUsuarioController {

    private final IUsuarioControlador usuarioControlador;

    public UsuarioAdapter(IUsuarioControlador usuarioControlador) {
        this.usuarioControlador = usuarioControlador;
    }

    @Override
    public UsuarioDTO creaUsuarioDTO(UsuarioForm form) throws ValidationException {
        var alumnoForm = UsuarioFormMapper.toStudent(form);
        try {
            var result = usuarioControlador.registrarUsuario(alumnoForm);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("result", ErrorType.NO_EXISTE)));
            }
            return UsuarioDTOMapper.toTeacher(result.get());
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public UsuarioDTO consultarPerfil(long id) {
        try {
            var result = usuarioControlador.consultarUsuarioId(id);
            return result.map(UsuarioDTOMapper::toTeacher).orElse(null);
        } catch (ValidacionException e) {
            return null;
        }
    }

    @Override
    public UsuarioDTO consultarPerfil(String nombreUsuario) {
        try {
            var result = usuarioControlador.consultarUsuarioNombreUsuario(nombreUsuario);
            return result.map(UsuarioDTOMapper::toTeacher).orElse(null);
        } catch (ValidacionException e) {
            return null;
        }
    }

    @Override
    public UsuarioDTO aniadirSaldo(long id, double cantidad) throws ValidationException {
        try {
            usuarioControlador.anhadirSaldo(id, (float) cantidad);
            var result = usuarioControlador.consultarUsuarioId(id);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("id", ErrorType.NO_EXISTE)));
            }
            return UsuarioDTOMapper.toTeacher(result.get());
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public double consultarSaldo(long id) throws ValidationException {
        try {
            var result = usuarioControlador.consultarSaldo(id);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("id", ErrorType.NO_EXISTE)));
            }
            return result.get().getCreditoSteam();
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }
}
