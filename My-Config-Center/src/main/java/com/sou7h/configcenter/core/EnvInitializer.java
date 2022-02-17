package com.sou7h.configcenter.core;

import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sou7h
 * @description TODO
 * @date 2022年02月16日 10:35 下午
 */
public class EnvInitializer {

    private static Map<String,Object> envMap=new HashMap<>();

    public static void init(){
        String path = EnvInitializer.class.getResource("/").getPath();
        List<String> fileByType = FileScanner.findFileByType(path, null, FileScanner.TYPE_YML);
        for (String ymlFilePath : fileByType) {
            path = FileScanner.getRealRoot(path);
            ymlFilePath = ymlFilePath.replace(path,"");
            YamlMapFactoryBean yamlMapFactoryBean = new YamlMapFactoryBean();
            yamlMapFactoryBean.setResources(new ClassPathResource(ymlFilePath));

            Map<String, Object> object = yamlMapFactoryBean.getObject();
            YamlConverter.doConvert(object,null,envMap);
        }
    }


    public static void setEnvMap(Map<String, Object> envMap) {
        EnvInitializer.envMap = envMap;
    }
    public static Map<String, Object> getEnvMap() {
        return envMap;
    }


}
