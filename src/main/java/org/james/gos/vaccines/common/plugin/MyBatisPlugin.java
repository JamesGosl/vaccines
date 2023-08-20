package org.james.gos.vaccines.common.plugin;

import org.apache.ibatis.reflection.MetaObject;

/**
 * MyBaitsPlugin 基础接口
 *
 * @author James Gosl
 * @since 2023/08/14 23:48
 */
public interface MyBatisPlugin {

    /**修改属性*/
    default void setValue(String name, MetaObject metaObject, Object obj) {
        try {
            metaObject.setValue(name, obj);
        } catch (Exception e) {
            setValue("h.target." + name, metaObject, obj);
        }
    }

    /**获取属性*/
    default Object getValue(String name, MetaObject metaObject) {
        try {
            return metaObject.getValue(name);
        } catch (Exception e) {
            return getValue("h.target." + name, metaObject);
        }
    }
}
