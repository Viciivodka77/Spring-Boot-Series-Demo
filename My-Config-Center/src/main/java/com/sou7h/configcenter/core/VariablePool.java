package com.sou7h.configcenter.core;

import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author sou7h
 * @description TODO
 * @date 2022年02月16日 10:23 下午
 */
public class VariablePool {

    public static Map<String, Map<Class<?>,String>> pool=new HashMap<>();

    private static final String regex="^(\\$\\{)(.)+(\\})$";
    private static Pattern pattern;

    static{
        pattern= Pattern.compile(regex);
    }

    public static void add(Class<?> clazz){
        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(Value.class)){
                Value annotation = declaredField.getAnnotation(Value.class);
                String value = annotation.value();
                if (!pattern.matcher(value).matches()){
                    continue;
                }

                value=value.replace("${","");
                value=value.substring(0,value.length()-1);

                Map<Class<?>, String> clazzMap = Optional.ofNullable(pool.get(value)).orElse(new HashMap<>());

                clazzMap.put(clazz,declaredField.getName());
                pool.put(value,clazzMap);
            }
        }

    }

    public static Map<String, Map<Class<?>,String>> getPool(){
        return pool;
    }


}
