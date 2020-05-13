package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestAboutError {

    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testError() {
        Flux<Object> test_error = Flux.error(new IllegalArgumentException("test error"));
        test_error.concatWith(flux);
        test_error.subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testError2() {
        Flux<Object> test_error = Flux.error(new IllegalArgumentException("test error"), true);
        test_error.subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testError3() {
        Flux<Object> test_error = Flux.error(new IllegalArgumentException("test error"), false);
        test_error.subscribe(x -> {
            System.out.println(x);
        });
    }
}
