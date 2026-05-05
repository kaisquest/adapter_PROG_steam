package com.permuta.adapter.teacherCode.model.dto;


import java.time.LocalDate;

import com.permuta.adapter.teacherCode.model.enums.ClasificacionEdad;
import com.permuta.adapter.teacherCode.model.enums.EstadoJuego;

public record JuegoDTO(
        long id,
        String titulo,
        String descripcion,
        String desarrollador,
        LocalDate fechaLanzamiento,
        double precioBase,
        double descuentoActual,
        String categoria,
        ClasificacionEdad clasificacionEdad,
        String[] idiomasDisponibles,
        EstadoJuego estado
) {}
