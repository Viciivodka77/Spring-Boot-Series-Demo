package com.sou7h.configcenter.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sou7h.configcenter.core.EnvInitializer;
import com.sou7h.configcenter.core.PropertyTrigger;
import com.sou7h.configcenter.core.YamlConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author sou7h
 * @description TODO
 * @date 2022年02月12日 10:57 上午
 */
@Controller
@ResponseBody
@RequestMapping("/config")
public class ConfigController {

    @Value("${person.name}")
    private String name;


    @Value("${person.age}")
    private String age;


    @PostMapping("/save")
    public String save(@RequestBody Map<String,Object> newValue) {
        String ymlContent =(String) newValue.get("yml");
        PropertyTrigger.change(ymlContent);
        return "success";
    }

    @GetMapping("/get")
    public String getConfig(){
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        String yamlContent = null;
        try{
            Map<String, Object> envMap = EnvInitializer.getEnvMap();
            Map<String, Object> map = YamlConverter.monoToMultiLayer(envMap, null);
            yamlContent = JSON.toJSONString(map);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(yamlContent);
        return yamlContent;
    }

    @GetMapping("/test")
    public String test(){
        return name + age;
    }

}
