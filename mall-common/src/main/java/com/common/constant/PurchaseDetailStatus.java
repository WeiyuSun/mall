package com.common.constant;

public class PurchaseDetailStatus {
    public static int CREATED = 0;
    public static int ASSIGNED = 1;
    public static int BUYING = 2;
    public static int FINISH = 3;
    public static int ERROR = 4;

    public static String CREATED_MSG = "新建";
    public static String ASSIGNED_MSG = "已分配";
    public static String BUYING_MSG = "正在采购";
    public static String FINISH_MSG = "已完成";
    public static String ERROR_MSG = "采购失败";
}
