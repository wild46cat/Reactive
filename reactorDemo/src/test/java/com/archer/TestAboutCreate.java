package com.archer;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestAboutCreate {

    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * create 动态的生成Flux
     */
    @Test
    public void testCreate() throws InterruptedException {
        Flux.create(x -> {
            for (int i = 0; i < 8; i++) {
                x.next("aa" + i);
            }
            x.complete();
        }).delayElements(Duration.ofMillis(500)).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    /**
     * 带有backPressure的flux
     *
     * @throws InterruptedException
     */
    @Test
    public void testCreate2() throws InterruptedException {
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
        }, FluxSink.OverflowStrategy.BUFFER)
//                .publishOn(Schedulers.newParallel("tt", 4), 1)
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

    class SlowSubscribe extends BaseSubscriber {
        @Override
        protected void hookOnSubscribe(Subscription subscription) {
            request(1);
        }

        @SneakyThrows
        @Override
        protected void hookOnNext(Object value) {
            Thread.sleep(50);
            System.out.println(value);
            request(1);
        }

    }


}
