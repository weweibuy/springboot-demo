package org.example.springbootdemo.support.multipleds;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.example.springbootdemo.config.properties.DatasourceConfigProperties;
import org.example.springbootdemo.utils.SpringResourcesUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @date 2024/8/5
 **/
@Setter
public class DatasourceFactoryBean implements FactoryBean<DataSource> {

    private DatasourceConfigProperties dataSourceProperties;

    private Environment environment;

    private Integer num;


    @Override
    public Class<?> getObjectType() {
        return DataSource.class;
    }

    @Override
    public DataSource getObject() {
        return hikariDataSource();
    }

    private DataSource hikariDataSource() {
        HikariDataSource dataSource = createDataSource(dataSourceProperties, HikariDataSource.class);
        HikariConfig hikari = dataSourceProperties.getHikari();
        if (hikari != null) {
            // 配置文件绑定 hikari
            SpringResourcesUtils.bindConfig(DatasourceConfigProperties.PREFIX
                            + ".datasource[" + num + "].hikari",
                    dataSource, environment);
        }

        // 连接池池name
        if (hikari == null || StringUtils.isBlank(hikari.getPoolName())) {
            String datasourceName = dataSourceProperties.getDatasourceName();
            dataSource.setPoolName(datasourceName + "-HikariPool");
        }

        return dataSource;
    }


    protected static <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type)
                .build();
    }


}
