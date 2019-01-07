package com.ecmp.spring.boot.autoconfigure;

import com.ecmp.context.common.ConfigConstants;
import com.ecmp.edm.manager.DocumentManager;
import com.ecmp.edm.manager.IDocumentManager;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PreDestroy;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/28 23:48
 */
@Configuration
@ConditionalOnProperty(name = ConfigConstants.SWITCH_EDM_KEY, havingValue = "true")
@ConditionalOnClass({MongoClient.class})
@EnableConfigurationProperties({MongoProperties.class})
@ConditionalOnMissingBean(type = {"org.springframework.data.mongodb.MongoDbFactory"})
@ComponentScan(basePackages = "com.ecmp.edm")
@EnableMongoRepositories(basePackages = "com.ecmp.edm.manager.dao")
public class EDMAutoConfiguration extends AbstractMongoConfiguration implements Ordered {
    private final MongoProperties properties;
    private final MongoClientOptions options;
    private final MongoClientFactory factory;
    private MongoClient mongo;

    public EDMAutoConfiguration(MongoProperties properties, ObjectProvider<MongoClientOptions> options, Environment environment) {
        this.properties = properties;
        this.options = options.getIfAvailable();
        this.factory = new MongoClientFactory(properties, environment);
    }

    @Override
    public int getOrder() {
        return -100;
    }

    @PreDestroy
    public void close() {
        if (this.mongo != null) {
            this.mongo.close();
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public IDocumentManager documentManager() {
        return new DocumentManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public MongoClient mongo() {
        this.mongo = this.factory.createMongoClient(this.options);
        return this.mongo;
    }

    @Primary
    @Bean("edmGridFsTemplate")
    public GridFsTemplate edmGridFsTemplate(MongoDbFactory mongoDbFactory, MappingMongoConverter mappingMongoConverter) {
        return new GridFsTemplate(mongoDbFactory, mappingMongoConverter);
    }

    /**
     * Return the {@link MongoClient} instance to connect to. Annotate with {@link Bean} in case you want to expose a
     * {@link MongoClient} instance to the {@link ApplicationContext}.
     *
     * @return
     */
    @Override
    public MongoClient mongoClient() {
        return this.factory.createMongoClient(this.options);
    }

    /**
     * Return the name of the database to connect to.
     *
     * @return must not be {@literal null}.
     */
    @Override
    protected String getDatabaseName() {
        return properties.getDatabase();
    }
}
