package com.permuta.adapter.teacherCode.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.permuta.adapter.teacherCode.excepciones.ValidationException;
import com.permuta.adapter.teacherCode.model.enums.EstadoCuenta;
import com.permuta.adapter.teacherCode.model.form.UsuarioForm;
import com.permuta.factory.AdapterBundle;
import com.permuta.factory.AdapterFactory;

public class UsuarioTest {

        IUsuarioController usuarioController;
        UsuarioForm validForm = new UsuarioForm("usuario1",
                        "usuario1@gmail.com",
                        "12345678Aa@",
                        "usuario1",
                        "España",
                        LocalDate.now().minusYears(20),
                        "usuario1.png");

        @BeforeEach
        public void setUp() throws ValidationException, NoSuchFieldException, IllegalArgumentException,
                        IllegalAccessException {
                AdapterBundle bundle = AdapterFactory.getAdapterBundle("DavidParada");

                usuarioController = bundle.usuarioController();
        }

        // =====================================================
        // Crear usuario
        // =====================================================

        @Test
        public void crearUsuarioDTO_FormularioValido_RetornaUsuarioDTO() throws ValidationException {
                // Act
                var user = usuarioController.creaUsuarioDTO(validForm);

                // Assert

                assertNotNull(user);
                var perfil = usuarioController.consultarPerfil(user.id());
                assertNotNull(perfil);

                assertEquals(perfil.estadoCuenta(), EstadoCuenta.ACTIVA);
                assertEquals(perfil.saldoCartera(), 0.0, 0.001);
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_NombreObligatorio() {
                // Arrange
                var invalidForm = new UsuarioForm("", // nombreUsuario obligatorio
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                // Act & Assert

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(invalidForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_NombreNoUnico()
                        throws ValidationException {

                usuarioController.creaUsuarioDTO(validForm);

                var nombreNoUnicoForm = new UsuarioForm("usuario1",
                                "usuario2@gmail.com",
                                "12345678Aa@",
                                "usuario2",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario2.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(nombreNoUnicoForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_NombreMenor3Caracteres() {
                var nombreCortoForm = new UsuarioForm("us",
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(nombreCortoForm));

        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_NombreMayor20Caracteres() {
                var nombreLargoForm = new UsuarioForm("usuario123456789012345", // 21 caracteres
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(nombreLargoForm));

        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_NombreConCaracteresInvalidos() {

                var nombreInvalidoForm = new UsuarioForm("usuario!", // caracteres especiales no permitidos
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(nombreInvalidoForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_NombreEmpiezaConNumero() {
                var nombreEmpiezaNumeroForm = new UsuarioForm("1usuario", // no puede empezar con número
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(nombreEmpiezaNumeroForm));
        }

        // ── Email ──────────────────────────────────────────────────────────────

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_EmailObligatorio() {
                var emailVacioForm = new UsuarioForm("usuario1",
                                "", // email obligatorio
                                "12345678Aa@",
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(emailVacioForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_EmailNoUnico()
                        throws ValidationException {
                usuarioController.creaUsuarioDTO(validForm);

                var emailNoUnicoForm = new UsuarioForm("usuario2",
                                "usuario1@gmail.com", // email ya registrado
                                "12345678Aa@",
                                "usuario2",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario2.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(emailNoUnicoForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_EmailFormatoInvalido() {
                var emailInvalidoForm = new UsuarioForm("usuario1",
                                "noesvalido", // sin @ ni dominio
                                "12345678Aa@",
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(emailInvalidoForm));
        }

        // ── Contraseña ─────────────────────────────────────────────────────────

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_ContrasenaObligatoria() {
                var contrasenaVaciaForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "", // contraseña obligatoria
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(contrasenaVaciaForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_ContrasenaMenor8Caracteres() {
                var contrasenaCortoForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "1234Aa", // menos de 8 caracteres
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(contrasenaCortoForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_ContrasenaSinMayuscula() {
                var sinMayusculaForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "12345678a@", // sin mayúscula
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(sinMayusculaForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_ContrasenaSinMinuscula() {
                var sinMinusculaForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "12345678A@", // sin minúscula
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(sinMinusculaForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_ContrasenaSinNumero() {
                var sinNumeroForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "AbcdefgA@", // sin número
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(sinNumeroForm));
        }

        // ── Nombre real ────────────────────────────────────────────────────────

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_NombreRealObligatorio() {
                var nombreRealVacioForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "", // nombre real obligatorio
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(nombreRealVacioForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_NombreRealMenor2Caracteres() {
                var nombreRealCortoForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "a", // 1 carácter, mínimo es 2
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(nombreRealCortoForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_NombreRealMayor50Caracteres() {
                var nombreRealLargoForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "a".repeat(51), // 51 caracteres, máximo es 50
                                "España",
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(nombreRealLargoForm));
        }

        // ── País ───────────────────────────────────────────────────────────────

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_PaisObligatorio() {
                var paisVacioForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "usuario1",
                                "", // país obligatorio
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(paisVacioForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_PaisNoValido() {
                var paisInvalidoForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "usuario1",
                                "Mordor", // país no válido en la lista predefinida
                                LocalDate.now().minusYears(20),
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(paisInvalidoForm));
        }

        // ── Fecha de nacimiento ────────────────────────────────────────────────

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_FechaNacimientoObligatoria() {
                var fechaNacimientoNulaForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "usuario1",
                                "España",
                                null, // fecha de nacimiento obligatoria
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(fechaNacimientoNulaForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_FechaNacimientoMenor13Anos() {
                var menorEdadForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(12), // tiene solo 12 años, mínimo 13
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(menorEdadForm));
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_FechaNacimientoFutura() {
                var fechaFuturaForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "usuario1",
                                "España",
                                LocalDate.now().plusDays(1), // fecha futura no permitida
                                "usuario1.png");

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(fechaFuturaForm));
        }

        // ── Fecha de registro ──────────────────────────────────────────────────

        @Test
        public void crearUsuarioDTO_FormularioValido_FechaRegistroGeneradaAutomaticamente() throws ValidationException {
                // Act
                var user = usuarioController.creaUsuarioDTO(validForm);

                // Assert
                assertNotNull(user.fechaRegistro());
                assertEquals(LocalDate.now(), user.fechaRegistro());
        }

        // ── Avatar ─────────────────────────────────────────────────────────────

        @Test
        public void crearUsuarioDTO_FormularioValido_AvatarNulo_Permitido() throws ValidationException {
                var sinAvatarForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                null); // avatar opcional, puede ser null

                var user = usuarioController.creaUsuarioDTO(sinAvatarForm);

                assertNotNull(user);
        }

        @Test
        public void crearUsuarioDTO_FormularioInvalido_LanzaValidationException_AvatarMayor100Caracteres() {
                var avatarLargoForm = new UsuarioForm("usuario1",
                                "usuario1@gmail.com",
                                "12345678Aa@",
                                "usuario1",
                                "España",
                                LocalDate.now().minusYears(20),
                                "a".repeat(101)); // 101 caracteres, máximo es 100

                assertThrows(ValidationException.class,
                                () -> usuarioController.creaUsuarioDTO(avatarLargoForm));
        }

        // =====================================================
        // Consultar perfil
        // =====================================================

        @Test
        public void consultarPerfil_IdValido_RetornaUsuarioDTO() throws ValidationException {
                var user = usuarioController.creaUsuarioDTO(validForm);

                var perfil = usuarioController.consultarPerfil(user.id());

                assertNotNull(perfil);
                assertEquals(user.id(), perfil.id());
        }

        @Test
        public void consultarPerfil_IdInvalido_RetornaNull() throws ValidationException {
                var perfil = usuarioController.consultarPerfil(9999L); // ID que no existe

                assertNull(perfil);
        }

        @Test
        public void consultarPerfil_NombreUsuarioValido_RetornaUsuarioDTO() throws ValidationException {
                var user = usuarioController.creaUsuarioDTO(validForm);

                var perfil = usuarioController.consultarPerfil(user.nombreUsuario());

                assertNotNull(perfil);
                assertEquals(user.nombreUsuario(), perfil.nombreUsuario());
        }

        @Test
        public void consultarPerfil_NombreUsuarioInvalido_RetornaNull() {
                var perfil = usuarioController.consultarPerfil("nombreNoExistente"); // nombre de usuario que no existe

                assertNull(perfil);
        }

        // =====================================================
        // Añadir saldo
        // =====================================================

        @Test
        public void aniadirSaldo_IdValido_CantidadValida_RetornaUsuarioDTOConSaldoActualizado()
                        throws ValidationException {
                var user = usuarioController.creaUsuarioDTO(validForm);

                var actualizado = usuarioController.aniadirSaldo(user.id(), 50.0);

                assertNotNull(actualizado);
                assertEquals(50.0, usuarioController.consultarSaldo(user.id()), 0.001);
        }

        @Test
        public void aniadirSaldo_IdInvalido_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> usuarioController.aniadirSaldo(9999L, 50.0)); // ID que no existe
        }

        @Test
        public void aniadirSaldo_CantidadNoValida_LanzaValidationException() throws ValidationException {
                var user = usuarioController.creaUsuarioDTO(validForm);

                assertThrows(ValidationException.class,
                                () -> usuarioController.aniadirSaldo(user.id(), -10.0)); // cantidad negativa no válida
        }

        // =====================================================
        // Consultar saldo
        // =====================================================

        @Test
        public void consultarSaldo_IdValido_RetornaSaldo() throws ValidationException {
                var user = usuarioController.creaUsuarioDTO(validForm);

                double saldo = usuarioController.consultarSaldo(user.id());

                assertEquals(0.0, saldo, 0.001); // saldo inicial es 0
        }

        @Test
        public void consultarSaldo_IdInvalido_LanzaValidationException() {
                assertThrows(ValidationException.class,
                                () -> usuarioController.consultarSaldo(9999L)); // ID que no existe
        }
}
