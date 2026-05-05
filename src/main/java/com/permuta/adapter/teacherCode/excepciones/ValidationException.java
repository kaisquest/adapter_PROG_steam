package com.permuta.adapter.teacherCode.excepciones;

import java.util.List;

import com.permuta.adapter.teacherCode.model.dto.ErrorDto;



public class ValidationException extends Exception {
    

    private List<ErrorDto> errores;

    public ValidationException(List<ErrorDto> errores) {
        super("Errores de validación");
        this.errores = errores;
    }
    public List<ErrorDto> getErrores() {
        return errores;
    }
    
}

