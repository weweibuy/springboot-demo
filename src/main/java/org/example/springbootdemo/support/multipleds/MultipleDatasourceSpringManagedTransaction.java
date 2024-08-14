package org.example.springbootdemo.support.multipleds;

import org.mybatis.spring.transaction.SpringManagedTransaction;

import javax.sql.DataSource;

/**
 * @date 2024/8/5
 **/
public class MultipleDatasourceSpringManagedTransaction extends SpringManagedTransaction {

    public MultipleDatasourceSpringManagedTransaction(DataSource dataSource) {
        super(findDataSource(dataSource));
    }

    private static DataSource findDataSource(DataSource dataSource) {
        if (dataSource instanceof MultipleHolderDataSource multipleHolderDataSource) {
            return multipleHolderDataSource .findSpecDataSourceOrThrow();
        }
        return dataSource;
    }

}