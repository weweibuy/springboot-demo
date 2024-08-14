package org.example.springbootdemo.support.multipleds;

/**
 * @date 2024/8/5
 **/
public class SpecDataSourceContext {

    private static final ThreadLocal<String> datasourceContext = new ThreadLocal<>();

    public static void setSpecDatasource(String datasourceName) {
        datasourceContext.set(datasourceName);
    }

    public static String getSpecDatasource() {
        return datasourceContext.get();
    }

    public static void clear() {
        datasourceContext.remove();
    }
}
