package com.lc.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dicongyan on 2017/4/5.
 *
 */
public class RepaymentDateUtil {
    /**
     *  获取还款日
     * @param date 还款日期
     * @return 处理后还款日
     */
    public static Date getRepaymentDate(Date date) {
        Date repayDate ;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);

        if(currentDay<=24) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            currentDay = cal.get(Calendar.DAY_OF_MONTH);
            currentMonth = cal.get(Calendar.MONTH);
            repayDate = DateUtil.getDate(currentYear,currentMonth,currentDay);
        } else {
            repayDate = DateUtil.getDate(currentYear,currentMonth,24);
        }

        repayDate = DateUtil.parse(DateUtil.format(repayDate,DateUtil.PATTERN_CLASSICAL_SIMPLE),DateUtil.PATTERN_CLASSICAL_SIMPLE);

        return repayDate;
    }
}
