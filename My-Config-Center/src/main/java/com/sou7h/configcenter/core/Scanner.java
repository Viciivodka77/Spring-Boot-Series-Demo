package com.sou7h.configcenter.core;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author sou7h
 * @description 启动时初始化扫描
 * @date 2022年02月12日 1:56 下午
 */
@Component
public class Scanner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("aaaaaaaaa");
        doScan();
    }

    private void doScan(){
        //获取根目录
        String path = this.getClass().getResource("/").getPath();
        List<String> fileByType = FileScanner.findFileByType(path, null, FileScanner.TYPE_CLASS);
        doFilter(path,fileByType);
        EnvInitializer.init();
    }

    private void doFilter(String rootPath, List<String> fileList){
        String realRootPath = FileScanner.getRealRoot(rootPath);

        for (String fullPath : fileList) {
            String shortPath = fullPath.replace(realRootPath, "").replace(FileScanner.TYPE_CLASS, "");
            String packageFileName=shortPath.replaceAll(Matcher.quoteReplacement(File.separator),"\\.");

            try {
                Class<?> clazz = Class.forName(packageFileName);
                if (clazz.isAnnotationPresent(Component.class) ||
                        clazz.isAnnotationPresent(Controller.class) ||
                            clazz.isAnnotationPresent(Service.class)){
                    //加入变量池中判断是否有相应的@Value属性
                    VariablePool.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }


    }


}
