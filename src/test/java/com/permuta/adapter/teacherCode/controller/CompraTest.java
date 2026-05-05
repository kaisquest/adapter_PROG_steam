package com.permuta.adapter.teacherCode.controller;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.dto.JuegoDTO;
import com.permuta.adapter.teacherCode.model.dto.UsuarioDTO;
import com.permuta.adapter.teacherCode.model.enums.ClasificacionEdad;
import com.permuta.adapter.teacherCode.model.enums.EstadoCompra;
import com.permuta.adapter.teacherCode.model.enums.EstadoJuego;
import com.permuta.adapter.teacherCode.model.enums.MetodoPago;
import com.permuta.adapter.teacherCode.model.form.CompraForm;
import com.permuta.adapter.teacherCode.model.form.JuegoForm;
import com.permuta.adapter.teacherCode.model.form.UsuarioForm;
import com.permuta.factory.AdapterBundle;
import com.permuta.factory.AdapterFactory;

public class CompraTest {

        private IUsuarioController usuarioController;
        private IJuegoController juegoController;
        private ICompraController compraController;

        private UsuarioDTO usuarioValido;
        private JuegoDTO juegoValido;

        @BeforeEach
        public void setUp()
                        throws ValidationException, NoSuchFieldException, IllegalArgumentException,
                        IllegalAccessException {

                AdapterBundle bundle = AdapterFactory.getAdapterBundle("DavidParada");

                usuarioController = bundle.usuarioController();
                juegoController = bundle.juegoController();
                compraController = bundle.compraController();

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
        // Realizar compra
        // =====================================================

        @Test
        public void realizarCompra_FormularioValido_RetornaCompraDTO() throws ValidationException {
                var compra = compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoValido.id(),
                                MetodoPago.PAYPAL,
                                29.99,
                                0,
                                null));

                assertNotNull(compra);
                assertEquals(usuarioValido.id(), compra.idUsuario());
                assertEquals(juegoValido.id(), compra.idJuego());
        }

        @Test
        public void realizarCompra_EstadoPorDefectoPendiente() throws ValidationException {
                var compra = compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoValido.id(),
                                MetodoPago.TARJETA_CREDITO,
                                29.99,
                                0,
                                null));

                assertEquals(EstadoCompra.PENDIENTE, compra.estado());
        }

        @Test
        public void realizarCompra_FechaCompraGeneradaAutomaticamente() throws ValidationException {
                var compra = compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoValido.id(),
                                MetodoPago.TRANSFERENCIA,
                                29.99,
                                0,
                                null));

                assertNotNull(compra.fechaCompra());
                assertEquals(LocalDate.now(), compra.fechaCompra());
        }

        // ── Usuario ────────────────────────────────────────────────────────────

        @Test
        public void realizarCompra_UsuarioInexistente_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> compraController.realizarCompra(new CompraForm(
                                                9999L, // usuario no existe
                                                juegoValido.id(),
                                                MetodoPago.PAYPAL,
                                                29.99,
                                                0,
                                                null)));
        }

        // ── Juego ──────────────────────────────────────────────────────────────

        @Test
        public void realizarCompra_JuegoInexistente_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> compraController.realizarCompra(new CompraForm(
                                                usuarioValido.id(),
                                                9999L, // juego no existe
                                                MetodoPago.PAYPAL,
                                                29.99,
                                                0,
                                                null)));
        }

        @Test
        public void realizarCompra_JuegoNoDisponible_LanzaValidationException() throws ValidationException {
                // Cambiar estado del juego a NO_DISPONIBLE
                juegoController.cambiarEstado(juegoValido.id(), EstadoJuego.NO_DISPONIBLE);

                assertThrows(ValidationException.class,
                                () -> compraController.realizarCompra(new CompraForm(
                                                usuarioValido.id(),
                                                juegoValido.id(), // juego en estado NO_DISPONIBLE
                                                MetodoPago.PAYPAL,
                                                29.99,
                                                0,
                                                null)));
        }

        @Test
        public void realizarCompra_JuegoEnPreventa_Permitido() throws ValidationException {
                JuegoDTO juegoEnPreventa = juegoController.crearJuego(new JuegoForm(
                                "Portal 3",
                                null,
                                "Valve",
                                LocalDate.now().plusMonths(3),
                                19.99,
                                0,
                                "Puzzle",
                                ClasificacionEdad.PEGI_7,
                                new String[] { "Español" },
                                EstadoJuego.PREVENTA));

                var compra = compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoEnPreventa.id(),
                                MetodoPago.PAYPAL,
                                19.99,
                                0,
                                null));

                assertNotNull(compra);
        }

        @Test
        public void realizarCompra_JuegoEnAccesoAnticipado_Permitido() throws ValidationException {
                JuegoDTO juegoAccesoAnticipado = juegoController.crearJuego(new JuegoForm(
                                "Dota 3",
                                null,
                                "Valve",
                                LocalDate.now().minusDays(10),
                                0.0,
                                0,
                                "Estrategia",
                                ClasificacionEdad.PEGI_12,
                                new String[] { "Inglés" },
                                EstadoJuego.ACCESO_ANTICIPADO));

                var compra = compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoAccesoAnticipado.id(),
                                MetodoPago.CARTERA_STEAM,
                                0.0,
                                0,
                                null));

                assertNotNull(compra);
        }

        // ── Método de pago ─────────────────────────────────────────────────────

        @Test
        public void realizarCompra_MetodoPagoNulo_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> compraController.realizarCompra(new CompraForm(
                                                usuarioValido.id(),
                                                juegoValido.id(),
                                                null, // método de pago obligatorio
                                                29.99,
                                                0,
                                                null)));
        }

        // ── Precio sin descuento ───────────────────────────────────────────────

        @Test
        public void realizarCompra_PrecioSinDescuentoCero_permitido() {
                assertDoesNotThrow(() -> compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoValido.id(),
                                MetodoPago.PAYPAL,
                                0.0, // precio cero permitido
                                0,
                                null)));
        }

        @Test
        public void realizarCompra_PrecioSinDescuentoNegativo_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> compraController.realizarCompra(new CompraForm(
                                                usuarioValido.id(),
                                                juegoValido.id(),
                                                MetodoPago.PAYPAL,
                                                -1.0, // precio negativo no permitido
                                                0,
                                                null)));
        }

        // ── Descuento aplicado ─────────────────────────────────────────────────

        @Test
        public void realizarCompra_DescuentoCero_PorDefecto() throws ValidationException {
                var compra = compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoValido.id(),
                                MetodoPago.OTROS,
                                29.99,
                                0, // descuento por defecto
                                null));

                assertEquals(0.0, compra.descuentoAplicado(), 0.001);
        }

        @Test
        public void realizarCompra_DescuentoNegativo_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> compraController.realizarCompra(new CompraForm(
                                                usuarioValido.id(),
                                                juegoValido.id(),
                                                MetodoPago.PAYPAL,
                                                29.99,
                                                -1, // descuento negativo no permitido
                                                null)));
        }

        @Test
        public void realizarCompra_DescuentoMayor100_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> compraController.realizarCompra(new CompraForm(
                                                usuarioValido.id(),
                                                juegoValido.id(),
                                                MetodoPago.PAYPAL,
                                                29.99,
                                                101, // supera el máximo de 100
                                                null)));
        }

        // =====================================================
        // Procesar pago
        // =====================================================

        @Test
        public void procesarPago_CompraEnEstadoPendiente_RetornaCompraCompletada() throws ValidationException {
                var compra = compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoValido.id(),
                                MetodoPago.TARJETA_CREDITO,
                                29.99,
                                0,
                                null));

                var procesada = compraController.procesarPago(compra.id());

                assertNotNull(procesada);
                assertEquals(EstadoCompra.COMPLETADA, procesada.estado());
        }

        @Test
        public void procesarPago_IdInvalido_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> compraController.procesarPago(9999L)); // ID que no existe
        }

        @Test
        public void procesarPago_CompraYaCompletada_LanzaValidationException() throws ValidationException {
                var compra = compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoValido.id(),
                                MetodoPago.PAYPAL,
                                29.99,
                                0,
                                null));

                compraController.procesarPago(compra.id()); // primera vez: PENDIENTE → COMPLETADA

                // Segunda vez no debe ser posible, ya no está en PENDIENTE
                assertThrows(ValidationException.class,
                                () -> compraController.procesarPago(compra.id()));
        }

        // =====================================================
        // Consultar compra
        // =====================================================

        @Test
        public void consultarCompra_IdValidoUsuarioCorrecto_RetornaCompraDTO() throws ValidationException {
                var compra = compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoValido.id(),
                                MetodoPago.TRANSFERENCIA,
                                29.99,
                                0,
                                null));

                var detalle = compraController.consultarCompra(compra.id(), usuarioValido.id());

                assertNotNull(detalle);
                assertEquals(compra.id(), detalle.id());
        }

        @Test
        public void consultarCompra_IdInvalido_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> compraController.consultarCompra(9999L, usuarioValido.id())); // compra no existe
        }

        @Test
        public void consultarCompra_UsuarioNoEsDuenio_LanzaValidationException() throws ValidationException {
                var compra = compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoValido.id(),
                                MetodoPago.PAYPAL,
                                29.99,
                                0,
                                null));

                UsuarioDTO otroUsuario = usuarioController.creaUsuarioDTO(new UsuarioForm(
                                "usuario2",
                                "usuario2@gmail.com",
                                "12345678Aa@",
                                "Usuario Dos",
                                "España",
                                LocalDate.now().minusYears(25),
                                null));

                // La compra pertenece a usuarioValido, no a otroUsuario
                assertThrows(ValidationException.class,
                                () -> compraController.consultarCompra(compra.id(), otroUsuario.id()));
        }

        // =====================================================
        // Solicitar reembolso
        // =====================================================

        @Test
        public void solicitarReembolso_CompraCompletada_RetornaCompraReembolsada() throws ValidationException {
                usuarioController.aniadirSaldo(usuarioValido.id(), 29.99);
                var compra = compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoValido.id(),
                                MetodoPago.CARTERA_STEAM,
                                29.99,
                                0,
                                null));
                compraController.procesarPago(compra.id()); // PENDIENTE → COMPLETADA

                var reembolsada = compraController.solicitarReembolso(compra.id());

                assertNotNull(reembolsada);
                assertEquals(EstadoCompra.REEMBOLSADA, reembolsada.estado());
                assertEquals(29.99, usuarioController.consultarSaldo(usuarioValido.id()), 0.001); // saldo reembolsado
        }

        @Test
        public void solicitarReembolso_IdInvalido_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> compraController.solicitarReembolso(9999L)); // ID que no existe
        }

        @Test
        public void solicitarReembolso_CompraPendiente_LanzaValidationException() throws ValidationException {
                var compra = compraController.realizarCompra(new CompraForm(
                                usuarioValido.id(),
                                juegoValido.id(),
                                MetodoPago.PAYPAL,
                                29.99,
                                0,
                                null));

                // Solo se puede reembolsar si está COMPLETADA, no PENDIENTE
                assertThrows(ValidationException.class,
                                () -> compraController.solicitarReembolso(compra.id()));
        }

}
