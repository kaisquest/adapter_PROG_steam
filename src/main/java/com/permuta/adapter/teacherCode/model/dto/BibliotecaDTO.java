package com.permuta.adapter.teacherCode.model.dto;

import java.time.LocalDate;

import com.permuta.adapter.teacherCode.model.enums.EstadoInstalacion;

public record BibliotecaDTO(
        long id,
        long idUsuario,
        UsuarioDTO usuario,
        long idJuego,
        JuegoDTO juego,
        LocalDate fechaAdquisicion,
        double tiempoJuegoTotal,
        LocalDate ultimaFechaJuego,
        EstadoInstalacion estadoInstalacion
) {}
