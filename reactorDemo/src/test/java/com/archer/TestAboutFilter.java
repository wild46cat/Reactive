package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestAboutFilter {

    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testFilter() {
        flux.filter(x -> {
            return x > 4;
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * 判断条件是异步的
     */
    @Test
    public void testFilterWhen() {
        flux.filterWhen(x -> {
            return Flux.just(x > 3);
        }).subscribe(x -> {
            System.out.println(x);
        });
    }
}
