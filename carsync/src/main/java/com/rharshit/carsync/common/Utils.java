package com.rharshit.carsync.common;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

@Slf4j
public class Utils {
    public static void awaitShutdownExecutorService(ExecutorService executor) {
        executor.shutdown();
        try {
            while (!executor.awaitTermination(1000, java.util.concurrent.TimeUnit.NANOSECONDS)) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.error("Error waiting for executor to finish", e);
        }
    }

    //TODO: Fix for names with spaces
    public static String listToRegexMongoQueryParam(String[] values) {
        if (values == null || values.length == 0) {
            return "/*";
        }
        StringBuilder query = new StringBuilder();
        query.append("^(");
        for (String value : values) {
            query.append(value).append("|");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(")$");
        return query.toString();
    }
}
