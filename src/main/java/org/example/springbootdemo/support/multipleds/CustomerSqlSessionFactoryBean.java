package org.example.springbootdemo.support.multipleds;

import lombok.Setter;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.example.springbootdemo.config.properties.MybatisAndDatasourceProperties;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @date 2024/8/5
 **/
@Setter
public class CustomerSqlSessionFactoryBean implements FactoryBean<SqlSessionFactory> {

    private MybatisAndDatasourceProperties properties;

    private Interceptor[] plugins;

    private TypeHandler<?>[] typeHandlers;

    private ObjectProvider<Map<String, DataSource>> datasourceMapProvider;


    @Override
    public SqlSessionFactory getObject() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setMapperLocations(properties.resolveMapperLocations());
        factoryBean.setPlugins(plugins);
        factoryBean.setTypeHandlers(typeHandlers);

        DataSource dataSource = buildDatasource();
        factoryBean.setDataSource(dataSource);

        if (dataSource instanceof MultipleHolderDataSource) {
            //  指定数据源 + 数据源事务上下文 支持
            factoryBean.setTransactionFactory(new MultipleDatasourceTransactionFactory());
        }

        return factoryBean.getObject();
    }


    private DataSource buildDatasource() {
        List<MybatisAndDatasourceProperties.RefDatasource> datasourceList = properties.getDatasource();
        if (CollectionUtils.isEmpty(datasourceList)) {
            throw new IllegalStateException("必须配置Mybatis的数据源");
        }

        Map<String, DataSource> dataSourceMap = datasourceMapProvider.getObject();
        Map<String, DataSource> refDataSourceMap = datasourceList.stream()
                .collect(Collectors.toMap(MybatisAndDatasourceProperties.RefDatasource::getDatasourceName,
                        p -> Optional.ofNullable(dataSourceMap.get(p.getDatasourceName()))
                                .orElseThrow(() -> new IllegalStateException("数据源: " + p.getDatasourceName() + " 不存在"))));

        if (refDataSourceMap.size() == 1) {
            return refDataSourceMap.values().iterator().next();
        }

        List<DataSource> defaultDatasourceList = datasourceList.stream()
                .filter(p -> Boolean.TRUE.equals(p.getDefaultDatasource()))
                .map(p -> dataSourceMap.get(p.getDatasourceName()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(defaultDatasourceList) || defaultDatasourceList.size() > 1) {
            throw new IllegalStateException("必须指定一个默认的数据源");
        }

        return new MultipleHolderDataSource(refDataSourceMap, defaultDatasourceList.get(0));
    }


    @Override
    public Class<?> getObjectType() {
        return SqlSessionFactory.class;
    }
}
