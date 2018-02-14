/*
* * Copyright (C) 2018 GT Silicon Pvt Ltd
 *
 * Licensed under the Creative Commons Attribution 4.0
 * International Public License (the "CCBY4.0 License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://creativecommons.org/licenses/by/4.0/legalcode
 *
 *
* */

package com.inertialelements.opendarex;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Utilities {
    private static final String LOG_DIR = "Log Files";
    private static String LOG_FILE_NAME = null;

    /**
     * Create log folder and file and write header to file
     *
     * @param context  application context
     *
     */
    public static void writeHeaderToLog(Context context){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.US);
        Date now = new Date();
        LOG_FILE_NAME = "LOG_FILE " + formatter.format(now) + ".txt";//like LOG_FILE_20181502_130316.txt
        try {
            String path = context.getPackageName() + File.separator + LOG_DIR;
            File root = createDirIfNotExists(path);
            if(root==null){
                LOG_FILE_NAME = null;
                return;
            }else {
                // code to make the files visible on the PC using MTP protocol
                if(root.setExecutable(true)&&root.setReadable(true)&&root.setWritable(true)){
                    Log.i("SET FILE PERMISSION ", "Set read , write and execuatable permission for log file");
                }

                MediaScannerConnection.scanFile(context, new String[]{root.toString()}, null, null);
            }
            File gpxFile = new File(root, LOG_FILE_NAME);

            FileWriter writer = new FileWriter(gpxFile, true);
            //writer.append(" LOG FILE  "+"\n\n");
            String header=String.format("%11s\t%9s\t%6s\t%6s\t%6s\t%6s\n\n",
                    "TIME STAMP","STEP COUNT","X","Y","Z","Distance");
            writer.append(header);
            writer.flush();
            writer.close();
            //Toast.makeText(get, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("WRITE LOG ERROR ", e.getMessage());
        }
    }

    /**
     * To get external file path
     *
     * @param relativePath path relative external sdcard path e.g.
     *
     * @return File external file where log file will be created
     */
    public static File createDirIfNotExists(String relativePath) {
        String testFilePath=null;
        File filePath = null;
        List<String> possibleExternalFilePath = Arrays.asList("external_sd", "ext_sd", "external", "extSdCard", "sdcard2");

        try{
            for (String sdPath : possibleExternalFilePath) {
                File file = new File("/mnt/", sdPath);

                if (file.isDirectory() && file.canWrite()) {
                    testFilePath = file.getAbsolutePath();

                    String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.US).format(new Date());
                    File checkWritable = new File(testFilePath, "test_" + timeStamp);

                    if (checkWritable.mkdirs()) {
                        if(checkWritable.delete()) {
                            Log.e("FILE ERROR ", "Problem deleting test file");
                        }
                    }
                    else {
                        testFilePath = null;
                    }
                }
            }

            if (testFilePath != null) {
                filePath = new File(testFilePath,relativePath);
            }
            else {
                filePath = new File(Environment.getExternalStorageDirectory(),relativePath);
            }
        }catch(Exception ex){
            filePath = null;
        }


        if (filePath!=null && !filePath.exists()) {
            if (!filePath.mkdirs()) {
                Log.e("FILE ERROR ", "Problem creating app folder");
                return null;
            }
            else {
                Log.i("FILE HANDLING", testFilePath + " Created successfully");
            }
        }
        return filePath;
    }

    /**
     * Write step details to log file if exists
     *
     * @param context  application context
     * @param stepData  step details like stepcounter, X, Y, Z and distance covered
     *
     */
    public static void writeDataToLog(Context context,StepData stepData){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        Date now = new Date();
        DecimalFormat df3 = new DecimalFormat("0.00");

        try
        {
            if(LOG_FILE_NAME!=null && LOG_FILE_NAME.length() > 0){
                String path = File.separator + context.getPackageName() + File.separator  + LOG_DIR + File.separator ;
                File root = createDirIfNotExists(path);
                if (root==null)
                {
                    return;
                }
                File gpxFile = new File(root, LOG_FILE_NAME);


                FileWriter writer = new FileWriter(gpxFile,true);
                //writer.append(" LOG FILE  "+"\n\n");"
                String data=String.format("%11s\t%9s\t%6s\t%6s\t%6s\t%6s\n", formatter.format(now),stepData.getStepCounter(),
                        df3.format(stepData.getX()),df3.format(stepData.getY()),df3.format(stepData.getZ()),
                        df3.format(stepData.getDistance()));
                writer.append(data);
                writer.flush();
                writer.close();
                //Toast.makeText(get, "Data has been written to Report File", Toast.LENGTH_SHORT).show();
            }
        }
        catch(IOException e)
        {
            Log.e("FILE ERROR","Problem File Writing ");
        }
    }


}
