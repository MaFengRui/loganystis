package com.qf.analytis.transfrom.service;

import com.qf.analytis.model.basedimension.BaseDimension;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-20
 * Time:下午5:05
 * Vision:1.1
 * Description:根据维度获取对应字段的ip
 */
public interface IDimension {

    int getDimensionByObject(BaseDimension baseDimension);

}
