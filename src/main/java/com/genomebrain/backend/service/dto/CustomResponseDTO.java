package com.genomebrain.backend.service.dto;


import org.apache.commons.lang3.builder.ToStringBuilder;

public class CustomResponseDTO {

    private String status;
    private Integer statusCode;
    private String message;
    private Object data;

    public CustomResponseDTO() {
    }

    public CustomResponseDTO(String status, Integer statusCode, String message,
                             Object data) {
        super();
        this.status = status;
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public CustomResponseDTO(Object data, String message) {
        this.statusCode = 200;
        this.message = message;
        this.data = data;
        this.status = "Success";

    }

    public CustomResponseDTO(Object data) {
        this.statusCode = 200;
        this.message = "Success";
        this.data = data;
    }

    public CustomResponseDTO(String message) {
        this.statusCode = 200;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("status", status)
                .append("statusCode", statusCode)
                .append("message", message)
                .append("data", data)
                .toString();
    }
}

