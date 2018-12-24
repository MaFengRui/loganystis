package com.qf.analytis.model.dimension;

import com.qf.analytis.model.basedimension.BaseDimension;
import com.qf.analytis.model.basedimension.DateDimension;
import com.qf.analytis.model.basedimension.KpiDimension;
import com.qf.analytis.model.basedimension.PlatformDimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-19
 * Time:下午10:02
 * Vision:1.1
 * Description:公用dimension信息组合
 */
public class StatsCommonDimension extends StatsDimension {
    private DateDimension date = new DateDimension(); //时间维度
    private PlatformDimension platform = new PlatformDimension(); //平台维度
    private KpiDimension kpi = new KpiDimension(); //我们要分析的维度

    public StatsCommonDimension() {
        super();
    }

    public StatsCommonDimension(DateDimension date, PlatformDimension platform, KpiDimension kpi) {
        super();
        this.date =date;
        this.platform = platform;
        this.kpi = kpi;
    }

    /**
     * 复制一个StatsCommonDimension维度
     * @param dimension
     * @return
     */
    public static StatsCommonDimension clone (StatsCommonDimension dimension){
        DateDimension date = new DateDimension(dimension.date.getId(), dimension.date.getYear(), dimension.date.getSeason(), dimension.date.getMonth(), dimension.date.getWeek(), dimension.date.getDay(), dimension.date.getType(), dimension.date.getCalendar());
        PlatformDimension platform = new PlatformDimension(dimension.platform.getId(), dimension.platform.getPlatformName());
        KpiDimension kpi = new KpiDimension(dimension.kpi.getId(), dimension.kpi.getKpiName());
        return new StatsCommonDimension(date, platform, kpi);
    }
    @Override
    public int compareTo(BaseDimension o) {
        if (this == o){
            return 0;
        }
        StatsCommonDimension other = (StatsCommonDimension) o;
        int tmp = this.date.compareTo(other.date);
        if (tmp != 0){
            return  tmp;
        }
        tmp = this.platform.compareTo(other.platform);
        if (tmp != 0) {
            return tmp;
        }
        tmp = this.kpi.compareTo(other.kpi);
        return tmp;
    }

    public void setDate(DateDimension date) {
        this.date = date;
    }

    public void setPlatform(PlatformDimension platform) {
        this.platform = platform;
    }

    public void setKpi(KpiDimension kpi) {
        this.kpi = kpi;
    }

    public DateDimension getDate() {
        return date;
    }

    public PlatformDimension getPlatform() {
        return platform;
    }

    public KpiDimension getKpi() {
        return kpi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatsCommonDimension that = (StatsCommonDimension) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(platform, that.platform) &&
                Objects.equals(kpi, that.kpi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, platform, kpi);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        this.date.write(dataOutput);
        this.platform.write(dataOutput);
        this.kpi.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.date.readFields(dataInput);
        this.platform.readFields(dataInput);
        this.kpi.readFields(dataInput);
    }

    @Override
    public String toString() {
        return "StatsCommonDimension{" +
                "date=" + date +
                ", platform=" + platform +
                ", kpi=" + kpi +
                '}';
    }
}
