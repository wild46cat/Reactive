package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

public class TestAboutInterval {

    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));
    private static List<String> list = Arrays.asList("aaaa", "bb", "cccc", "d");

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * 内部带定时
     *
     * @throws InterruptedException
     */
    @Test
    public void testInterval() throws InterruptedException {
        Flux.interval(Duration.ofMillis(500))
                .subscribe(x -> {
                    System.out.println(x);
                });

        holdTheWorld();
    }

    @Test
    public void testInterval2() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(2), Duration.ofMillis(500))
                .subscribe(x -> {
                    System.out.println(x);
                });

        holdTheWorld();
    }
}
