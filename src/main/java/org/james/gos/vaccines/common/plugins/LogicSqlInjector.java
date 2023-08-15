package org.james.gos.vaccines.common.plugins;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ClassUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.james.gos.vaccines.common.annotation.TableLogic;
import tk.mybatis.mapper.entity.Example;

import javax.persistence.Column;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis 注入插件 @TableLogic
 *
 * @author James Gosl
 * @since 2023/08/14 23:18
 */
@Intercepts(
        @Signature(type = StatementHandler.class, method = "prepare", args = {
                Connection.class, Integer.class
        })
)
@NoArgsConstructor
@AllArgsConstructor
public class LogicSqlInjector extends BaseSqlInjector {
    /** 未删除 */
    private Integer deletedDefault = 0;
    /** 已删除 */
    private Integer deletedValue = 1;

    @Override
    public BoundSql configureGeneric(Class<?> genericClass, MetaObject metaObject, StatementHandler statementHandler,
                                     MappedStatement mappedStatement) throws IllegalAccessException {
        // C增 R查 U更 D删
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

        BoundSql boundSql = statementHandler.getBoundSql();

        // 这里不对UPDATE 事件进行更改，各有各的做法
        if(sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.SELECT) {
            Object generic = getValue("boundSql.parameterObject", metaObject);

            // 处理Example 类型
            // selectByExample(example)
            if (generic instanceof Example) {
                Example example = (Example) generic;
                List<Field> fields = getGenericToLogicField(genericClass);
                for (Field field : fields) {
                    example.and().andEqualTo(field.getName(), deletedDefault);
                }
                return boundSql;
            }

            // 处理接口泛型类型
            // select(auth)
            else if(generic != null && !ClassUtils.isPrimitiveOrWrapper(generic.getClass())) {
                // 获取符合字段的
                List<Field> fields = getGenericToLogicField(genericClass);
                // 不是按照对象查询
                for (Field field : fields) {
                    field.set(generic, this.deletedDefault);
                }
            }

            // 处理基本数据类型
            // selectByPrimaryKey(1L)
            else if(generic != null && ClassUtils.isPrimitiveOrWrapper(generic.getClass())) {
                Constructor<?> constructor = mapperToGenericConstructor.get(genericClass);
                try {
                    Object obj = constructor.newInstance();
                    // 获取符合字段的
                    for (Field field : getGenericToId(genericClass)) {
                        field.set(obj, generic);
                    }

                    for (Field field : getGenericToLogicField(genericClass)) {
                        field.set(obj, this.deletedDefault);
                    }
                    // 将selectByPrimaryKey(1L) 改为select(auth)
                    setValue("delegate.parameterHandler.parameterObject", metaObject, obj);

                    // 查找select MappedStatement
                    String flag =  getMapperToInterface(mappedStatement.getId()).getName() + ".select";
                    // 获取这个接口的全部MappedStatement
                    List<MappedStatement> mappedStatements = getMappedStatement(mappedStatement.getResource(), this.mappedStatements);
                    for (MappedStatement statement : mappedStatements) {
                        if (flag.equals(statement.getId())) {
                            return statement.getBoundSql(obj);
                        }
                    }
                } catch (InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            // 处理没有数据的
            // authMapper.selectAll()
            else if(generic == null) {
                Constructor<?> constructor = mapperToGenericConstructor.get(genericClass);
                try {
                    Object obj = constructor.newInstance();
                    for (Field field : getGenericToLogicField(genericClass)) {
                        field.set(obj, this.deletedDefault);
                    }
                    // 将authMapper.selectAll() 改为select(auth)
                    setValue("delegate.parameterHandler.parameterObject", metaObject, obj);

                    // 查找select MappedStatement
                    String flag =  getMapperToInterface(mappedStatement.getId()).getName() + ".select";
                    // 获取这个接口的全部MappedStatement
                    List<MappedStatement> mappedStatements = getMappedStatement(mappedStatement.getResource(), this.mappedStatements);
                    for (MappedStatement statement : mappedStatements) {
                        if (flag.equals(statement.getId())) {
                            return statement.getBoundSql(obj);
                        }
                    }
                } catch (InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            return mappedStatement.getBoundSql(generic);
        }

        // 删除操作 只能通过ID 进行更新操作
        if(sqlCommandType == SqlCommandType.DELETE) {
            Object generic = getValue("boundSql.parameterObject", metaObject);

            // 处理接口泛型类型
            // delete(auth)
            if(generic != null && !ClassUtils.isPrimitiveOrWrapper(generic.getClass())) {
                List<Field> fields = getGenericToLogicField(genericClass);
                for (Field field : fields) {
                    field.set(generic, this.deletedValue);
                }

                // 查找update MappedStatement
                String flag =  getMapperToInterface(mappedStatement.getId()).getName() + ".updateByPrimaryKeySelective";
                // 获取这个接口的全部MappedStatement
                List<MappedStatement> mappedStatements = getMappedStatement(mappedStatement.getResource(), this.mappedStatements);
                for (MappedStatement statement : mappedStatements) {
                    if (flag.equals(statement.getId())) {
                        return statement.getBoundSql(generic);
                    }
                }
            }

            // 处理基本数据类型
            // selectByPrimaryKey(1L)
            else if(generic != null && ClassUtils.isPrimitiveOrWrapper(generic.getClass())) {
                Constructor<?> constructor = mapperToGenericConstructor.get(genericClass);
                try {
                    Object obj = constructor.newInstance();
                    for (Field field : getGenericToId(genericClass)) {
                        field.set(obj, generic);
                    }
                    for (Field field : getGenericToLogicField(genericClass)) {
                        field.set(obj, this.deletedValue);
                    }
                    // 将selectByPrimaryKey(1L) 改为select(auth)
                    setValue("delegate.parameterHandler.parameterObject", metaObject, obj);

                    // 查找select MappedStatement
                    String flag =  getMapperToInterface(mappedStatement.getId()).getName() + ".updateByPrimaryKeySelective";
                    // 获取这个接口的全部MappedStatement
                    List<MappedStatement> mappedStatements = getMappedStatement(mappedStatement.getResource(), this.mappedStatements);
                    for (MappedStatement statement : mappedStatements) {
                        if (flag.equals(statement.getId())) {
                            return statement.getBoundSql(obj);
                        }
                    }
                } catch (InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            return mappedStatement.getBoundSql(generic);
        }
        return boundSql;
    }

    @Override
    public String getSql(String sql, Object[] args, StatementHandler statementHandler, MetaObject metaObject,
                         MappedStatement mappedStatement, Class<?> genericClass) throws IllegalAccessException {

        // 如果是插入的话，那么给LogicSqlInjector 改为deletedDefault
        if(mappedStatement.getSqlCommandType() == SqlCommandType.INSERT) {
            List<Field> fields = genericToLogicField.get(genericClass);
            if(fields == null) {
                fields = new ArrayList<>();
                for (Field declaredField : genericClass.getDeclaredFields()) {
                    if (declaredField.getAnnotation(TableLogic.class) != null) {
                        declaredField.setAccessible(true);
                        fields.add(declaredField);
                    }
                }
                genericToLogicField.put(genericClass, fields);
            }

            // 处理普通insert
            Object parameterObject = statementHandler.getBoundSql().getParameterObject();
            if (mappedStatement.getSqlCommandType() == SqlCommandType.INSERT) {
                for (Field field : fields) {
                    field.set(parameterObject, this.deletedDefault);
                }
            }

            // 处理通用Mapper insertSelective
            String deleted = getDeleted(genericClass);
            return insertSelective(sql, deleted);
        }
        // 如果是删除的话，那么给LogicSqlInjector 改为deletedValue
        else if(mappedStatement.getSqlCommandType() == SqlCommandType.DELETE && sql.contains("DELETE")) {
            setValue("delegate.mappedStatement.sqlCommandType", metaObject, SqlCommandType.UPDATE);


            // DELETE FROM user WHERE id = ?
            // UPDATE user SET deleted = 1 WHERE id = ?
            return sql.substring(0, sql.indexOf("WHERE")).replaceAll("DELETE FROM", "UPDATE") +
                    "SET " + getDeleted(genericClass) + "=" + this.deletedValue + " " + sql.substring(sql.indexOf("WHERE"));
        }
        // 如果是查询的话，增加逻辑删除值
        else if(mappedStatement.getSqlCommandType() == SqlCommandType.SELECT){
            // 不包含WHERE
            if(!sql.contains("WHERE") && !sql.contains("where")) {
                sql += " WHERE ";
            }
            // 包含WHERE
            else {
                sql += " AND ";
            }

            return sql + getDeleted(genericClass) + "=" + this.deletedDefault;
        }

        return sql;
    }

    /***
     * 获取deleted 字段的数据库字段名
     * @param genericClass ORM 类
     * @return deleted 字段的数据库字段名
     */
    private String getDeleted(Class<?> genericClass) {
        // 缓存功能，避免对同一个类的字段进行多次反射读取
        String deleted = genericToLogic.get(genericClass);
        if(deleted == null) {
            for (Field declaredField : genericClass.getDeclaredFields()) {
                if (declaredField.getAnnotation(TableLogic.class) != null) {
                    // 增加Column 策略，支持指定逻辑删除字段
                    Column column = declaredField.getAnnotation(Column.class);
                    if (column == null) {
                        deleted = declaredField.getName();
                    }
                    else {
                        deleted = column.name();
                    }
                    genericToLogic.put(genericClass, deleted);
                }
            }
        }

        return deleted;
    }
}
