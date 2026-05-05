package com.permuta.adapter.teacherCode.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.JuegoDTO;
import com.permuta.adapter.teacherCode.model.enums.ClasificacionEdad;
import com.permuta.adapter.teacherCode.model.enums.EstadoJuego;
import com.permuta.adapter.teacherCode.model.form.BusquedaForm;
import com.permuta.adapter.teacherCode.model.form.JuegoForm;
import com.permuta.factory.AdapterBundle;
import com.permuta.factory.AdapterFactory;

public class JuegoTest {

        IJuegoController juegoController;

        JuegoForm validForm = new JuegoForm(
                        "Half-Life 3",
                        "El juego más esperado de la historia.",
                        "Valve",
                        LocalDate.now().minusDays(1),
                        29.99,
                        0,
                        "Acción",
                        ClasificacionEdad.PEGI_18,
                        new String[] { "Español", "Inglés" },
                        EstadoJuego.DISPONIBLE);

        @BeforeEach
        public void setUp() throws ValidationException, NoSuchFieldException, IllegalArgumentException,
                        IllegalAccessException {

                AdapterBundle bundle = AdapterFactory.getAdapterBundle("DavidParada");

                juegoController = bundle.juegoController();

        }

        // =====================================================
        // Crear juego
        // =====================================================

        @Test
        public void crearJuego_FormularioValido_RetornaJuegoDTO() throws ValidationException {
                var juego = juegoController.crearJuego(validForm);

                assertNotNull(juego);
                assertTrue(juego.id() > 0);
                assertEquals("Half-Life 3", juego.titulo());
                assertEquals(EstadoJuego.DISPONIBLE, juego.estado());
        }

        // ── Título ─────────────────────────────────────────────────────────────

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_TituloObligatorio() {
                var form = new JuegoForm(
                                "", // título obligatorio
                                "Descripción válida.",
                                "Valve",
                                LocalDate.now(),
                                29.99,
                                0,
                                "Acción",
                                ClasificacionEdad.PEGI_18,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_TituloNoUnico() throws ValidationException {
                juegoController.crearJuego(validForm);

                var tituloRepetidoForm = new JuegoForm(
                                "Half-Life 3", // título ya registrado
                                "Otra descripción.",
                                "Otro Desarrollador",
                                LocalDate.now(),
                                19.99,
                                0,
                                "Aventura",
                                ClasificacionEdad.PEGI_12,
                                new String[] { "Inglés" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(tituloRepetidoForm));
        }

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_TituloMayor100Caracteres() {
                var form = new JuegoForm(
                                "a".repeat(101), // 101 caracteres, máximo 100
                                "Descripción válida.",
                                "Valve",
                                LocalDate.now(),
                                29.99,
                                0,
                                "Acción",
                                ClasificacionEdad.PEGI_18,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        // ── Descripción ────────────────────────────────────────────────────────

        @Test
        public void crearJuego_FormularioValido_DescripcionNula_Permitida() throws ValidationException {
                var form = new JuegoForm(
                                "Portal 3",
                                null, // descripción opcional
                                "Valve",
                                LocalDate.now(),
                                19.99,
                                0,
                                "Puzzle",
                                ClasificacionEdad.PEGI_7,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                var juego = juegoController.crearJuego(form);

                assertNotNull(juego);
        }

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_DescripcionMayor2000Caracteres() {
                var form = new JuegoForm(
                                "Team Fortress 3",
                                "a".repeat(2001), // 2001 caracteres, máximo 2000
                                "Valve",
                                LocalDate.now(),
                                0.0,
                                0,
                                "Acción",
                                ClasificacionEdad.PEGI_12,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        // ── Desarrollador ──────────────────────────────────────────────────────

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_DesarrolladorObligatorio() {
                var form = new JuegoForm(
                                "Dota 3",
                                "Descripción.",
                                "", // desarrollador obligatorio
                                LocalDate.now(),
                                0.0,
                                0,
                                "Estrategia",
                                ClasificacionEdad.PEGI_12,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_DesarrolladorMenor2Caracteres() {
                var form = new JuegoForm(
                                "CS2 Legacy",
                                "Descripción.",
                                "V", // 1 carácter, mínimo 2
                                LocalDate.now(),
                                14.99,
                                0,
                                "Acción",
                                ClasificacionEdad.PEGI_16,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_DesarrolladorMayor100Caracteres() {
                var form = new JuegoForm(
                                "Aperture Science Simulator",
                                "Descripción.",
                                "a".repeat(101), // 101 caracteres, máximo 100
                                LocalDate.now(),
                                9.99,
                                0,
                                "Simulación",
                                ClasificacionEdad.PEGI_3,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        // ── Fecha de lanzamiento ───────────────────────────────────────────────

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_FechaLanzamientoObligatoria() {
                var form = new JuegoForm(
                                "Left 4 Dead 3",
                                "Descripción.",
                                "Valve",
                                null, // fecha de lanzamiento obligatoria
                                19.99,
                                0,
                                "Acción",
                                ClasificacionEdad.PEGI_18,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        @Test
        public void crearJuego_FormularioValido_FechaLanzamientoFutura_Permitida() throws ValidationException {
                var form = new JuegoForm(
                                "Artifact 2",
                                "El regreso.",
                                "Valve",
                                LocalDate.now().plusMonths(6), // fecha futura válida (preventa)
                                9.99,
                                0,
                                "Cartas",
                                ClasificacionEdad.PEGI_7,
                                new String[] { "Español" },
                                EstadoJuego.PREVENTA);

                var juego = juegoController.crearJuego(form);

                assertNotNull(juego);
        }

        // ── Precio base ────────────────────────────────────────────────────────

        @Test
        public void crearJuego_FormularioValido_PrecioBaseCero_Permitido() throws ValidationException {
                var form = new JuegoForm(
                                "Dota 2 Free",
                                "Gratis para todos.",
                                "Valve",
                                LocalDate.now(),
                                0.0, // juego gratuito
                                0,
                                "Estrategia",
                                ClasificacionEdad.PEGI_12,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                var juego = juegoController.crearJuego(form);

                assertNotNull(juego);
                assertEquals(0.0, juego.precioBase(), 0.001);
        }

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_PrecioBaseNegativo() {
                var form = new JuegoForm(
                                "SteamVR Ultimate",
                                "Descripción.",
                                "Valve",
                                LocalDate.now(),
                                -1.0, // precio negativo no permitido
                                0,
                                "VR",
                                ClasificacionEdad.PEGI_7,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_PrecioBaseSuperaMaximo() {
                var form = new JuegoForm(
                                "Valve Ultra Edition",
                                "Descripción.",
                                "Valve",
                                LocalDate.now(),
                                1000.0, // supera el máximo 999.99
                                0,
                                "Bundle",
                                ClasificacionEdad.PEGI_18,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        // ── Descuento actual ───────────────────────────────────────────────────

        @Test
        public void crearJuego_FormularioValido_DescuentoCero_PorDefecto() throws ValidationException {
                var form = new JuegoForm(
                                "Steam Deck: The Game",
                                "Descripción.",
                                "Valve",
                                LocalDate.now(),
                                49.99,
                                0, // descuento por defecto
                                "Simulación",
                                ClasificacionEdad.PEGI_3,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                var juego = juegoController.crearJuego(form);

                assertNotNull(juego);
                assertEquals(0, juego.descuentoActual(), 0.001);
        }

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_DescuentoNegativo() {
                var form = new JuegoForm(
                                "Ricochet 2",
                                "Descripción.",
                                "Valve",
                                LocalDate.now(),
                                4.99,
                                -1, // descuento negativo no permitido
                                "Acción",
                                ClasificacionEdad.PEGI_3,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_DescuentoMayor100() {
                var form = new JuegoForm(
                                "Ricochet 3",
                                "Descripción.",
                                "Valve",
                                LocalDate.now(),
                                4.99,
                                101, // descuento supera 100
                                "Acción",
                                ClasificacionEdad.PEGI_3,
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        // ── Clasificación por edad ─────────────────────────────────────────────

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_ClasificacionEdadObligatoria() {
                var form = new JuegoForm(
                                "Valve Classics",
                                "Descripción.",
                                "Valve",
                                LocalDate.now(),
                                9.99,
                                0,
                                "Bundle",
                                null, // clasificación por edad obligatoria
                                new String[] { "Español" },
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        // ── Idiomas disponibles ────────────────────────────────────────────────

        @Test
        public void crearJuego_FormularioValido_IdiomasNulos_Permitido() throws ValidationException {
                var form = new JuegoForm(
                                "Steam Workshop Creator",
                                "Descripción.",
                                "Valve",
                                LocalDate.now(),
                                0.0,
                                0,
                                "Herramientas",
                                ClasificacionEdad.PEGI_3,
                                null, // idiomas opcionales
                                EstadoJuego.DISPONIBLE);

                var juego = juegoController.crearJuego(form);

                assertNotNull(juego);
        }

        @Test
        public void crearJuego_FormularioInvalido_LanzaValidationException_IdiomasArrayVacio() {
                var form = new JuegoForm(
                                "Steam Remote Play",
                                "Descripción.",
                                "Valve",
                                LocalDate.now(),
                                0.0,
                                0,
                                "Herramientas",
                                ClasificacionEdad.PEGI_3,
                                new String[] {}, // array vacío: si se proporciona debe tener al menos uno
                                EstadoJuego.DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> juegoController.crearJuego(form));
        }

        // ── Estado ─────────────────────────────────────────────────────────────

        @Test
        public void crearJuego_FormularioValido_EstadoDisponible_PorDefecto() throws ValidationException {
                var juego = juegoController.crearJuego(validForm);

                assertEquals(EstadoJuego.DISPONIBLE, juego.estado());
        }

        // =====================================================
        // Listar catálogo
        // =====================================================

        @Test
        public void listarCatalogo_SinJuegos_RetornaListaVacia() {
                var catalogo = juegoController.listarCatalogo();

                assertNotNull(catalogo);
        }

        @Test
        public void listarCatalogo_ConJuegos_RetornaJuegosDisponibles() throws ValidationException {
                juegoController.crearJuego(validForm);

                var catalogo = juegoController.listarCatalogo();

                assertNotNull(catalogo);
                assertFalse(catalogo.isEmpty());
        }

        // =====================================================
        // Buscar juegos
        // =====================================================

        @Test
        public void buscarJuegos_TextoCoincidente_RetornaResultados() throws ValidationException {
                juegoController.crearJuego(validForm);

                var form = new BusquedaForm("Half-Life", null, null, null);
                List<JuegoDTO> resultados = juegoController.buscarJuegos(form);

                assertNotNull(resultados);
                assertFalse(resultados.isEmpty());
        }

        @Test
        public void buscarJuegos_TextoSinCoincidencia_RetornaListaVacia() throws ValidationException {
                juegoController.crearJuego(validForm);

                var form = new BusquedaForm("XxJuegoInexistenteXx", null, null, null);
                List<JuegoDTO> resultados = juegoController.buscarJuegos(form);

                assertNotNull(resultados);
                assertTrue(resultados.isEmpty());
        }

        // =====================================================
        // Aplicar descuento
        // =====================================================

        @Test
        public void aplicarDescuento_IdValido_DescuentoValido_RetornaJuegoActualizado() throws ValidationException {
                var juego = juegoController.crearJuego(validForm);

                var actualizado = juegoController.aplicarDescuento(juego.id(), 25);

                assertNotNull(actualizado);
                assertEquals(25, actualizado.descuentoActual(), 0.001);
        }

        @Test
        public void aplicarDescuento_IdInvalido_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> juegoController.aplicarDescuento(9999L, 10)); // ID que no existe
        }

        @Test
        public void aplicarDescuento_DescuentoFueraDeRango_LanzaValidationException() throws ValidationException {
                var juego = juegoController.crearJuego(validForm);

                assertThrows(ValidationException.class,
                                () -> juegoController.aplicarDescuento(juego.id(), 101)); // supera el máximo
        }

        // =====================================================
        // Cambiar estado
        // =====================================================

        @Test
        public void cambiarEstado_IdValido_EstadoValido_RetornaJuegoConNuevoEstado() throws ValidationException {
                var juego = juegoController.crearJuego(validForm);

                var actualizado = juegoController.cambiarEstado(juego.id(), EstadoJuego.NO_DISPONIBLE);

                assertNotNull(actualizado);
                assertEquals(EstadoJuego.NO_DISPONIBLE, actualizado.estado());
        }

        @Test
        public void cambiarEstado_IdInvalido_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> juegoController.cambiarEstado(9999L, EstadoJuego.DISPONIBLE)); // ID que no existe
        }

}
