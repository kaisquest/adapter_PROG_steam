package com.permuta.adapter.teacherCode.model.entity;

import java.time.LocalDate;

import com.permuta.adapter.teacherCode.model.enums.ClasificacionEdad;
import com.permuta.adapter.teacherCode.model.enums.EstadoJuego;

public record Juego(
        long id,
        String titulo,
        String descripcion,
        String desarrollador,
        LocalDate fechaLanzamiento,
        double precioBase,
        int descuentoActual,
        String categoria,
        ClasificacionEdad clasificacionEdad,
        String[] idiomasDisponibles,
        EstadoJuego estado
) {}
