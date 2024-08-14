package org.example.springbootdemo.config.properties;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

/**
 * @date 2024/8/5
 **/
@Data

public class DatasourceConfigProperties extends DataSourceProperties {

    public static final String PREFIX = "db.multiple";

    /**
     * 数据源名称, 注册到spring的bean名称
     */
    private String datasourceName;

    /**
     * 是否是主要的bean  @Primary
     */
    private Boolean primary = false;

    /**
     * 是否创建事务管理器
     */
    private Boolean createTransactionManager = true;

    /**
     * transactionManagerName 默认  数据源名称 + TransactionManager
     */
    private String transactionManagerName;

    /**
     * hikari 数据连接配置
     */
    private HikariConfig hikari;


}

