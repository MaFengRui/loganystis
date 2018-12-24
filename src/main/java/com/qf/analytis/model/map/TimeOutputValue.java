package com.qf.analytis.model.map;

import com.qf.analytis.common.KpiType;
import com.qf.analytis.model.kpi.OutputValueBaseWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-19
 * Time:下午10:34
 * Vision:1.1
 * Description:用户模块和浏览器模块在map的value的输出
 */
public class TimeOutputValue extends OutputValueBaseWritable {

    private String id;//泛指uuid,sesionid,memberid
    private  long time; //时间戳

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.id);
        out.writeLong(this.time);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readUTF();
        this.time = in.readLong();
    }

    @Override
    public KpiType getKpi() {
        return null;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
