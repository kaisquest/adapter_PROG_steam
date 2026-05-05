package com.permuta.adapter.teacherCode.model.dto;


import java.time.LocalDate;

import com.permuta.adapter.teacherCode.model.enums.EstadoCompra;
import com.permuta.adapter.teacherCode.model.enums.MetodoPago;

public record CompraDTO(
        long id,
        long idUsuario,
        UsuarioDTO usuario,
        long idJuego,
        JuegoDTO juego,
        LocalDate fechaCompra,
        MetodoPago metodoPago,
        double precioSinDescuento,
        double descuentoAplicado,
        EstadoCompra estado
) {}
