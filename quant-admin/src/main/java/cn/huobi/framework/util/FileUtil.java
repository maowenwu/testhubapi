package cn.huobi.framework.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Administrator on 2018/4/23/023.
 * @author liuks
 * 文件操作
 */
public class FileUtil {
    /**
     * 删除该路径下所有文件（文件夹和文件）
     * @param folderPath
     */
    public static  void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    /**
     *打包指定目录下的文件成zip
     * @param inFile 要打包的路径
     * @param zipsFile 打包成ZIP的路径
     */
    public static void zipsFile(File inFile,File zipsFile){
        //zip打包
        FileOutputStream fos = null;
        ZipOutputStream zos=null;
        try {
            fos = new FileOutputStream(zipsFile);
            zos= new ZipOutputStream(fos);
            zipFile(inFile, zos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(zos!=null){
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //封装压缩文件的方法
    public static   void zipFile(File inputFile,ZipOutputStream zipoutputStream) {
        if(inputFile.exists()) { //判断文件是否存在
            if (inputFile.isFile()) {  //判断是否属于文件，还是文件夹
                FileInputStream fis=null;
                BufferedInputStream bis=null;
                try {
                    fis = new FileInputStream(inputFile);
                    bis = new BufferedInputStream(fis);
                    //将文件写入zip内，即将文件进行打包
                    ZipEntry ze = new ZipEntry(inputFile.getName()); //获取文件名
                    zipoutputStream.putNextEntry(ze);
                    //写入文件的方法，同上
                    byte [] b=new byte[1024];
                    long l=0;
                    while(l<inputFile.length()){
                        int j=bis.read(b,0,1024);
                        l+=j;
                        zipoutputStream.write(b,0,j);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    //关闭输入输出流
                    if(bis!=null){
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(fis!=null){
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {  //如果是文件夹，则使用穷举的方法获取文件，写入zip
                try {
                    File[] files = inputFile.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        zipFile(files[i], zipoutputStream);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
