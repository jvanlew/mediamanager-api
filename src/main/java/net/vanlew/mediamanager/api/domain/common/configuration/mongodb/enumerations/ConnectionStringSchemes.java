package net.vanlew.mediamanager.api.domain.common.configuration.mongodb.enumerations;

import lombok.Getter;

@Getter
public enum ConnectionStringSchemes {
    MONGODB("mongodb"),
    MONGODB_SRV("mongodb+srv");

    private final String description;

    // convert string to enum value
    public static ConnectionStringSchemes fromString(String scheme) {
        for (ConnectionStringSchemes value : ConnectionStringSchemes.values()) {
            if (value.getDescription().equalsIgnoreCase(scheme)) {
                return value;
            }
            else if (scheme.toUpperCase().equalsIgnoreCase(value.toString())) {
                return value;
            }
        }
        throw new IllegalArgumentException("No enum constant for scheme: " + scheme);
    }

    ConnectionStringSchemes(String scheme) {
        this.description = scheme;
    }
}
