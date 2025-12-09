package net.vanlew.mediamanager.api.domain.models.entities;

import java.time.Instant;
import java.util.EnumSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.vanlew.mediamanager.api.domain.models.enumerations.UserRoles;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(name = "asc_createdDateUtc", direction = IndexDirection.ASCENDING)
    private Instant createdDateUtc;

    private Instant modifiedDateUtc;

    @NotNull
    @Indexed(name = "require_unique_userName", unique = true)
    private String userName;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String hashId;

    @NotNull
    @Email
    private String emailAddress;

    private String passwordHash;

    private boolean active;

    private EnumSet<UserRoles> userRoles;

    private String securityQuestion;

    private String securityAnswerHash;

    private String timezoneInfo;

    @JsonIgnore

    public String getDisplayName() {
        return String.format("%s %s", this.firstName, this.lastName);
    }

    @JsonIgnore
    public boolean isAdministrator() {
        if (this.userRoles == null) {
            return false;
        }

        return this.getUserRoles().stream().map(UserRoles::getValue).anyMatch(UserRoles.ADMINISTRATOR.getValue()::equals);
    }

    public boolean hasRole(UserRoles role) {
        if (this.userRoles == null || this.userRoles.isEmpty()) {
            return false;
        }
        return this.getUserRoles().stream().map(UserRoles::getValue).anyMatch(role.getValue()::equals);
    }

    public void addUserRole(UserRoles role) {
        if (this.userRoles == null) {
            this.userRoles = EnumSet.noneOf(UserRoles.class);
        }
        this.userRoles.add(role);
    }

    public void removeUserRole(UserRoles role) {
        if (this.userRoles != null) {
            this.userRoles.remove(role);
        }
    }
}