package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.util.context.Context;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class TestAboutSubscribe {

    private static Flux<Integer> flux = Flux.range(1, 10);
    private static Flux<Integer> flux2 = Flux.range(100, 10).delayElements(Duration.ofMillis(200));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testSubscribe() {
        flux.subscribe(
                x -> System.out.println(x),
                e -> System.out.println(e),
                () -> System.out.println("complete")
        );
    }

    @Test
    public void testSubscribe2() {
        flux.subscribe(
                x -> System.out.println(x),
                e -> System.out.println(e),
                () -> System.out.println("complete")
        );
    }
}
