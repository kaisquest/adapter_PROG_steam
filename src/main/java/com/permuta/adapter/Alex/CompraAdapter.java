package com.permuta.adapter.Alex;

import com.permuta.adapter.Alex.modelMapper.CompraDTOMapper;
import com.permuta.adapter.Alex.modelMapper.CompraFormMapper;
import com.permuta.adapter.teacherCode.controller.ICompraController;
import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.CompraDTO;
import com.permuta.adapter.teacherCode.model.dto.ErrorDto;
import com.permuta.adapter.teacherCode.model.enums.ErrorType;
import com.permuta.adapter.teacherCode.model.form.CompraForm;
import org.alexyivan.controlador.ICompraControlador;
import org.alexyivan.exception.ValidacionException;
import org.alexyivan.modelo.enums.OpcionesReembolsoEnum;

import java.util.List;

public class CompraAdapter implements ICompraController {

    private final ICompraControlador compraControlador;

    public CompraAdapter(ICompraControlador compraControlador) {
        this.compraControlador = compraControlador;
    }

    @Override
    public CompraDTO realizarCompra(CompraForm form) throws ValidationException {
        var alumnoForm = CompraFormMapper.toStudent(form);
        try {
            var result = compraControlador.realizarCompra(alumnoForm);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("result", ErrorType.NO_EXISTE)));
            }
            return CompraDTOMapper.toTeacher(result.get());
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public CompraDTO procesarPago(long idCompra) throws ValidationException {
        // La firma del alumno requiere CompraForm adicional. Se construye un form mínimo.
        // La lógica real depende del alumno conseguir la compra por id internamente.
        try {
            var alumnoForm = new org.alexyivan.modelo.form.CompraForm(
                    0L, 0L, java.time.LocalDateTime.now(), null, 0f, 0, 0.0,
                    org.alexyivan.modelo.enums.EstadoCompraEnum.COMPLETADO
            );
            var result = compraControlador.procesarPago(alumnoForm, idCompra);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("id", ErrorType.NO_EXISTE)));
            }
            return CompraDTOMapper.toTeacher(result.get());
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public CompraDTO consultarCompra(long id, long idUsuario) throws ValidationException {
        try {
            var alumnoForm = new org.alexyivan.modelo.form.CompraForm(
                    idUsuario, 0L, java.time.LocalDateTime.now(), null, 0f, 0, 0.0, null
            );
            var result = compraControlador.consultarDetallesCompra(id, alumnoForm);
            return result.map(CompraDTOMapper::toTeacher).orElse(null);
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public CompraDTO solicitarReembolso(long idCompra) throws ValidationException {
        try {
            // El alumno requiere un motivo de reembolso. Se usa NO_GUSTA como valor por defecto.
            boolean resultado = compraControlador.solicitarReembolso(idCompra, OpcionesReembolsoEnum.NO_GUSTA);
            if (!resultado) {
                throw new ValidationException(List.of(new ErrorDto("reembolso", ErrorType.REEMBOLSO_NO_PERMITIDO)));
            }
            // Recuperar la compra actualizada mediante consultarDetallesCompra con form vacío
            var alumnoForm = new org.alexyivan.modelo.form.CompraForm(
                    0L, 0L, java.time.LocalDateTime.now(), null, 0f, 0, 0.0, null
            );
            var result = compraControlador.consultarDetallesCompra(idCompra, alumnoForm);
            return result.map(CompraDTOMapper::toTeacher).orElse(null);
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }
}
