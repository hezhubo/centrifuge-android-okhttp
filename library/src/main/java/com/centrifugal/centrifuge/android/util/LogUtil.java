package com.centrifugal.centrifuge.android.util;

import android.util.Log;

/**
 * Project Name: centrifuge-android-okhttp
 * File Name:    LogUtil.java
 * ClassName:    LogUtil
 * <p>
 * Description: 日志工具类.
 *
 * @author hezhubo
 * @date 2017年12月08日 18:38
 */
public class LogUtil {

    private static volatile boolean writeLogs = true;

    public static void writeLogs(boolean writeLogs) {
        LogUtil.writeLogs = writeLogs;
    }

    public static void v(String tag, String msg) {
        if (writeLogs)
            Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (writeLogs)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (writeLogs)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (writeLogs)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (writeLogs)
            Log.e(tag, msg);
    }
}
