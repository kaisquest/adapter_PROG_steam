package com.permuta.adapter.teacherCode.model.entity;

import java.time.LocalDate;

import com.permuta.adapter.teacherCode.model.enums.EstadoResena;

public record Resena(
        long id,
        long idUsuario,
        long idJuego,
        boolean recomendado,
        String texto,
        double horasJugadas,
        LocalDate fechaPublicacion,
        LocalDate fechaUltimaEdicion,
        EstadoResena estado
) {}
