package net.vanlew.mediamanager.api.domain.common.configuration.mongodb;

import java.util.List;
import org.springframework.lang.Nullable;

import com.mongodb.ConnectionString;

import net.vanlew.mediamanager.api.domain.common.configuration.mongodb.enumerations.ConnectionStringSchemes;

public class MongoDBConnectionStringBuilder {

    private final StringBuilder connectionString;

    public MongoDBConnectionStringBuilder() {
        connectionString = new StringBuilder(ConnectionStringSchemes.MONGODB.getDescription() + "://");
    }

    public MongoDBConnectionStringBuilder withScheme(ConnectionStringSchemes scheme) {
        // replace the existing scheme
        connectionString.replace(0, connectionString.indexOf("://"), scheme.getDescription());
        return this;
    }

    public MongoDBConnectionStringBuilder withCredentials(String username, String password) {

        // find the position after "://"
        int insertPosition = connectionString.indexOf("://") + 3;
        connectionString.insert(insertPosition, username + ":" + password + "@");
        return this;
    }

    public MongoDBConnectionStringBuilder withHost(String host, @Nullable Integer port) {
        if (port != null && port > 0) {
            connectionString.append(host).append(":").append(port);
        } else {
            connectionString.append(host);
        }
        return this;
    }

    public MongoDBConnectionStringBuilder withHosts(List<String> hostsWithPorts) {
        connectionString.append(String.join(",", hostsWithPorts));
        return this;
    }

    public MongoDBConnectionStringBuilder withDatabase(String database) {
        connectionString.append("/").append(database);
        return this;
    }

    public MongoDBConnectionStringBuilder withOption(String key, String value) {
        if (!connectionString.toString().contains("?")) {
            connectionString.append("?");
        } else {
            connectionString.append("&");
        }

        if (value != null && !value.isBlank()) {
            connectionString.append(key).append("=").append(value);
        }
        return this;
    }

    public ConnectionString build() {
        return new ConnectionString(connectionString.toString());
    }

    @Override
    public String toString() {
        return connectionString.toString();
    }
}

