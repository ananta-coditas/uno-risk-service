package com.genomebrain.backend.service.dto;

import java.util.Objects;

/**
 * A DTO to send data to UI in particular format which matches the common component used by UI
 */
public class ValueLabelDTO {

    private String value;
    private String label;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ValueLabelDTO(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public ValueLabelDTO() {
    }

    @Override
    public String toString() {
        return "ValueLabelDTO{" +
            "value='" + value + '\'' +
            ", label='" + label + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ValueLabelDTO) {
            return Objects.equals(value, ((ValueLabelDTO) obj).value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }

}


