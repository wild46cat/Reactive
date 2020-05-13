package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TestAboutMerge {
    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 8, 3))
            .delayElements(Duration.ofMillis(500));
    private static Flux<Integer> flux3 = Flux.fromIterable(Arrays.asList(7, 4, 2))
            .delayElements(Duration.ofMillis(500));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"))
            .delayElements(Duration.ofMillis(600));
    private static List<String> list = Arrays.asList("aaaa", "bb", "cccc", "d");

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * 和concat的区别是concat是整个publisher纬度的,merge是出现后就会进行
     *
     * @throws InterruptedException
     */
    @Test
    public void testMerge() throws InterruptedException {
        Flux.merge(flux, flux2).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    @Test
    public void testMergeSequential() throws InterruptedException {
        Flux.mergeSequential(flux, flux3).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

}
