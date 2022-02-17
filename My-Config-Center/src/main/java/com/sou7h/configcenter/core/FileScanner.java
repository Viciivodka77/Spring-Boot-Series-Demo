package com.sou7h.configcenter.core;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * @author sou7h
 * @description 文件扫描器
 * @date 2022年02月12日 2:00 下午
 */
public class FileScanner {

    public static final String TYPE_CLASS = ".class";
    public static final String TYPE_YML = ".yml";

    /**
     * @description TODO
     * @param rootPath:
     * @param fileList:
     * @param fileType:
     * @return java.util.List<java.lang.String>
     */
    public static List<String> findFileByType(String rootPath,List<String> fileList,String fileType){
        if (fileList == null){
            fileList = new ArrayList<>();
        }

        File rootFile=new File(rootPath);
        if (!rootFile.isDirectory()){
            //如果是文件
            addFile(rootFile.getPath(),fileList,fileType);
        }else {
            //如果是目录
            String[] subFileList = rootFile.list();
            for (String file : subFileList) {
                String subFilePath = rootPath + "/" + file;
                File subFile = new File(subFilePath);
                if (!subFile.isDirectory()){
                    addFile(subFile.getPath(),fileList,fileType);
                }else{
                    findFileByType(subFilePath,fileList,fileType);
                }
            }


        }

        return fileList;
    }

    /**
     * @description 如果文件符合类型，放入list列表中
     * @param fileName: 文件名
     * @param fileList: 文件列表
     * @param fileType: 指定的文件类型
     */
    private static void addFile(String fileName,List<String> fileList,String fileType){
        if (fileName.endsWith(fileType)){
            fileList.add(fileName);
        }
    }

    public static String getRealRoot(String rootPath){
        if (System.getProperty("os.name").startsWith("Windows")
                && rootPath.startsWith("/")){
            rootPath = rootPath.substring(1);
            rootPath = rootPath.replaceAll("/", Matcher.quoteReplacement(File.separator));
        }
        return rootPath;
    }

}
