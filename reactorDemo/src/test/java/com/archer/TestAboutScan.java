package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TestAboutScan {

    private static Flux<Integer> flux = Flux.range(1, 10);
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));
    private static List<String> list = Arrays.asList("aaaa", "bb", "cccc", "d");

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testScan() {
        flux.scan(0, (x, y) -> {
            System.out.println(x + " " + y);
            return x + y;
        }).collectList().subscribe(x -> {
            System.out.println();
        });
    }

    /**
     * 和scan的区别在于，scanwith能够把结果值返回到flux中
     */
    @Test
    public void testScanWith() {
        flux.scanWith(() -> {
            return 0;
        }, (x, y) -> {
            return x + y;
        }).subscribe(x -> {
            System.out.println(x);
        });
    }
}

