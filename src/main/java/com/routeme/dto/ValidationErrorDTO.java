package com.routeme.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDTO {
    private List<FieldErrorDTO> fieldErrors = new ArrayList<FieldErrorDTO>();

    public void setFieldErrors(List<FieldErrorDTO> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public void addFieldError(String field, String message) {
        FieldErrorDTO fieldError = new FieldErrorDTO(field, message);
        this.fieldErrors.add(fieldError);
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }
}
