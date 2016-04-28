package com.techengage.components.camel.camera.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CommonUtils 
{
    public static String generateStillImageFileNamePath(String imageDirectory,String imageFormat)
    {
        String fileName=null;
        
        UUID uuid = UUID.randomUUID();
        
        Date date = new Date();
        String ISO_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
        
        fileName=imageDirectory+File.separator+uuid.toString()+"-"+sdf.format(date)+"."+imageFormat;
        
        return fileName;
    }
    
    public static String generateStillImageFileNamePath(String imageDirectory)
    {
        String fileName=null;
        
        UUID uuid = UUID.randomUUID();
        
        Date date = new Date();
        String ISO_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
        SimpleDateFormat sdf = new SimpleDateFormat(ISO_FORMAT);
        
        fileName=imageDirectory+File.separator+uuid.toString()+"-"+sdf.format(date)+".ts";
        
        return fileName;
    }
    
    public static boolean isBlank(String source)
    {
        return (source==null || source.isEmpty() || source.trim().equals(""));
    }
}
