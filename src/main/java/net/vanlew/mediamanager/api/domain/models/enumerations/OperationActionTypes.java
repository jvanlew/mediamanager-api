package net.vanlew.mediamanager.api.domain.models.enumerations;

import lombok.Getter;

@Getter
public enum OperationActionTypes {
    UNKNOWN("Unknown"),

    GET("Get"),

    GET_MANY("Get Many"),

    ADD("Add"),

    ADD_MANY("Add Many"),

    UPDATE("Update"),

    UPDATE_MANY("Update Many"),

    DELETE("Delete"),

    DELETE_MANY("Delete Many");


    private final String description;

    OperationActionTypes(String description) {
        this.description = description;
    }
}
