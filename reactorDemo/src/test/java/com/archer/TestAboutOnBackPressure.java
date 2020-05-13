package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BufferOverflowStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestAboutOnBackPressure {
    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8))
            .delayElements(Duration.ofMillis(500));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"))
            .delayElements(Duration.ofMillis(600));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testOnBackPressureBuffer() throws InterruptedException {
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
        })
                .onBackpressureBuffer(5, BufferOverflowStrategy.DROP_OLDEST)
//                .onBackpressureBuffer(2, x -> {
//                    System.out.println("--------------------overflow " + x);
//                }, BufferOverflowStrategy.DROP_LATEST)
//                .onBackpressureBuffer(Duration.ofMillis(200), 3, x -> {
//                    System.out.println("--------------------overflow " + x);
//                })
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


    @Test
    public void testOnBackPressureDrop() throws InterruptedException {
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
        })
                .onBackpressureDrop(x -> {
                    System.out.println("--------------drop " + x);
                })
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

    @Test
    public void testOnBackPressureLatest() throws InterruptedException {
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
        })
                .onBackpressureLatest()
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
}
