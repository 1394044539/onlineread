package com.wpy.utils;

import com.google.common.collect.Lists;
import com.wpy.pojo.FileInfoPojo;
import lombok.extern.slf4j.Slf4j;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;
import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 13940
 * @date 2021/3/2
 */
@Slf4j
public class FileUtils {

    /**
     * 获取上传文件的md5
     * @param file
     * @return
     * @throws
     */
    public static String getMd5(MultipartFile file) {
        try {
            //获取文件的byte信息
            byte[] uploadBytes = file.getBytes();
            // 拿到一个MD5转换器
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            //转换为16进制
            return new BigInteger(1, digest).toString(16);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据文件路径获取文件总行数
     * @param filePath
     * @return
     */
    public static Long getTotalLineByPath(String filePath){
        try(Stream<String> lines= Files.lines(Paths.get(filePath))){
            return lines.count();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return 0L;
    }

    /**
     * 根据文件路径获取文件数据集合
     * @param filePath
     * @return
     */
    public static List<String> getAllList(String filePath){
        try(Stream<String> lines= Files.lines(Paths.get(filePath))){
            return lines.collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * 读取文件制定行数
     * @param filePath
     * @param before 从哪一行开始
     * @param limit 跳过的行数
     * @return
     */
    public static List<String> getLineByLimit(String filePath,long before,long limit){
        try(Stream<String> lines= Files.lines(Paths.get(filePath))){
            return lines.skip(before).limit(limit).collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * 读取文件制定行数
     * @param filePath
     * @param start 从哪一行开始
     * @param end 到哪一行结束
     * @return
     */
    public static List<String> getLineByEnd(String filePath,int start,int end){
        try(Stream<String> lines= Files.lines(Paths.get(filePath))){
            return lines.skip(start).limit(end-start).collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * 根据文件路径获取文件总行数
     * @param inputStream
     * @return
     */
    public static Long getTotalLineByPath(InputStream inputStream){
        try(Stream<String> lines= IOUtils.readLines(inputStream, StandardCharsets.UTF_8).stream()){
            return lines.count();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return 0L;
    }

    /**
     * 根据文件路径获取文件数据集合
     * @param inputStream
     * @return
     */
    public static List<String> getAllList(InputStream inputStream){
        try(Stream<String> lines= IOUtils.readLines(inputStream, StandardCharsets.UTF_8).stream()){
            return lines.collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * 读取文件制定行数
     * @param inputStream
     * @param before 从哪一行开始
     * @param limit 跳过的行数
     * @return
     */
    public static List<String> getLineByLimit(InputStream inputStream,Long before,Long limit){
        try(Stream<String> lines= IOUtils.readLines(inputStream, StandardCharsets.UTF_8).stream()){
            return lines.skip(before).limit(limit).collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * 读取文件制定行数
     * @param inputStream
     * @param start 从哪一行开始
     * @param end 到哪一行结束
     * @return
     */
    public static List<String> getLineByEnd(InputStream inputStream,int start,int end){
        try(Stream<String> lines= IOUtils.readLines(inputStream, StandardCharsets.UTF_8).stream()){
            return lines.skip(start).limit(end-start).collect(Collectors.toList());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * 获取文件的大部分属性
     * @param filePath
     * @return
     */
    public static FileInfoPojo getFileInfoByParam(String filePath,Integer start,Integer end){
        FileInfoPojo fileInfoPojo=new FileInfoPojo();
        fileInfoPojo.setTotal(getTotalLineByPath(filePath));
        fileInfoPojo.setAllList(getAllList(filePath));
        fileInfoPojo.setAppointList(Lists.newArrayList());
        if(StringUtils.isNotBlank(start)&&StringUtils.isNotBlank(end)){
            fileInfoPojo.setAppointList(getLineByEnd(filePath,start,end));
        }
        fileInfoPojo.setWordNum(StringUtils.getListLength(fileInfoPojo.getAllList()));
        return fileInfoPojo;
    }

    /**
     * 获取文件的大部分属性
     * @param inputStream
     * @return
     */
    public static FileInfoPojo getFileInfoByInput(InputStream inputStream,Integer start,Integer end){
        FileInfoPojo fileInfoPojo=new FileInfoPojo();
        byte[] fileBytes = saveaIns(inputStream);
        fileInfoPojo.setTotal(getTotalLineByPath(new ByteArrayInputStream(fileBytes)));
        fileInfoPojo.setAllList(getAllList(new ByteArrayInputStream(fileBytes)));
        fileInfoPojo.setAppointList(Lists.newArrayList());
        if(StringUtils.isNotBlank(start)&&StringUtils.isNotBlank(end)){
            fileInfoPojo.setAppointList(getLineByEnd(new ByteArrayInputStream(fileBytes),start,end));
        }
        fileInfoPojo.setWordNum(StringUtils.getListLength(fileInfoPojo.getAllList()));
        return fileInfoPojo;
    }

    /**
     * 吧输入流转为byte
     * @author pywang6
     * @date 2021/3/11 10:24
     *
     * @param ins
     * @return byte[]
     */
    public static byte[] saveaIns(InputStream ins){
        byte[] buf = null;
        try {
            if(ins!=null){
                buf = IOUtils.toByteArray(ins);//ins为InputStream流
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return buf;
    }

    /**
     *通过输入流获得epub文件信息
     * @author pywang6
     * @date 2021/3/11 10:27
     *
     * @param inputStream
     * @return nl.siegmann.epublib.domain.Book
     */
    public static Book getEpubBookByIn(InputStream inputStream){
        Book book=null;
        EpubReader reader = new EpubReader();
        try {
            book= reader.readEpub(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return book;
    }

    /**
     *通过路径获得epub文件信息
     * @author pywang6
     * @date 2021/3/11 10:27
     *
     * @param path
     * @return nl.siegmann.epublib.domain.Book
     */
    public static Book getEpubBookByPath(String path){
        Book book=null;
        File file = new File(path);
        try (InputStream in = new FileInputStream(file)){
            EpubReader reader = new EpubReader();
            book= reader.readEpub(in);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return book;
    }

    /**
     *保存btye格式文件
     * @author pywang6
     * @date 2021/3/11 15:47
     *
     * @param buf
     * @param filePath 不带文件名
     * @param fileName
     * @return java.io.File
     */
    public static File byteToFile(byte[] buf, String filePath, String fileName){
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try{
            File dir = new File(filePath);
            if (!dir.exists()){
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            if (bos != null){
                try{
                    bos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if (fos != null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    public static String getFileCode(InputStream inputStream) {
        //或GBK
        String code = "gb2312";
        try {
            byte[] head = new byte[3];
            inputStream.read(head);

            if (head[0] == -1 && head[1] == -2 ) {
                code = "UTF-16";
            }else if (head[0] == -2 && head[1] == -1 ) {
                code = "Unicode";
            }else if(head[0]==-17 && head[1]==-69 && head[2] ==-65) {
                code = "UTF-8";
            } else if(head[0]==13 && head[1]==10 && head[2] ==45){
                code = "";
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return code;
    }
}
