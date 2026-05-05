package com.permuta.adapter.teacherCode.model.entity;

import java.time.LocalDate;

import com.permuta.adapter.teacherCode.model.enums.EstadoInstalacion;

public record Biblioteca(
        long id,
        long idUsuario,
        long idJuego,
        LocalDate fechaAdquisicion,
        double tiempoJuegoTotal,
        LocalDate ultimaFechaJuego,
        EstadoInstalacion estadoInstalacion
) {}
