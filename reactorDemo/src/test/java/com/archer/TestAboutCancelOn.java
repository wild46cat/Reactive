package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestAboutCancelOn {
    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8))
            .delayElements(Duration.ofSeconds(1));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * 指定cancel的时候，用哪个Scheduler
     *
     * @throws InterruptedException
     */
    @Test
    public void testCancelOn() throws InterruptedException {
        flux.cancelOn(Schedulers.parallel()).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }
}
