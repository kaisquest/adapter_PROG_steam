package com.permuta.adapter.teacherCode.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.BibliotecaDTO;
import com.permuta.adapter.teacherCode.model.dto.JuegoDTO;
import com.permuta.adapter.teacherCode.model.dto.UsuarioDTO;
import com.permuta.adapter.teacherCode.model.enums.ClasificacionEdad;
import com.permuta.adapter.teacherCode.model.enums.EstadoInstalacion;
import com.permuta.adapter.teacherCode.model.enums.EstadoJuego;
import com.permuta.adapter.teacherCode.model.form.JuegoForm;
import com.permuta.adapter.teacherCode.model.form.UsuarioForm;
import com.permuta.factory.AdapterBundle;
import com.permuta.factory.AdapterFactory;

public class BibliotecaTest {

    private IUsuarioController usuarioController;
    private IJuegoController juegoController;
    private IBibliotecaController bibliotecaController;

    private UsuarioDTO usuarioValido;
    private JuegoDTO juegoValido;

    @BeforeEach
    public void setUp()
            throws ValidationException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        
        AdapterBundle bundle = AdapterFactory.getAdapterBundle("DavidParada");
        
        usuarioController = bundle.usuarioController();
        juegoController = bundle.juegoController();
        bibliotecaController = bundle.bibliotecaController();

        // Datos base disponibles en todos los tests
        usuarioValido = usuarioController.creaUsuarioDTO(new UsuarioForm(
                "usuario1",
                "usuario1@gmail.com",
                "12345678Aa@",
                "Usuario Uno",
                "España",
                LocalDate.now().minusYears(20),
                null));

        juegoValido = juegoController.crearJuego(new JuegoForm(
                "Half-Life 3",
                "El más esperado.",
                "Valve",
                LocalDate.now().minusDays(1),
                29.99,
                0,
                "Acción",
                ClasificacionEdad.PEGI_18,
                new String[] { "Español", "Inglés" },
                EstadoJuego.DISPONIBLE));
    }

    // =====================================================
    // Obtener juegos de usuario
    // =====================================================

    @Test
    public void obtenerJuegosUsuario_UsuarioValido_RetornaListaVacia() throws ValidationException {
        var biblioteca = bibliotecaController.obtenerJuegosUsuario(usuarioValido.id());

        assertNotNull(biblioteca);
        assertTrue(biblioteca.isEmpty());
    }

    @Test
    public void obtenerJuegosUsuario_UsuarioInvalido_LanzaValidationException() {
        assertThrows(ValidationException.class,
                () -> {
                    bibliotecaController.obtenerJuegosUsuario(9999L); // ID que no existe
                });
    }

    // =====================================================
    // Agregar juego a biblioteca
    // =====================================================

    @Test
    public void agregarJuegoBiblioteca_UsuarioYJuegoValidos_RetornaTrue() throws ValidationException {
        boolean resultado = bibliotecaController.agregarJuegoBiblioteca(
                usuarioValido.id(), juegoValido.id());

        assertTrue(resultado);
    }

    @Test
    public void agregarJuegoBiblioteca_VerificaEntradaEnBiblioteca() throws ValidationException {
        bibliotecaController.agregarJuegoBiblioteca(usuarioValido.id(), juegoValido.id());

        var biblioteca = bibliotecaController.obtenerJuegosUsuario(usuarioValido.id());

        assertFalse(biblioteca.isEmpty());
        assertEquals(juegoValido.id(), biblioteca.get(0).idJuego());
    }

    @Test
    public void agregarJuegoBiblioteca_TiempoJuegoInicialCero() throws ValidationException {
        bibliotecaController.agregarJuegoBiblioteca(usuarioValido.id(), juegoValido.id());

        var entrada = bibliotecaController.obtenerJuegosUsuario(usuarioValido.id()).get(0);

        assertEquals(0.0, entrada.tiempoJuegoTotal(), 0.001);
    }

    @Test
    public void agregarJuegoBiblioteca_EstadoInstalacionPorDefectoNoInstalado() throws ValidationException {
        bibliotecaController.agregarJuegoBiblioteca(usuarioValido.id(), juegoValido.id());

        var entrada = bibliotecaController.obtenerJuegosUsuario(usuarioValido.id()).get(0);

        assertEquals(EstadoInstalacion.NO_INSTALADO, entrada.estadoInstalacion());
    }

    @Test
    public void agregarJuegoBiblioteca_UsuarioInexistente_LanzaValidationException() {
        assertThrows(ValidationException.class,
                () -> bibliotecaController.agregarJuegoBiblioteca(9999L, juegoValido.id())); // usuario no existe
    }

    @Test
    public void agregarJuegoBiblioteca_JuegoInexistente_LanzaValidationException() {
        assertThrows(ValidationException.class,
                () -> bibliotecaController.agregarJuegoBiblioteca(usuarioValido.id(), 9999L)); // juego no existe
    }

    @Test
    public void agregarJuegoBiblioteca_JuegoDuplicado_LanzaValidationException() throws ValidationException {
        bibliotecaController.agregarJuegoBiblioteca(usuarioValido.id(), juegoValido.id());

        // Añadir el mismo juego por segunda vez debe lanzar excepción
        assertThrows(ValidationException.class,
                () -> bibliotecaController.agregarJuegoBiblioteca(usuarioValido.id(), juegoValido.id()));
    }

    @Test
    public void agregarJuegoBiblioteca_OtroUsuarioMismoJuego_Permitido() throws ValidationException {
        // Crear un segundo usuario
        UsuarioDTO usuario2 = usuarioController.creaUsuarioDTO(new UsuarioForm(
                "usuario2",
                "usuario2@gmail.com",
                "12345678Aa@",
                "Usuario Dos",
                "España",
                LocalDate.now().minusYears(25),
                null));

        bibliotecaController.agregarJuegoBiblioteca(usuarioValido.id(), juegoValido.id());

        // El mismo juego puede estar en la biblioteca de otro usuario
        boolean resultado = bibliotecaController.agregarJuegoBiblioteca(usuario2.id(), juegoValido.id());

        assertTrue(resultado);
    }

    // =====================================================
    // Eliminar juego de biblioteca
    // =====================================================

    @Test
    public void eliminarJuegoBiblioteca_EntradaExistente_EliminaCorrectamente() throws ValidationException {
        bibliotecaController.agregarJuegoBiblioteca(usuarioValido.id(), juegoValido.id());

        bibliotecaController.eliminarJuegoBiblioteca(usuarioValido.id(), juegoValido.id());

        var biblioteca = bibliotecaController.obtenerJuegosUsuario(usuarioValido.id());
        assertTrue(biblioteca.isEmpty());
    }

    @Test
    public void eliminarJuegoBiblioteca_EntradaInexistente_LanzaValidationException() {
        // El juego nunca fue agregado a la biblioteca
        assertThrows(ValidationException.class,
                () -> bibliotecaController.eliminarJuegoBiblioteca(usuarioValido.id(), juegoValido.id()));
    }

    @Test
    public void eliminarJuegoBiblioteca_UsuarioInexistente_LanzaValidationException() {
        assertThrows(ValidationException.class,
                () -> bibliotecaController.eliminarJuegoBiblioteca(9999L, juegoValido.id()));
    }

    // =====================================================
    // Actualizar tiempo de juego
    // =====================================================

    @Test
    public void actualizarTiempoJuego_EntradaValida_TiempoActualizado() throws ValidationException {
        bibliotecaController.agregarJuegoBiblioteca(usuarioValido.id(), juegoValido.id());

        BibliotecaDTO actualizado = bibliotecaController.actualizarTiempoJuego(
                usuarioValido.id(), juegoValido.id(), 10);

        assertNotNull(actualizado);
        assertEquals(10.0, actualizado.tiempoJuegoTotal(), 0.1);
    }

    @Test
    public void actualizarTiempoJuego_TiempoNegativo_LanzaValidationException() throws ValidationException {
        bibliotecaController.agregarJuegoBiblioteca(usuarioValido.id(), juegoValido.id());

        // Tiempo negativo no permitido
        assertThrows(ValidationException.class,
                () -> bibliotecaController.actualizarTiempoJuego(usuarioValido.id(), juegoValido.id(), -1));
    }

    @Test
    public void actualizarTiempoJuego_UsuarioInexistente_LanzaValidationException() {
        assertThrows(ValidationException.class,
                () -> bibliotecaController.actualizarTiempoJuego(9999L, juegoValido.id(), 5));
    }

    @Test
    public void actualizarTiempoJuego_JuegoNoEnBiblioteca_LanzaValidationException() {
        // El juego existe, pero no está en la biblioteca del usuario
        assertThrows(ValidationException.class,
                () -> bibliotecaController.actualizarTiempoJuego(
                        usuarioValido.id(), juegoValido.id(), 5));
    }

    // =====================================================
    // Consulta última sesión
    // =====================================================

    @Test
    public void consultaUltimaSesion_LanzaUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class,
                () -> bibliotecaController.consultaUltimaSesion());
    }

}
