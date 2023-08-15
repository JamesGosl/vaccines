package org.james.gos.vaccines.common.plugins;

import org.apache.ibatis.mapping.MappedStatement;
import org.james.gos.vaccines.common.annotation.TableField;
import org.james.gos.vaccines.common.annotation.TableLogic;

import javax.persistence.Id;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MyBaits 插件 本地缓存
 *
 * @author James Gosl
 * @since 2023/08/14 23:18
 */
public interface SqlInterceptorCache {

    /** Mapper 泛型中的Entity 缓存 */
    Map<Class<?>, Class<?>> mapperToGeneric = new HashMap<>();

    /** Mapper 泛型中的Entity 缓存 */
    Map<String, Class<?>> mapperToInterface = new HashMap<>();

    /** MappedStatement */
    Map<String, List<MappedStatement>> mappedStatement = new HashMap<>();

    /** Mapper 泛型中的Entity 构造方法 缓存 */
    Map<Class<?>, Constructor<?>> mapperToGenericConstructor = new HashMap<>();

    /** TableLogic 注解值缓存 */
    Map<Class<?>, String> genericToLogic = new HashMap<>();
    /** TableLogic 字段缓存 */
    Map<Class<?>, List<Field>> genericToLogicField = new HashMap<>();

    /** ID 字段缓存 */
    Map<Class<?>, List<Field>> genericToId = new HashMap<>();

    /** TableField 字段缓存 */
    Map<Class<?>, List<Field>> genericToField = new HashMap<>();

    default List<Field> getGenericToField(Class<?> genericClass) {
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
        return fields;
    }

    default Class<?> getMapperToInterface(String id) {
        Class<?> clazz = mapperToInterface.get(id);
        if(clazz == null) {
            try {
                clazz = Class.forName(id.substring(0, id.lastIndexOf(".")));
                mapperToInterface.put(id, clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }

    default List<Field> getGenericToId(Class<?> genericClass) {
        List<Field> fields = genericToId.get(genericClass);
        if (fields == null) {
            fields = new ArrayList<>();
            for (Field declaredField : genericClass.getDeclaredFields()) {
                if (declaredField.getAnnotation(Id.class) != null) {
                    declaredField.setAccessible(true);
                    fields.add(declaredField);
                }
            }
            genericToId.put(genericClass, fields);
        }
        return fields;
    }

    default List<Field> getGenericToLogicField(Class<?> genericClass) {
        List<Field> fields = genericToLogicField.get(genericClass);
        if (fields == null) {
            fields = new ArrayList<>();
            for (Field declaredField : genericClass.getDeclaredFields()) {
                if (declaredField.getAnnotation(TableLogic.class) != null) {
                    declaredField.setAccessible(true);
                    fields.add(declaredField);
                }
            }
            genericToLogicField.put(genericClass, fields);
        }
        return fields;
    }

    default Class<?> getMapperToGeneric(Class<?> mapperClass) throws NoSuchMethodException, ClassNotFoundException {
        Class<?> genericClass = mapperToGeneric.get(mapperClass);
        if (genericClass == null) {
            genericClass = Class.forName(
                    ((ParameterizedType) mapperClass.getGenericInterfaces()[0]).getActualTypeArguments()[0].getTypeName());
            mapperToGeneric.put(mapperClass, genericClass);

            Constructor<?> constructor = genericClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            mapperToGenericConstructor.put(genericClass, constructor);
        }
        return genericClass;
    }

    default List<MappedStatement> getMappedStatement(String resource, List<MappedStatement> mappedStatements) {
        List<MappedStatement> mappedStatementsList = mappedStatement.get(resource);
        if(null == mappedStatementsList) {
            mappedStatementsList =
                    mappedStatements.stream().filter(ms -> ms.getResource().equals(resource)).collect(Collectors.toList());
            mappedStatement.put(resource, mappedStatementsList);
        }
        return mappedStatementsList;
    }
}
