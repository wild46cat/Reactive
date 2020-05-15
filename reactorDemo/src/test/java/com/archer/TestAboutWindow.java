package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class TestAboutWindow {
    private static Flux<Integer> flux = Flux.range(1, 10);
    private static Flux<Integer> flux2 = Flux.range(100, 10).delayElements(Duration.ofMillis(200));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }


    @Test
    public void test2() {
        Flux<Flux<Integer>> window = flux.window(3);
        window.subscribe(fluxx -> {
            System.out.println("----");
            fluxx.subscribe(x -> {
                System.out.println(Thread.currentThread().getName() + "  " + x);
            });
        });
    }

    /**
     * 与buffer的区别在于输出的是多个流
     *
     * @throws InterruptedException
     */
    @Test
    public void test() throws InterruptedException {
        Flux<Flux<Integer>> window = flux.window(3);
        window.parallel(3).runOn(Schedulers.parallel())
                .subscribe(fluxx -> {
                    System.out.println("----");
                    fluxx.subscribe(x -> {
                        System.out.println(Thread.currentThread().getName() + "  " + x);
                    });
                });
    }
}
