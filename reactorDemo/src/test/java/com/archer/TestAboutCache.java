package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestAboutCache {
    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8))
            .delayElements(Duration.ofSeconds(1));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * 当一个流被多次subscribe的时候，如果没有cache,和有cache的区别就是说
     * 如果有cache，那么重新消费这个流的时候，流里面的数据的cache数或者时间
     * 是可以控制的.
     * 也就是说，如果cache是0，可以模拟处于冷流
     *
     * @throws InterruptedException
     */
    @Test
    public void testCache() throws InterruptedException {
        Flux<Integer> cache = flux.cache(0);
//        Flux<Integer> cache = flux;
        cache.subscribe(x -> {
            System.out.println(x);
        });
        Thread.sleep(5000);
        cache.subscribe(x -> {
            System.out.println("cache " + x);
        });
        holdTheWorld();
    }

    /**
     * 根据是ttl缓存数据到流中
     *
     * @throws InterruptedException
     */
    @Test
    public void TestCacheWithDuration() throws InterruptedException {
        Flux<Integer> cache = flux.cache(Duration.ofSeconds(3));
        cache.subscribe(x -> {
            System.out.println(x);
        });
        Thread.sleep(5000);
        cache.subscribe(x -> {
            System.out.println("cache " + x);
        });
        holdTheWorld();
    }


    /**
     * 进入到cache的条件是count和duration同时满足(&&)
     */
    @Test
    public void testCacheWithCountAndDuration() throws InterruptedException {
        Flux<Integer> cache = flux.cache(2, Duration.ofSeconds(3));
        cache.subscribe(x -> {
            System.out.println(x);
        });
        Thread.sleep(5000);
        cache.subscribe(x -> {
            System.out.println("cache " + x);
        });
        holdTheWorld();
    }
}
