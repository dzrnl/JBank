package ru.dzrnl.bank.data.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import ru.dzrnl.bank.data.entities.AccountEntity;
import ru.dzrnl.bank.data.entities.TransactionEntity;
import ru.dzrnl.bank.data.entities.UserEntity;

import java.util.Properties;

@Configuration
@PropertySource("classpath:data-hibernate.properties")
@ComponentScan(basePackages = "ru.dzrnl.bank.data")
public class DataConfig {
    private final Environment env;

    public DataConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public SessionFactory sessionFactory() {
        Properties props = new Properties();
        props.setProperty("hibernate.connection.driver_class", env.getProperty("hibernate.connection.driver_class"));
        props.setProperty("hibernate.connection.url", env.getProperty("hibernate.connection.url"));
        props.setProperty("hibernate.connection.username", env.getProperty("hibernate.connection.username"));
        props.setProperty("hibernate.connection.password", env.getProperty("hibernate.connection.password"));

        org.hibernate.cfg.Configuration config = new org.hibernate.cfg.Configuration();
        config.setProperties(props);
        config.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());

        config.addAnnotatedClass(UserEntity.class);
        config.addAnnotatedClass(AccountEntity.class);
        config.addAnnotatedClass(TransactionEntity.class);

        return config.buildSessionFactory();
    }
}