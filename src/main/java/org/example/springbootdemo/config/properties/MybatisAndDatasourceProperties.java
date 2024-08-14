package org.example.springbootdemo.config.properties;

import lombok.Data;
import org.example.springbootdemo.support.multipleds.aspect.SpecDatasource;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;

import java.util.List;

/**
 * @date 2024/8/5
 **/
@Data
public class MybatisAndDatasourceProperties extends MybatisProperties {

    /**
     * 对应的 sqlSessionFactory 是否是主要
     */
    private Boolean primary;

    /**
     * sqlSessionFactory bean名称
     * 默认 sqlSessionFactory + 配置数组序号
     */
    private String sqlSessionFactoryName;

    /**
     * 包扫描
     */
    private List<String> mapperScanPackages;


    /**
     * 数据源 当使用多个数据源时支持通过 {@link SpecDatasource} 指定使用的数据源
     */
    private List<RefDatasource> datasource;


    @Data
    public static class RefDatasource {

        /**
         * 数据源名称 对应 {@link  DatasourceConfigProperties#datasourceName}
         */
        private String datasourceName;

        /**
         * 当有多个数据源时,是否是默认的数据源
         */
        private Boolean defaultDatasource;

    }

}
