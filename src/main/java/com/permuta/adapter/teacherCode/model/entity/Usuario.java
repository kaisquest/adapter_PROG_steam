package com.permuta.adapter.teacherCode.model.entity;

import java.time.LocalDate;

import com.permuta.adapter.teacherCode.model.enums.EstadoCuenta;

public record Usuario(
        long id,
        String nombreUsuario,
        String email,
        String contrasena,
        String nombreReal,
        String pais,
        LocalDate fechaNacimiento,
        LocalDate fechaRegistro,
        String avatar,
        double saldoCartera,
        EstadoCuenta estadoCuenta
) {}
