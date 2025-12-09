package net.vanlew.mediamanager.api.domain.models.enumerations;

import lombok.Getter;

@Getter
public enum OperationStatusTypes {
    UNKNOWN("Unknown"),
    SUCCESS("Success"),
    FAILURE("Failure");

    private final String description;

    OperationStatusTypes(String description) {
        this.description = description;
    }
}
