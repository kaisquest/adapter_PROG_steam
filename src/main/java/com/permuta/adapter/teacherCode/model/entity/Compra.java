package com.permuta.adapter.teacherCode.model.entity;

import java.time.LocalDate;

import com.permuta.adapter.teacherCode.model.enums.EstadoCompra;
import com.permuta.adapter.teacherCode.model.enums.MetodoPago;

public record Compra(
        long id,
        long idUsuario,
        long idJuego,
        LocalDate fechaCompra,
        MetodoPago metodoPago,
        double precioSinDescuento,
        int descuentoAplicado,
        EstadoCompra estado
) {}
