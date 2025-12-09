package net.vanlew.mediamanager.api.domain.models.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum UserRoles {
    NONE("None"),

    SUBSCRIBER("Subscriber"),

    AUTHOR("Author"),

    CONTRIBUTOR("Contributor"),

    ADMINISTRATOR("Administrator");

    private final String description;

    UserRoles(String description) {
        this.description = description;
    }

    @JsonValue
    public String getValue() {
        return description;
    }

    @JsonCreator
    public static UserRoles fromValue(String value) {
        var returnValue = Arrays.stream(UserRoles.values())
                .filter(role -> role.getValue().equals(value))
                .findFirst()
                .orElse(null);

        if (returnValue == null) {
            returnValue = Arrays.stream(UserRoles.values())
                    .filter(role -> role.name().equals(value))
                    .findFirst()
                    .orElse(null);
        }

        return returnValue;
    }

    // return all UserRoles as a comma-separated string
    public static String getAllUserRoles() {
        StringBuilder allUserRoles = new StringBuilder();
        for (UserRoles role : UserRoles.values()) {
            if (role == UserRoles.NONE) {
                continue;
            }
            allUserRoles.append(role.toString()).append("\", \"");
        }
        allUserRoles.insert(0, "\"");

        return allUserRoles.substring(0, allUserRoles.length() - 3);
    }
}