package net.vanlew.mediamanager.library.enumerations;

import lombok.Getter;

@Getter
public enum ResultTypes {
    NONE("None"),
    OK("OK"),
    FAIL("Fail");

    private final String description;

    ResultTypes(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}