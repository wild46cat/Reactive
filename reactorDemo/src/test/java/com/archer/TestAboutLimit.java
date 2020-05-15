package com.archer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.logging.Level;

public class TestAboutLimit {

    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));
    private static List<String> list = Arrays.asList("aaaa", "bb", "cccc", "d");

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testLimitRate() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1)).limitRate(2)
                .log()
                .subscribe(x -> {
                    System.out.println(x);
                });
        holdTheWorld();
    }

    /**
     * @throws InterruptedException
     */
    @Test
    public void testLimitRate2() throws InterruptedException {
        Flux.create(x -> {
            for (long i = 0; i < 3000000L; i++) {
                System.out.println("publish>>" + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                x.next("====================aa" + i);
            }
            x.complete();
        }, FluxSink.OverflowStrategy.DROP)
//                .limitRate(3)
                .publishOn(Schedulers.single(), 1)
                .subscribe(x -> {
                    System.out.println(x);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
        holdTheWorld();
    }


    /**
     * limit Request
     */
    @Test
    public void testLimitRequest() {
        Flux.range(1, 10).limitRequest(3)
                .subscribe(x -> {
                    System.out.println(x);
                });
    }

}
