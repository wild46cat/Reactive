package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 * 这些能够让Reactor实现同步的编程
 * 如果在非阻塞的Scheduler中，会报错UnsupportedOperatorException
 */
public class TestAboutBlock {

    /**
     * blockFirst 等待第一个元素执行完成
     */
    @Test
    public void testBlockFirst() {
        Flux<Integer> flux = Flux.just(1, 2, 3);
        flux.subscribe(x -> {
            System.out.println(x);
        });
        Integer integer = flux.blockFirst();
        System.out.println(integer);
    }

    /**
     * blockFirstWithDuration(Duration)
     * block第一个元素{duration} 时间
     */
    @Test
    public void TestBlockFirstWithDuration() throws InterruptedException {
        Flux<Integer> flux = Flux.just(1, 2, 3).delayElements(Duration.ofSeconds(1));
        flux.subscribe(x -> {
            System.out.println(x);
        });
        Integer integer = flux.blockFirst(Duration.ofMillis(200));
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();

    }

    /**
     * blockLast block直到最后一个元素完成
     */
    @Test
    public void testBlockLast() {
        Flux<Integer> flux = Flux.just(1, 2, 3).delayElements(Duration.ofSeconds(1));
        flux.subscribe(x -> {
            System.out.println(x);
        });
        Integer integer = flux.blockLast();
    }

    @Test
    public void testBlockLastWithDuration() throws InterruptedException {
        Flux<Integer> flux = Flux.just(1, 2, 3).delayElements(Duration.ofMillis(100));
//        flux.subscribe(x -> {
//            System.out.println(x);
//        });
        Integer integer = flux.blockLast(Duration.ofMillis(400));
        System.out.println(integer);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
