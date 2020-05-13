package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TestAboutSample {
    private static Flux<Integer> flux = Flux.range(1, 100)
            .delayElements(Duration.ofMillis(10));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));
    private static List<String> list = Arrays.asList("aaaa", "bb", "cccc", "d");

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testSample() throws InterruptedException {
        flux.sample(Duration.ofMillis(100)).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    /**
     * 根据其他publisher进行取样
     *
     * @throws InterruptedException
     */
    @Test
    public void testSample2() throws InterruptedException {
        flux.sample(Flux.interval(Duration.ofMillis(100)))
                .subscribe(x -> {
                    System.out.println(x);
                });
        holdTheWorld();
    }

    /**
     * 取样取第一个
     *
     * @throws InterruptedException
     */
    @Test
    public void testSampleFirst() throws InterruptedException {
        flux.sampleFirst(Duration.ofMillis(100))
                .subscribe(x -> {
                    System.out.println(x);
                });
        holdTheWorld();
    }
}
