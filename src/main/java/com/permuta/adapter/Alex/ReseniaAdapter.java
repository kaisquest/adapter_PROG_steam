package com.permuta.adapter.Alex;

import com.permuta.adapter.Alex.modelMapper.ResenaDTOMapper;
import com.permuta.adapter.Alex.modelMapper.ResenaFormMapper;
import com.permuta.adapter.teacherCode.controller.IReseniaController;
import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.ErrorDto;
import com.permuta.adapter.teacherCode.model.dto.ResenaDTO;
import com.permuta.adapter.teacherCode.model.enums.ErrorType;
import com.permuta.adapter.teacherCode.model.form.ResenaForm;
import org.alexyivan.controlador.IResenhaControlador;
import org.alexyivan.exception.ValidacionException;
import org.alexyivan.modelo.enums.EstadoResenhaEnum;

import java.util.List;
import java.util.Optional;

public class ReseniaAdapter implements IReseniaController {

    private final IResenhaControlador resenhaControlador;

    public ReseniaAdapter(IResenhaControlador resenhaControlador) {
        this.resenhaControlador = resenhaControlador;
    }

    @Override
    public ResenaDTO crearResena(ResenaForm form) throws ValidationException {
        var alumnoForm = ResenaFormMapper.toStudent(form);
        try {
            var result = resenhaControlador.escribirResenha(alumnoForm);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("result", ErrorType.NO_EXISTE)));
            }
            return ResenaDTOMapper.toTeacher(result.get());
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public void eliminarResena(long id, long idUsuario) throws ValidationException {
        // El alumno requiere un ResenhaForm completo. Se construye con el idUsuario mínimo.
        var alumnoForm = new org.alexyivan.modelo.form.ResenhaForm(
                idUsuario, null, 0L, null, false, "placeholder de 50+ caracteres para pasar validacion de longitud minima",
                0f, java.time.LocalDate.now(), null, EstadoResenhaEnum.BORRADA
        );
        try {
            var result = resenhaControlador.eliminarResenha(id, alumnoForm);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("id", ErrorType.NO_EXISTE)));
            }
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public List<ResenaDTO> listarResenasJuego(long idJuego) throws ValidationException {
        try {
            var result = resenhaControlador.verResenhasJuego(idJuego, Optional.empty(), Optional.empty());
            // Nota: ResenhaDto del alumno no tiene getter para estado → no se puede filtrar
            return result.stream().map(ResenaDTOMapper::toTeacher).toList();
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public void ocultarResena(long id, long idUsuario) throws ValidationException {
        var alumnoForm = new org.alexyivan.modelo.form.ResenhaForm(
                idUsuario, null, 0L, null, false, "placeholder de 50+ caracteres para pasar validacion de longitud minima",
                0f, java.time.LocalDate.now(), null, EstadoResenhaEnum.OCULTA
        );
        try {
            var result = resenhaControlador.ocultarResenha(id, alumnoForm);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("id", ErrorType.NO_EXISTE)));
            }
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public List<ResenaDTO> listarResenasPorUsuario(long idUsuario) throws ValidationException {
        var alumnoForm = new org.alexyivan.modelo.form.ResenhaForm(
                idUsuario, null, 0L, null, false, "placeholder de 50+ caracteres para pasar validacion de longitud minima",
                0f, java.time.LocalDate.now(), null, EstadoResenhaEnum.PUBLICADA
        );
        try {
            var result = resenhaControlador.verResenhaUsuario(alumnoForm, Optional.of(EstadoResenhaEnum.PUBLICADA));
            return result.stream().map(ResenaDTOMapper::toTeacher).toList();
        } catch (Exception e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }
}
