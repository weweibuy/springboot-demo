package org.example.springbootdemo.support.multipleds;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.example.springbootdemo.config.properties.DatasourceConfigProperties;
import org.example.springbootdemo.config.properties.MultipleDatasourceAndMybatisProperties;
import org.example.springbootdemo.config.properties.MybatisAndDatasourceProperties;
import org.example.springbootdemo.utils.SpringResourcesUtils;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @date 2024/8/5
 **/
@Slf4j
public class MultipleDatasourceAndMybatisRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        // 配置文件绑定
        MultipleDatasourceAndMybatisProperties dataSourceAndMybatisProperties =
                SpringResourcesUtils.bindConfig(DatasourceConfigProperties.PREFIX,
                        MultipleDatasourceAndMybatisProperties.class, environment);

        if (log.isDebugEnabled()) {
            log.debug("绑定多数据源配置: {}", dataSourceAndMybatisProperties);
        }

        List<DatasourceConfigProperties> datasourceList = dataSourceAndMybatisProperties.getDatasource();
        if (CollectionUtils.isNotEmpty(datasourceList)) {
            registryDatasource(datasourceList, registry);
        }

        List<MybatisAndDatasourceProperties> mybatis = dataSourceAndMybatisProperties.getMybatis();
        if (CollectionUtils.isNotEmpty(mybatis)) {
            registryMybatis(mybatis, registry);
        }
    }

    private void registryMybatis(List<MybatisAndDatasourceProperties> mybatis, BeanDefinitionRegistry registry) {
        for (int i = 0; i < mybatis.size(); i++) {
            MybatisAndDatasourceProperties properties = mybatis.get(i);

            String sqlSessionFactoryBeanName = registrySqlSessionFactoryBean(properties, registry, i);

            registerMapperScan(properties, registry, sqlSessionFactoryBeanName, i);
        }
    }

    private void registryDatasource(List<DatasourceConfigProperties> datasourceList, BeanDefinitionRegistry registry) {
        for (int i = 0; i < datasourceList.size(); i++) {
            DatasourceConfigProperties properties = datasourceList.get(i);
            // 注册数据源
            registryDatasourceFactoryBean(properties, registry, i);

            if (Boolean.TRUE.equals(properties.getCreateTransactionManager())) {
                // 注册事务管理器
                registerTransactionManager(properties, registry);
            }
        }

    }


    /**
     * 注册 Datasource
     *
     * @param dataSourceProperties
     * @param registry
     * @param num
     */
    private void registryDatasourceFactoryBean(DatasourceConfigProperties dataSourceProperties, BeanDefinitionRegistry registry, int num) {
        AbstractBeanDefinition definition = BeanDefinitionBuilder
                .genericBeanDefinition(DatasourceFactoryBean.class)
                .addPropertyValue("dataSourceProperties", dataSourceProperties)
                .addPropertyValue("num", num)
                .setPrimary(Optional.ofNullable(dataSourceProperties.getPrimary())
                        .orElse(false))
                .getBeanDefinition();

        registry.registerBeanDefinition(dataSourceProperties.getDatasourceName(), definition);

    }


    /**
     * 注册 SqlSessionFactory
     *
     * @param mybatisProperties
     * @param registry
     * @param num
     * @return
     */
    public String registrySqlSessionFactoryBean(MybatisAndDatasourceProperties mybatisProperties, BeanDefinitionRegistry registry, int num) {

        AbstractBeanDefinition definition = BeanDefinitionBuilder
                .genericBeanDefinition(CustomerSqlSessionFactoryBean.class)
                .addPropertyValue("properties", mybatisProperties)
                .setPrimary(Optional.ofNullable(mybatisProperties.getPrimary()).orElse(false))
                .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                .getBeanDefinition();

        String beanName = sqlSessionFactoryBeanName(mybatisProperties.getSqlSessionFactoryName(), num);

        registry.registerBeanDefinition(beanName, definition);

        return beanName;
    }


    /**
     * 注册 mapperScan
     *
     * @param properties
     * @param registry
     * @param sqlSessionFactoryName
     */
    public void registerMapperScan(MybatisAndDatasourceProperties properties, BeanDefinitionRegistry registry,
                                   String sqlSessionFactoryName, Integer num) {

        BeanDefinition beanDefinition =
                BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class)
                        .addPropertyValue("sqlSessionFactoryBeanName", sqlSessionFactoryName)
                        .addPropertyValue("basePackage",
                                StringUtils.collectionToCommaDelimitedString(properties.getMapperScanPackages()))
                        .getBeanDefinition();

        registry.registerBeanDefinition("mapperScannerConfigurer" + num, beanDefinition);

    }

    /**
     * 注册事务管理器
     *
     * @param properties
     * @param registry
     */
    public void registerTransactionManager(DatasourceConfigProperties properties, BeanDefinitionRegistry registry) {
        String datasourceName = properties.getDatasourceName();

        AbstractBeanDefinition definition =
                BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManagerFactoryBean.class)
                        .addPropertyValue("datasourceName", datasourceName)
                        .setPrimary(Optional.ofNullable(properties.getPrimary()).orElse(false))
                        .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                        .getBeanDefinition();

        String transactionManagerName = Optional.ofNullable(properties.getTransactionManagerName())
                .orElseGet(() -> datasourceName + "TransactionManager");

        registry.registerBeanDefinition(transactionManagerName, definition);
    }


    private String sqlSessionFactoryBeanName(String sqlSessionFactoryName, int num) {
        return Optional.ofNullable(sqlSessionFactoryName)
                .filter(org.apache.commons.lang3.StringUtils::isNotBlank)
                .orElseGet(() -> "sqlSessionFactory" + num);
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }


}