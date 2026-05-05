package com.permuta.adapter.Alex;

import com.permuta.adapter.Alex.modelMapper.JuegoDTOMapper;
import com.permuta.adapter.Alex.modelMapper.JuegoFormMapper;
import com.permuta.adapter.Alex.modelMapper.BusquedaFormMapper;
import com.permuta.adapter.teacherCode.controller.IJuegoController;
import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.ErrorDto;
import com.permuta.adapter.teacherCode.model.dto.JuegoDTO;
import com.permuta.adapter.teacherCode.model.enums.ErrorType;
import com.permuta.adapter.teacherCode.model.enums.EstadoJuego;
import com.permuta.adapter.teacherCode.model.form.BusquedaForm;
import com.permuta.adapter.teacherCode.model.form.JuegoForm;
import org.alexyivan.controlador.IJuegoControlador;
import org.alexyivan.exception.ValidacionException;
import org.alexyivan.modelo.enums.EstadoJuegoEnum;
import org.alexyivan.modelo.enums.OrdenBusquedaJuegoEnum;

import java.util.List;

public class JuegoAdapter implements IJuegoController {

    private final IJuegoControlador juegoControlador;

    public JuegoAdapter(IJuegoControlador juegoControlador) {
        this.juegoControlador = juegoControlador;
    }

    @Override
    public JuegoDTO crearJuego(JuegoForm form) throws ValidationException {
        var alumnoForm = JuegoFormMapper.toStudent(form);
        try {
            var result = juegoControlador.anhadirJuegoCatalogo(alumnoForm);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("result", ErrorType.NO_EXISTE)));
            }
            return JuegoDTOMapper.toTeacher(result.get());
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public List<JuegoDTO> buscarJuegos(BusquedaForm form) {
        var alumnoForm = BusquedaFormMapper.toStudent(form);
        try {
            var result = juegoControlador.buscarJuegos(alumnoForm);
            return result.stream().map(JuegoDTOMapper::toTeacher).toList();
        } catch (ValidacionException e) {
            return List.of();
        }
    }

    @Override
    public List<JuegoDTO> listarCatalogo() {
        try {
            return juegoControlador.listarTodosJuegos(OrdenBusquedaJuegoEnum.ALFABETICO)
                    .stream()
                    .filter(j -> j.getEstado() == EstadoJuegoEnum.DISPONIBLE)
                    .map(JuegoDTOMapper::toTeacher)
                    .toList();
        } catch (ValidacionException e) {
            return List.of();
        }
    }

    @Override
    public JuegoDTO aplicarDescuento(long id, int descuento) throws ValidationException {
        try {
            var result = juegoControlador.actualizarDescuento(id, descuento);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("id", ErrorType.NO_EXISTE)));
            }
            return JuegoDTOMapper.toTeacher(result.get());
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public JuegoDTO cambiarEstado(long id, EstadoJuego nuevoEstado) throws ValidationException {
        EstadoJuegoEnum estadoAlumno = JuegoFormMapper.estadoJuegoToStudent(nuevoEstado);
        try {
            var result = juegoControlador.cambiarEstado(id, estadoAlumno);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("id", ErrorType.NO_EXISTE)));
            }
            return JuegoDTOMapper.toTeacher(result.get());
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }
}
