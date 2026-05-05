package com.permuta.adapter.Alex;

import com.permuta.adapter.Alex.modelMapper.BibliotecaDTOMapper;
import com.permuta.adapter.teacherCode.controller.IBibliotecaController;
import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.BibliotecaDTO;
import com.permuta.adapter.teacherCode.model.dto.ErrorDto;
import com.permuta.adapter.teacherCode.model.enums.ErrorType;
import org.alexyivan.controlador.IBibliotecaControlador;
import org.alexyivan.exception.ValidacionException;
import org.alexyivan.modelo.enums.EstadoInstalacionEnum;
import org.alexyivan.modelo.enums.OrdenBusquedaBibliotecaEnum;
import org.alexyivan.modelo.form.BibliotecaForm;

import java.time.LocalDate;
import java.util.List;

public class BibliotecaAdapter implements IBibliotecaController {

    private final IBibliotecaControlador bibliotecaControlador;

    public BibliotecaAdapter(IBibliotecaControlador bibliotecaControlador) {
        this.bibliotecaControlador = bibliotecaControlador;
    }

    @Override
    public List<BibliotecaDTO> obtenerJuegosUsuario(Long idUsuario) throws ValidationException {
        try {
            var result = bibliotecaControlador.verBibliotecaPersonal(idUsuario, OrdenBusquedaBibliotecaEnum.FECHA_ADQUISICION);
            return result.stream().map(BibliotecaDTOMapper::toTeacher).toList();
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public boolean agregarJuegoBiblioteca(Long idUsuario, Long idJuego) throws ValidationException {
        var alumnoForm = new BibliotecaForm(
                idUsuario, idJuego, LocalDate.now(), 0f, null, EstadoInstalacionEnum.NO_INSTALADO
        );
        try {
            var result = bibliotecaControlador.anhadirJuego(alumnoForm);
            return result.isPresent();
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public void eliminarJuegoBiblioteca(Long idUsuario, Long idJuego) throws ValidationException {
        var alumnoForm = new BibliotecaForm(
                idUsuario, idJuego, LocalDate.now(), 0f, null, EstadoInstalacionEnum.NO_INSTALADO
        );
        try {
            var result = bibliotecaControlador.eliminarJuego(alumnoForm);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("id", ErrorType.NO_EXISTE)));
            }
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public BibliotecaDTO actualizarTiempoJuego(long idUsuario, long idJuego, int tiempoJugado) throws ValidationException {
        var alumnoForm = new BibliotecaForm(
                idUsuario, idJuego, LocalDate.now(), 0f, null, EstadoInstalacionEnum.NO_INSTALADO
        );
        try {
            var result = bibliotecaControlador.actualizarTempoJuego(alumnoForm, (float) tiempoJugado);
            if (result.isEmpty()) {
                throw new ValidationException(List.of(new ErrorDto("id", ErrorType.NO_EXISTE)));
            }
            return BibliotecaDTOMapper.toTeacher(result.get());
        } catch (ValidacionException e) {
            throw new ValidationException(List.of(new ErrorDto("validacion", ErrorType.FORMATO_INVALIDO)));
        }
    }

    @Override
    public BibliotecaDTO consultaUltimaSesion() {
        // El alumno requiere un BibliotecaForm con usuario y juego específicos,
        // mientras que el profesor no requiere parámetros (última sesión global).
        // Esta diferencia semántica NO tiene equivalente en el código del alumno.
        throw new UnsupportedOperationException(
                "NotImplementedException: consultaUltimaSesion() no tiene equivalente en el alumno. " +
                "El alumno requiere usuario y juego específicos (consultarUltimaSesion(BibliotecaForm))."
        );
    }
}
