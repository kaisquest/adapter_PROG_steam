package com.permuta.adapter.teacherCode.model.enums;

public enum ErrorType {
    REQUERIDO("El campo es obligatorio"),
    FORMATO_INVALIDO("El formato es inválido"),
    VALOR_DEMASIADO_ALTO("El valor es demasiado alto"),
    VALOR_DEMASIADO_BAJO("El valor es demasiado bajo"),
    LONGITUD_MINIMA("El valor no alcanza la longitud mínima"),
    LONGITUD_MAXIMA("El valor supera la longitud máxima"),
    DUPLICADO("El valor ya existe"),
    NO_EXISTE("El valor no existe"),
    YA_EXISTE("El valor ya existe"),
    ESTADO_INCORRECTO("El estado es incorrecto"),
    SALDO_INSUFICIENTE("Saldo insuficiente"), 
    REEMBOLSO_NO_PERMITIDO("Reembolso no permitido"), 
    JUEGO_NO_COMPRADO("El juego no ha sido comprado"), RESENA_EXISTENTE("La reseña ya existe");

    private final String mensaje;

    ErrorType(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }
}
