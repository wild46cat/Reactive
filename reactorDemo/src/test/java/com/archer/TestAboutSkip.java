package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;

public class TestAboutSkip {

    private static Flux<Integer> flux = Flux.range(1, 10);
    private static Flux<Integer> flux2 = Flux.range(1, 10).delayElements(Duration.ofMillis(200));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * 跳过元素从头开始
     */
    @Test
    public void testSkip() {
        flux.skip(2).subscribe(x -> System.out.println(x));
    }

    /**
     * 从尾部开始跳过元素
     */
    @Test
    public void testSkipLast() {
        flux.skipLast(2).subscribe(x -> System.out.println(x));
    }

    /**
     * 根据时间进行跳过
     */
    @Test
    public void testSkipWithDuration() throws InterruptedException {
        flux2.skip(Duration.ofMillis(1000)).subscribe(x -> System.out.println(x));
        holdTheWorld();
    }

    /**
     * 根据条件跳过,默认一直跳过，直到条件满足
     */
    @Test
    public void testSkipUntil() {
        flux.skipUntil(x -> {
            return x > 3;
        }).subscribe(x -> System.out.println(x));
    }

    /**
     * 默认跳过，直到另一个依赖的publiser出现onNext和onComplete过程
     */
    @Test
    public void testSkipUntilOther() throws InterruptedException {
        flux2.skipUntilOther(Flux.interval(Duration.ofSeconds(1))).subscribe(x -> System.out.println(x));
        holdTheWorld();
    }

    /**
     * 当条件满足时skip,默认开始就skip
     */
    @Test
    public void testSkipWhile() {
        flux.skipWhile(x -> {
            return x < 4;
        }).subscribe(x -> System.out.println(x));
    }
}
