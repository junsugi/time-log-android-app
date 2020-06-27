package com.junsugi.mobileproject;

import android.util.Log;
import android.widget.EditText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

public class FileSystem {

    private File file;
    private File[] files;

    public boolean writeToFile(String title, Vector<Object> vTime1, Vector<Object> vTime2, Vector<Object> vLogging, File filesDir) {
        try{
            this.file = new File(filesDir, title);

            FileWriter fw = new FileWriter(this.file);
            for(int i = 0 ; i < vLogging.size(); i++){
                String time1 = ((EditText)vTime1.get(i)).getText().toString();
                String time2 = ((EditText)vTime2.get(i)).getText().toString();
                String logging = ((EditText)vLogging.get(i)).getText().toString();
                //시간1 저장
                fw.write(time1);
                fw.write(10);
                //시간2 저장
                fw.write(time2);
                fw.write(10);
                //한 일 저장 후
                fw.write(logging);
                //문장 마침표
                fw.write(10);
            }
            fw.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public File[] readAllToFile(File filesDir){
        this.files = new File(filesDir.toString()).listFiles();
        Arrays.sort(this.files, Collections.reverseOrder());
        return this.files;
    }

    public File getTodayFile(File filesDir, String today) {
        this.files = new File(filesDir.toString()).listFiles();

        for(File file : this.files){
            if(file.getName().equals(today)){
                return file;
            }
        }
        return null;
    }
}
