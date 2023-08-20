package org.james.gos.vaccines.common.plugin;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.StringTokenizer;

/**
 * MyBatis 日志插件 @TableField
 *
 * @author James Gosl
 * @since 2023/08/14 23:46
 */
@Intercepts(
        @Signature(type = StatementHandler.class, method = "prepare", args = {
                Connection.class, Integer.class
        })
)
@Slf4j
public class ShowSqlPlugin implements Interceptor, MyBatisPlugin {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (log.isDebugEnabled()) {
            // RoutingStatementHandler
            StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
            // MyBatis 强大的反射工具类 非常牛皮
            MetaObject metaObject = MetaObject.forObject(statementHandler,
                    SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
            // 获取的是mapper.xml 中配置的id
            String sqlId = (String) getValue("delegate.mappedStatement.id", metaObject);
            // 获取的是mapper.xml 中配置的SQL 语句
            String sql = removeBreakingWhitespace((String) getValue("delegate.boundSql.sql", metaObject));
            log.debug("{}-->{}", sqlId, sql);
        }
        return invocation.proceed();
    }

    /**
     * 转化SQL
     */
    private String removeBreakingWhitespace(String original) {
        StringTokenizer whitespaceStripper = new StringTokenizer(original);
        StringBuilder builder = new StringBuilder();
        while (whitespaceStripper.hasMoreTokens()) {
            builder.append(whitespaceStripper.nextToken());
            builder.append(" ");
        }
        return builder.toString();
    }
}
