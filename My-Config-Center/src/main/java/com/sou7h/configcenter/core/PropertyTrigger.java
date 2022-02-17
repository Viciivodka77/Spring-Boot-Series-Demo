package com.sou7h.configcenter.core;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author sou7h
 * @description TODO
 * @date 2022年02月16日 11:05 下午
 */
public class PropertyTrigger {

    public static void change(String ymlContent) {
        Map<String, Object> newMap = YamlConverter.convert(ymlContent);
        Map<String, Object> oldMap = EnvInitializer.getEnvMap();

        oldMap.keySet().stream()
                .filter(key->newMap.containsKey(key))
                .filter(key->!newMap.get(key).equals(oldMap.get(key)))
                .forEach(key->{
                    System.out.println(key);
                    Object newVal = newMap.get(key);
                    oldMap.put(key, newVal);
                    doChange(key,newVal);
                });
        EnvInitializer.setEnvMap(oldMap);
    }

    private static void doChange(String propertyName, Object newValue) {
        System.out.println("newValue:"+newValue);
        Map<String, Map<Class<?>, String>> pool = VariablePool.getPool();
        Map<Class<?>, String> classProMap = pool.get(propertyName);

        classProMap.forEach((clazzName,realPropertyName)->{
            try {
                Object bean = SpringContextUtil.getBean(clazzName);
                Field field = clazzName.getDeclaredField(realPropertyName);
                field.setAccessible(true);
                field.set(bean, newValue.toString());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }



}
