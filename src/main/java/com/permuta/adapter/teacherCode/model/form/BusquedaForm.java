package com.permuta.adapter.teacherCode.model.form;

import com.permuta.adapter.teacherCode.model.enums.ClasificacionEdad;
import com.permuta.adapter.teacherCode.model.enums.EstadoJuego;

public record BusquedaForm(String texto, String categoria,
     ClasificacionEdad clasificacionEdad, EstadoJuego estado) {

}
