package com.permuta.adapter.teacherCode.model.dto;

import java.time.LocalDate;

import com.permuta.adapter.teacherCode.model.enums.EstadoResena;

public record ResenaDTO(
        long id,
        long idUsuario,
        UsuarioDTO usuario,
        long idJuego,
        JuegoDTO juego,
        boolean recomendado,
        String texto,
        double horasJugadas,
        LocalDate fechaPublicacion,
        LocalDate fechaUltimaEdicion,
        EstadoResena estado
) {}
