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
}
