package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class TestAboutStartWith {
    private static Flux<Integer> flux = Flux.range(1, 10);
    private static Flux<Integer> flux2 = Flux.range(100, 10).delayElements(Duration.ofMillis(200));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testStartWith() {
        flux.startWith(100, 200).subscribe(x -> System.out.println(x));
    }

    /**
     * 把指定的publisher放在前面
     *
     * @throws InterruptedException
     */
    @Test
    public void testStartWith2() throws InterruptedException {
        flux.startWith(flux2).subscribe(x -> System.out.println(x));
        holdTheWorld();
    }
}
