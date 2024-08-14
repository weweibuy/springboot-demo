package org.example.springbootdemo.config;

import org.example.springbootdemo.config.properties.MultipleDatasourceAndMybatisProperties;
import org.example.springbootdemo.support.multipleds.MultipleDatasourceAndMybatisRegister;
import org.example.springbootdemo.support.multipleds.aspect.SpecDatasourceAspect;
import org.example.springbootdemo.support.multipleds.aspect.SpecDatasourcePointcut;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 需要排除配置: {DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class}
 *
 * @date 2024/8/5
 **/
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties({MultipleDatasourceAndMybatisProperties.class})
@ConditionalOnMissingBean({DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class})
public class MultipleDatasourceConfig {


    @Bean
    public MultipleDatasourceAndMybatisRegister multipleDatasourceRegister() {
        return new MultipleDatasourceAndMybatisRegister();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnProperty(name = "db.multiple.enable-spec-datasource", havingValue = "true", matchIfMissing = true)
    public DefaultBeanFactoryPointcutAdvisor specDatasourceBeanFactoryPointcutAdvisor(MultipleDatasourceAndMybatisProperties properties) {
        DefaultBeanFactoryPointcutAdvisor advisor = new DefaultBeanFactoryPointcutAdvisor();

        Integer order = properties.getSpecDatasourceAspectOrder();
        advisor.setPointcut(new SpecDatasourcePointcut());
        advisor.setAdvice(new SpecDatasourceAspect());
        advisor.setOrder(order);

        return advisor;
    }


}
