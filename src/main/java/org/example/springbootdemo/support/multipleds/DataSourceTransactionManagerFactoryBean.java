package org.example.springbootdemo.support.multipleds;

import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

/**
 * @date 2024/8/5
 **/
@Setter
public class DataSourceTransactionManagerFactoryBean implements FactoryBean<TransactionManager> {

    private String datasourceName;

    private ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers;

    private Environment environment;

    private ApplicationContext applicationContext;


    @Override
    public TransactionManager getObject() {
        DataSource dataSource = applicationContext.getBean(datasourceName, DataSource.class);

        TransactionManager transactionManager = createTransactionManager(environment, dataSource);

        transactionManagerCustomizers.ifAvailable(customizers -> customizers.customize(transactionManager));

        return transactionManager;
    }

    @Override
    public Class<?> getObjectType() {
        return TransactionManager.class;
    }

    private DataSourceTransactionManager createTransactionManager(Environment environment, DataSource dataSource) {
        if (environment.getProperty("spring.dao.exceptiontranslation.enabled", Boolean.class, Boolean.TRUE)) {
            return new JdbcTransactionManager(dataSource);
        }
        return new DataSourceTransactionManager(dataSource);
    }


}