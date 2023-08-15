package org.james.gos.vaccines.common.config;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.Configuration;
import org.james.gos.vaccines.common.doman.enums.YesOrNoEnum;
import org.james.gos.vaccines.common.plugins.FieldSqlInjector;
import org.james.gos.vaccines.common.plugins.LogicSqlInjector;
import org.james.gos.vaccines.common.plugins.ShowSqlPlugin;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * MyBatis 相关配置
 *
 * @author James Gosl
 * @since 2023/08/14 23:15
 */
@MapperScan(basePackages = "org.james.gos.vaccines", annotationClass = Repository.class)
@SpringBootConfiguration
@EnableTransactionManagement
public class MyBatisConfiguration {

    @Bean
    public FieldSqlInjector fieldSqlInjector() {
        return new FieldSqlInjector();
    }

    @Bean
    public LogicSqlInjector logicSqlInjector() {
        return new LogicSqlInjector(YesOrNoEnum.NO.getStatus(), YesOrNoEnum.YES.getStatus());
    }

    @Bean
    public ShowSqlPlugin showSqlPlugin() {
        return new ShowSqlPlugin();
    }

    /** MyBatis */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage("org.combat.mapper.po");

        // 配置中心
        Configuration configuration = new Configuration();
        configuration.setCacheEnabled(false);
        configuration.setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.setConfiguration(configuration);
//		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:sql/mapper/*.xml"));

        // 添加插件
        sqlSessionFactoryBean.setPlugins(logicSqlInjector(), fieldSqlInjector());
//        sqlSessionFactoryBean.setPlugins(logicSqlInjector(), fieldSqlInjector(), showSqlPlugin());
        return sqlSessionFactoryBean;
    }

    /** 事务管理器 */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

    /** 事务模板 */
    @Bean
    public TransactionTemplate transactionTemplate(DataSource dataSource) {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(transactionManager(dataSource));
        return transactionTemplate;
    }

    /** 分页插件 */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.put("helperDialect", "mysql");
        properties.put("reasonable", "true");
        properties.put("supportMethodsArguments", "true");
        properties.put("params", "count=countsql");
        // 把这个设置为true，会带RowBounds 第一个参数offset 当成PageNum 使用
        properties.setProperty("offsetAsPageNum","true");
        // 设置为true时，使用RowBounds 分页会进行count 查询
        properties.setProperty("rowBoundsWithCount","true");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
