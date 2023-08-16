package org.james.gos.vaccines.common.plugins;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.james.gos.vaccines.common.annotation.FieldFill;
import org.james.gos.vaccines.common.annotation.TableField;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MyBatis 注入插件 @TableField
 *
 * @author James Gosl
 * @since 2023/08/14 23:18
 */
@Intercepts(
        @Signature(type = StatementHandler.class, method = "prepare", args = {
                Connection.class, Integer.class
        })
)
public class FieldSqlInjector extends BaseSqlInjector {

    @Override
    public BoundSql configureGeneric(Class<?> genericClass, MetaObject metaObject, StatementHandler statementHandler,
                                     MappedStatement mappedStatement) throws IllegalAccessException {
        // C增 R查 U更 D删
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        BoundSql boundSql = statementHandler.getBoundSql();

        // 处理插入操作 insert/insertSelective
        if(sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
            Object generic = getValue("boundSql.parameterObject", metaObject);

            List<Field> fields = getGenericToField(genericClass);

            if(generic instanceof MapperMethod.ParamMap) {
                MapperMethod.ParamMap<Object> map = (MapperMethod.ParamMap<Object>) generic;
                Object o = map.get("record");
                for (Field field : fields) {
                    TableField tableField = field.getAnnotation(TableField.class);
                    FieldFill fill = tableField.fill();
                    if(sqlCommandType == SqlCommandType.INSERT) {
                        if (fill == FieldFill.INSERT || fill == FieldFill.INSERT_UPDATE) {
                            field.set(o, new Date());
                        }
                    } else {
                        if (fill == FieldFill.UPDATE || fill == FieldFill.INSERT_UPDATE) {
                            field.set(o, new Date());
                        }
                    }
                }
                map.put("record", o);
            }
            else {
                for (Field field : fields) {
                    TableField tableField = field.getAnnotation(TableField.class);
                    FieldFill fill = tableField.fill();
                    if(sqlCommandType == SqlCommandType.INSERT) {
                        if (fill == FieldFill.INSERT || fill == FieldFill.INSERT_UPDATE) {
                            field.set(generic, new Date());
                        }
                    } else {
                        if (fill == FieldFill.UPDATE || fill == FieldFill.INSERT_UPDATE) {
                            field.set(generic, new Date());
                        }
                    }
                }
            }
            return mappedStatement.getBoundSql(generic);
        }

        return boundSql;
    }

    @Override
    public String getSql(String sql, Object[] args, StatementHandler statementHandler, MetaObject metaObject,
                         MappedStatement mappedStatement, Class<?> genericClass) throws IllegalAccessException {

        // 缓存功能，避免对同一个类的字段进行多次反射读取
        List<Field> fields = genericToField.get(genericClass);
        if(fields == null) {
            fields = new ArrayList<>();
            for (Field declaredField : genericClass.getDeclaredFields()) {
                if (declaredField.getAnnotation(TableField.class) != null) {
                    declaredField.setAccessible(true);
                    fields.add(declaredField);
                }
            }
            genericToField.put(genericClass, fields);
        }

        // INSERT INTO user  ( id,username,password,deleted,gmt_create,gmt_modified ) VALUES( ?,?,?,?,?,? )
        Object parameterObject = statementHandler.getBoundSql().getParameterObject();
        if (mappedStatement.getSqlCommandType() == SqlCommandType.INSERT) {
            for (Field field : fields) {
                TableField tableField = field.getAnnotation(TableField.class);

                // 处理普通insert
                FieldFill fill = tableField.fill();
                if (fill == FieldFill.INSERT || fill == FieldFill.INSERT_UPDATE) {
                    // 反射设置
                    field.set(parameterObject, new Date());
                }

                // 处理通用Mapper insertSelective
                String columnName = tableField.value();
                sql = insertSelective(sql, columnName);
            }
        }

        // UPDATE user  SET id = id,username = ?,password = ? WHERE  id = ?
        else if (mappedStatement.getSqlCommandType() == SqlCommandType.UPDATE) {
            StringBuilder preSql = new StringBuilder(sql.substring(0, sql.indexOf("WHERE") - 1));
            String newSql = sql.substring(sql.indexOf("WHERE"));

            // 入参索引
//            int paramIndex = preSql.toString().split("\\?").length;
//            Configuration configuration = mappedStatement.getConfiguration();
//            List<ParameterMapping> parameterMappings = statementHandler.getBoundSql().getParameterMappings();

            for (Field field : fields) {
                TableField tableField = field.getAnnotation(TableField.class);
                FieldFill fill = tableField.fill();
                if (fill == FieldFill.UPDATE || fill == FieldFill.INSERT_UPDATE) {
                    String value = tableField.value();
                    if(sql.contains(value)) {
                        if (parameterObject.getClass() == genericClass) {
                            field.set(parameterObject, new Date());
                        } else {
                            field.set(((Map<String, Object>) parameterObject).get("param1"), new Date());
                        }
                    } else {
                        preSql.append(", ").append(value).append("=now()").append(" ");

                        // 下面方式存在问题
//                        preSql.append(", ").append(value).append(" = ? ");
//                        field.set(parameterObject, new Date());
//                        ParameterMapping parameterMapping =
//                                new ParameterMapping.Builder(configuration, field.getName(), DateTypeHandler.class).javaType(Date.class).mode(ParameterMode.IN).build();
//                        parameterMappings.add(paramIndex++, parameterMapping);
                    }
                }
            }
            return preSql.append(newSql).toString();
        }

        return sql;
    }
}

