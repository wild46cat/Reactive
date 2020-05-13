package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TestAboutIgnoreElement {
    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));
    private static List<String> list = Arrays.asList("aaaa", "bb", "cccc", "d");

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testIgnoreElement() {
        flux.map(x -> {
            return x + 3;
        }).ignoreElements().subscribe(x -> {
            System.out.println(x);
        });
    }
}
