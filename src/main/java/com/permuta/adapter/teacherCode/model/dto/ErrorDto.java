package com.permuta.adapter.teacherCode.model.dto;

import com.permuta.adapter.teacherCode.model.enums.ErrorType;

public record ErrorDto(String campo, ErrorType mensaje) {
}
