package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class TestAboutZip {

    private static Flux<Integer> flux = Flux.range(1, 10);
    private static Flux<Integer> flux2 = Flux.range(100, 10);

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testZip() throws InterruptedException {
        Flux.zip(flux, flux2, Flux.interval(Duration.ofSeconds(1)))
                .subscribe(x -> System.out.println(x));
        holdTheWorld();
    }
}
