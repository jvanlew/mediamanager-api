package net.vanlew.mediamanager.api.domain.common.configuration.mongodb;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.vanlew.mediamanager.api.domain.common.configuration.mongodb.enumerations.ConnectionStringSchemes;

@Configuration
@ConfigurationProperties(prefix = "spring.mongodb")
@EnableMongoRepositories
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SpringMongoConfiguration extends AbstractMongoClientConfiguration {
    private String applicationName;

    private String connectionStringScheme;

    private ArrayList<String> hostPortList;

    private String database;

    @JsonAlias("user-name")
    private String userName;

    private String encryptedSecret;

    private String authenticationDatabase;

    private Boolean useTls;

    private MongoClient mongoClient;

    @Override
    protected String getDatabaseName() {
        return getDatabase();
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory) {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(mongoClient(), getDatabaseName()));
    }

    @Bean
    public MongoClient mongoClient() {
        MongoDBConnectionStringBuilder csBuilder = new MongoDBConnectionStringBuilder()
                .withScheme(ConnectionStringSchemes.fromString(getConnectionStringScheme()))
                .withCredentials(userName, encryptedSecret)
                .withHosts(hostPortList)
                .withOption("authSource", authenticationDatabase)
                .withOption("retryWrites", "true")
                .withOption("w", "majority")
                .withOption("appName", applicationName);

        var settings = MongoClientSettings.builder().applyConnectionString(csBuilder.build()).build();

        mongoClient = MongoClients.create(settings);

        return mongoClient;
    }
}
