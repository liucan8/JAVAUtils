package com.lc.utils;

/**
 * Created by feihaitao on 2017/3/3.
 */
public enum LOANPERIODUtil {

    LOANPERIOD_SAN("LOANPERIOD/SAN", 3),LOANPERIOD_SHIER("LOANPERIOD/SHIER", 12),
    LOANPERIOD_LIU("LOANPERIOD/LIU", 6), LOANPERIOD_JIU("LOANPERIOD/ERSHISI", 24);

    // 成员变量
    private String name;
    private int index;

    // 构造方法，注意：构造方法不能为public，因为enum并不可以被实例化
    private LOANPERIODUtil(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (LOANPERIODUtil c : LOANPERIODUtil .values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    public static int  getCode(String name) {
        for (LOANPERIODUtil c : LOANPERIODUtil .values()) {
            if (c.getName().equals(name) ) {
                return c.index;
            }
        }
        return 0;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    public static void main(String[] args) {
        System.out.print(LOANPERIODUtil.getCode("LOANPERIOD/SHIER"));
    }
}
