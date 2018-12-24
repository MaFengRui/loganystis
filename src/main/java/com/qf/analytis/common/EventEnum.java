package com.qf.analytis.common;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-20
 * Time:上午10:33
 * Vision:1.1
 * Description:事件的枚举
 */
public enum EventEnum {
    LANUCH(1,"lanuch event","e_l"),
    PAGEVIEW(2,"pageview event","e_pv"),
    EVENT(3,"event name","e_e"),
    CHARGEREQUEST(4,"charge request event","e_crt"),
    CHARGESUCCESS(5,"charge success","e_cs"),
    CHARGEREFUND(6,"charge refund","e_cr")
    ;

    public int id;
    public String name;
    public String alias;

    EventEnum(int id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    //根据名称获取枚举
    public static EventEnum valueOfAlias(String alia){
        for (EventEnum event : values()){
            if(event.alias.equals(alia)){
                return event;
            }
        }
        throw new RuntimeException("该alias没有对应的枚举.alias:"+alia);
    }


}
