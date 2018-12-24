package com.qf.analytis.model.basedimension;

import com.qf.analytis.common.DateEnum;
import com.qf.analytis.etl.util.TimeUtil;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-19
 * Time:下午8:39
 * Vision:1.1
 * Description:时间维度类
 */
public class DateDimension extends BaseDimension{
    private  int id;//id,eg:1
    private int year;//年份　eg 2015
    private int season;//季度：eg:4
    private int month; //月份　eg :12
    private int week; //周
    private int day;
    private String type; //类型
    private Date calendar = new Date();
   //int year, int season, int month, int week, int day, String type
   public DateDimension() {
       super();
   }

    public DateDimension(int year, int season, int month, int week, int day, String type) {
        super();
        this.year = year;
        this.season = season;
        this.month = month;
        this.week = week;
        this.day = day;
        this.type = type;
    }

    public DateDimension(int year, int season, int month, int week, int day, String type, Date calendar) {
        this(year, season, month, week, day, type);
        this.calendar = calendar;
    }

    public DateDimension(int id, int year, int season, int month, int week, int day, String type, Date calendar) {
        this(year, season, month, week, day, type, calendar);
        this.id = id;
    }


    /**
     * 根据type类型获取对应的时间维度对象
     * @param
     * @return
     */
    public  static DateDimension buildDate(long time, DateEnum type){
        //根据时间戳获得年份
        int year = TimeUtil.getDateInfo(time, DateEnum.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        if (DateEnum.YEAR.equals(type)){
            //从第一个月第一天开始
            calendar.set(year,0,1);
            //int year, int season, int month, int week, int day, String type
            return  new DateDimension(year,0,0,0,0,type.dateType,calendar.getTime());
        }
        //按照季度的维度
        int season = TimeUtil.getDateInfo(time,DateEnum.SEASON);
        if (DateEnum.SEASON.equals(type)){
            int month = (3*season - 2);
            //从第一个季度的第一个月第一天开始
            calendar.set(year,month-1,1);
            return new DateDimension(year,season,0,0,0,type.dateType,calendar.getTime());
        }
        //按照月份
        int month = TimeUtil.getDateInfo(time, DateEnum.MONTH);
        if (DateEnum.MONTH.equals(type)) {
            calendar.set(year, month - 1, 1);
            return new DateDimension(year, season, month, 0, 0, type.dateType, calendar.getTime());
        }
        //按照周
        int week = TimeUtil.getDateInfo(time, DateEnum.WEEK);
        if (DateEnum.WEEK.equals(type)) {
            long firstDayOfWeek = TimeUtil.getFirstDayOfWeek(time); // 获取指定时间戳所属周的第一天时间戳
            year = TimeUtil.getDateInfo(firstDayOfWeek, DateEnum.YEAR);
            season = TimeUtil.getDateInfo(firstDayOfWeek, DateEnum.SEASON);
            month = TimeUtil.getDateInfo(firstDayOfWeek, DateEnum.MONTH);
            week = TimeUtil.getDateInfo(firstDayOfWeek, DateEnum.WEEK);
            if (month == 12 && week == 1) {
                week = 53;
            }
            return new DateDimension(year, season, month, week, 0, type.dateType, new Date(firstDayOfWeek));
        }
        int day = TimeUtil.getDateInfo(time, DateEnum.DAY);
        if (DateEnum.DAY.equals(type)) {
            calendar.set(year, month - 1, day);
            if (month == 12 && week == 1) {
                week = 53;
            }
            return new DateDimension(year, season, month, week, day, type.dateType, calendar.getTime());
        }
        throw new RuntimeException("不支持所要求的dateEnum类型来获取datedimension对象" + type);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCalendar(Date calendar) {
        this.calendar = calendar;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public int getSeason() {
        return season;
    }

    public int getMonth() {
        return month;
    }

    public int getWeek() {
        return week;
    }

    public int getDay() {
        return day;
    }

    public String getType() {
        return type;
    }

    public Date getCalendar() {
        return calendar;
    }

    @Override
    public int compareTo(BaseDimension o) {
        if (this == o) {
            return 0;
        }

        DateDimension other = (DateDimension) o;
        int tmp = Integer.compare(this.id, other.id);
        if (tmp != 0) {
            return tmp;
        }

        tmp = Integer.compare(this.year, other.year);
        if (tmp != 0) {
            return tmp;
        }

        tmp = Integer.compare(this.season, other.season);
        if (tmp != 0) {
            return tmp;
        }

        tmp = Integer.compare(this.month, other.month);
        if (tmp != 0) {
            return tmp;
        }

        tmp = Integer.compare(this.week, other.week);
        if (tmp != 0) {
            return tmp;
        }

        tmp = Integer.compare(this.day, other.day);
        if (tmp != 0) {
            return tmp;
        }

        tmp = this.type.compareTo(other.type);
        return tmp;

    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.id);
        out.writeInt(this.year);
        out.writeInt(this.season);
        out.writeInt(this.month);
        out.writeInt(this.week);
        out.writeInt(this.day);
        out.writeUTF(this.type);
        out.writeLong(this.calendar.getTime());

    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.year = in.readInt();
        this.season = in.readInt();
        this.month = in.readInt();
        this.week = in.readInt();
        this.day = in.readInt();
        this.type = in.readUTF();
        this.calendar.setTime(in.readLong());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateDimension that = (DateDimension) o;
        return  id == that.id &&
                year == that.year &&
                season == that.season &&
                month == that.month &&
                week == that.week &&
                day == that.day &&
                Objects.equals(type, that.type) &&
                Objects.equals(calendar, that.calendar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, year, season, month, week, day, type, calendar);
    }
}
