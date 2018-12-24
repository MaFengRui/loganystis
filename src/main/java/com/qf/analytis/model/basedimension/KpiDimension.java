package com.qf.analytis.model.basedimension;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-19
 * Time:下午2:54
 * Vision:1.1
 * Description:关键绩效指标(KPI：Key Performance Indicator)是
 * 通过对组织内部流程的输入端、输出端的关键参数进行设置、取样、计算、分析
 * ，衡量流程绩效的一种目标式量化管理指标，
 * 是把企业的战略目标分解为可操作的工作目标的工具，是企业绩效管理的基础。
 */
public class KpiDimension extends BaseDimension{
    private int id;
    private String kpiName;

    public KpiDimension() {
    }

    public KpiDimension(String kpiName) {
        this.kpiName = kpiName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKpiName(String kpiName) {
        this.kpiName = kpiName;
    }

    public int getId() {
        return id;
    }

    public String getKpiName() {
        return kpiName;
    }

    public KpiDimension(int id, String kpiName) {
        this.id = id;
        this.kpiName = kpiName;
    }

    @Override
    public int compareTo(BaseDimension o) {
        if (this == o) {
            return 0;
        }

        KpiDimension other = (KpiDimension) o;
        int tmp = Integer.compare(this.id, other.id);
        if (tmp != 0) {
            return tmp;
        }
        tmp = this.kpiName.compareTo(other.kpiName);
        return tmp;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.id);
        dataOutput.writeUTF(this.kpiName);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.id = dataInput.readInt();
        this.kpiName = dataInput.readUTF();
    }
}
