package com.btson.aoes.tools;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTimer {
    public static Date getCurrentTime(){
        // 存入mysql中的时间格式“yyyy/MM/dd HH:mm:ss”间
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime = formatter.parse(dateString, pos);
        return currentTime;
    }

}
