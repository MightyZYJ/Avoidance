package com.juno.avoidance.utils;

import timber.log.Timber;

/**
 * Created by Juno.
 * Date : 2019/4/19.
 * Time : 20:13.
 * When I met you in the summer.
 * Description : 性能监控，调用begin和end，打印使用时间
 */
@SuppressWarnings("unused")
public class MethodPerformanceUtil {

    private static final String METHOD_PERFORMANCE = "MethodPerformanceUtil";

    private long begin;
    private String serviceMethod;

    private static final ThreadLocal<MethodPerformanceUtil> performanceRecord = new ThreadLocal<>();

    public static void begin(String method) {
        Timber.e("begin monitor");
        MethodPerformanceUtil methodPerformance = new MethodPerformanceUtil(method);
        performanceRecord.set(methodPerformance);
    }

    public static void end() {
        Timber.e("end monitor");
        MethodPerformanceUtil methodPerformance = performanceRecord.get();
        if (methodPerformance != null) {
            methodPerformance.printPerformance();
        }
    }

    private MethodPerformanceUtil(String serviceMethod) {
        this.serviceMethod = serviceMethod;
        begin = System.currentTimeMillis();
    }

    private void printPerformance() {
        long end = System.currentTimeMillis();
        Timber.e(serviceMethod + " cost " + (end - begin) + " millis.");
    }

}
