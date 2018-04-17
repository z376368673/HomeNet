package com.benkie.hjw.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**  
 *   
 * @author Andy.Chen  
 * @mail Chenjunjun.ZJ@gmail.com  
 *  
 */  
public class InputStreamUtils {  
      
    final static int BUFFER_SIZE = 4096;  
      
    /**  
     * ��InputStreamת����String  
     * @param in InputStream  
     * @return String  
     * @throws Exception  
     *   
     */  
    public static String InputStreamTOString(InputStream in) throws Exception{  
          
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[BUFFER_SIZE];  
        int count = -1;  
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)  
            outStream.write(data, 0, count);  
          
        data = null;  
        return new String(outStream.toByteArray(),"ISO-8859-1");  
    }  
      
    /**  
     * ��InputStreamת����ĳ���ַ������String  
     * @param in  
     * @param encoding  
     * @return  
     * @throws Exception  
     */  
         public static String InputStreamTOString(InputStream in,String encoding) throws Exception{  
          
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[BUFFER_SIZE];  
        int count = -1;  
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)  
            outStream.write(data, 0, count);  
          
        data = null;  
        return new String(outStream.toByteArray(),"ISO-8859-1");  
    }  
      
    /**  
     * ��Stringת����InputStream  
     * @param in  
     * @return  
     * @throws Exception  
     */  
    public static InputStream StringTOInputStream(String in) throws Exception{  
          
        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));  
        return is;  
    }  
      
    /**  
     * ��InputStreamת����byte����  
     * @param in InputStream  
     * @return byte[]  
     * @throws IOException  
     */  
    public static byte[] InputStreamTOByte(InputStream in) throws IOException{  
          
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] data = new byte[BUFFER_SIZE];  
        int count = -1;  
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)  
            outStream.write(data, 0, count);  
          
        data = null;  
        return outStream.toByteArray();  
    }  
      
    /**  
     * ��byte����ת����InputStream  
     * @param in  
     * @return  
     * @throws Exception  
     */  
    public static InputStream byteTOInputStream(byte[] in) throws Exception{  
          
        ByteArrayInputStream is = new ByteArrayInputStream(in);  
        return is;  
    }  
      
    /**  
     * ��byte����ת����String  
     * @param in  
     * @return  
     * @throws Exception  
     */  
    public static String byteTOString(byte[] in) throws Exception{  
          
        InputStream is = byteTOInputStream(in);  
        return InputStreamTOString(is);  
    }  
  
} 