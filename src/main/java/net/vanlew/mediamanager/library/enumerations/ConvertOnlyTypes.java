package net.vanlew.mediamanager.library.enumerations;

import lombok.Getter;

@Getter
public enum ConvertOnlyTypes {
    ALL("All"),
    PHOTOS("Photos"),
    VIDEOS("Videos");

    private final String description;

    ConvertOnlyTypes(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
