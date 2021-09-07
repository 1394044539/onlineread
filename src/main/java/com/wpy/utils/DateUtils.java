package com.wpy.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
public class DateUtils {

    private static final String YMD_DATE_ONE="yyyy-MM-dd";
    private static final String YMDHMS_DATE_ONE="yyyy-MM-dd HH:mm:ss";

    private static final String YMD_DATE_TWO="yyyy/MM/dd";
    private static final String YMDHMS_DATE_TWO="yyyy/MM/dd HH:mm:ss";

    public static String strFormaterDate1(Date date){
        if(date == null){
            return "";
        }
        return DateFormatUtils.format(date,YMD_DATE_ONE);
    }

    public static String strFormaterDate2(Date date){
        if(date == null){
            return "";
        }
        return DateFormatUtils.format(date,YMDHMS_DATE_ONE);
    }

    public static String strFormaterDate3(Date date){
        if(date == null){
            return "";
        }
        return DateFormatUtils.format(date,YMD_DATE_TWO);
    }

    public static String strFormaterDate4(Date date){
        if(date == null){
            return "";
        }
        return DateFormatUtils.format(date,YMDHMS_DATE_TWO);
    }

    public static String strFormaterDateCustom(Date date,String pattern){
        if(date == null){
            return "";
        }
        return DateFormatUtils.format(date,pattern);
    }

    public static Date dateFormaterStr1(String str){
        SimpleDateFormat sdf=new SimpleDateFormat(YMD_DATE_ONE);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static Date dateFormaterStr2(String str){
        SimpleDateFormat sdf=new SimpleDateFormat(YMDHMS_DATE_ONE);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static Date dateFormaterStr3(String str){
        SimpleDateFormat sdf=new SimpleDateFormat(YMD_DATE_TWO);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static Date dateFormaterStr4(String str){
        SimpleDateFormat sdf=new SimpleDateFormat(YMDHMS_DATE_TWO);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static Date dateFormaterStrCustom(String str,String pattern){
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    public static int getYear(){
        return DateTime.now().getYear();
    }

    public static int getMoth(){
        return DateTime.now().getMonthOfYear();
    }

    public static int getDay(){
        return DateTime.now().getDayOfYear();
    }

    public static Date getNowDate(){
        return DateTime.now().toDate();
    }

    public static Date getPlusDateTime(int d){
        return DateTime.now().plusDays(d).toDate();
    }

    public static Date getYesterDateTime(int d){
        return DateTime.now().minusDays(d).toDate();
    }

    public static Date getDateBefore(Date date,int day){
        Calendar now=Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
        return now.getTime();
    }

    public static Date getDateAfter(Date date,int day){
        Calendar now=Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        return now.getTime();
    }

    public static Date getAllFormatDate(String value) {
        Date date=DateUtils.dateFormaterStr1(value);
        if(date==null){
            date=DateUtils.dateFormaterStr2(value);
        }
        if(date==null){
            date=DateUtils.dateFormaterStr3(value);
        }
        if(date==null){
            date=DateUtils.dateFormaterStr4(value);
        }
        return date;
    }

    /**
     * 增加一天
     * @param date
     * @return
     */
    public static Date addDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }
}
