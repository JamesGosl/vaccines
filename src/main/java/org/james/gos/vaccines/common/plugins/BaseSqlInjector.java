package org.james.gos.vaccines.common.plugins;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.SqlSession;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础SQL 注入插件
 *
 * @author James Gosl
 * @since 2023/08/14 23:17
 */
public abstract class BaseSqlInjector implements Interceptor, MyBatisPlugin, SqlInterceptorCache,
        ApplicationListener<ContextRefreshedEvent> {
    protected ApplicationContext applicationContext;
    protected SqlSession sqlSession;
    protected List<MappedStatement> mappedStatements;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.applicationContext = event.getApplicationContext();
        this.sqlSession = applicationContext.getBean(SqlSession.class);
        this.mappedStatements = new ArrayList<>();
        for (Object object : sqlSession.getConfiguration().getMappedStatements()) {
            if (object instanceof MappedStatement) {
                this.mappedStatements.add((MappedStatement) object);
            }
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        // MyBatis 反射模块
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());

        // 拿到通用Mapper 的接口信息，和SQL
        MappedStatement mappedStatement = (MappedStatement) getValue("delegate.mappedStatement", metaObject);
        // 获取Mapper 接口
        Class<?> mapperClass = getMapperToInterface(mappedStatement.getId());
        // 获取接口中的泛型
        Class<?> genericClass = getMapperToGeneric(mapperClass);


        // 修改SQL 弃用
//        setValue("delegate.boundSql.sql", metaObject,
//                getSql(sql, args, statementHandler, metaObject, mappedStatement, genericClass));

        // 修改GenericClass
        BoundSql boundSql = configureGeneric(genericClass, metaObject, statementHandler, mappedStatement);
        setValue("delegate.boundSql", metaObject, boundSql);
        setValue("delegate.parameterHandler.boundSql", metaObject, boundSql);
        setValue("delegate.resultSetHandler.boundSql", metaObject, boundSql);

        return invocation.proceed();
    }

    /**修改Generic*/
    public abstract BoundSql configureGeneric(Class<?> genericClass, MetaObject metaObject, StatementHandler statementHandler,
                                              MappedStatement mappedStatement) throws IllegalAccessException;

    /**修改SQL*/
    @Deprecated
    public abstract String getSql(String sql, Object[] args, StatementHandler statementHandler, MetaObject metaObject,
                                  MappedStatement mappedStatement, Class<?> genericClass) throws IllegalAccessException;


    /** 处理通用Mapper insertSelective */
    @Deprecated
    protected String insertSelective(String sql, String columnName) {
        if (!sql.contains(columnName)) {
            int flagFirst = sql.indexOf(")");
            String firstSQL = sql.substring(0, flagFirst) + "," + columnName;
            String lastSQL = sql.substring(flagFirst);
            int flagLast = lastSQL.indexOf(")", 1);
            return firstSQL + lastSQL.substring(0, flagLast) + ",?" + lastSQL.substring(flagLast);
        }
        return sql;
    }
}
