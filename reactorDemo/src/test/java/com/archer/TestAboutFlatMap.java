package com.archer;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

public class TestAboutFlatMap {
    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));
    private static List<String> list = Arrays.asList("aaaa", "bb", "cccc", "d");

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * flatmap 不保证顺序
     *
     * @throws InterruptedException
     */
    @Test
    public void testFlatMap() throws InterruptedException {
        flux.flatMap(x -> {
            String s = list.get(x);
            return Flux.fromArray(s.split("")).delayElements(Duration.ofMillis(300));
        }).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    /**
     * flatmap 不保证顺序,可以设置内部序列的最大并发数从而实现有序
     *
     * @throws InterruptedException
     */
    @Test
    public void testFlatMap2() throws InterruptedException {
        flux.flatMap(x -> {
            String s = list.get(x);
            return Flux.fromArray(s.split("")).delayElements(Duration.ofMillis(300));
        }, 1).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }


    /**
     * 注意这里的顺序
     *
     * @throws InterruptedException
     */
    @Test
    public void testFlatMapIterable() throws InterruptedException {
        flux.flatMapIterable(x -> {
            String s = list.get(x);
            return Arrays.asList(s.split(""));
        }).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    /**
     * 和flatmap的区别就是在于是否有序
     *
     * @throws InterruptedException
     */
    @Test
    public void testFlatMapSequential() throws InterruptedException {
        flux.flatMapSequential(x -> {
            String s = list.get(x);
            return Flux.fromArray(s.split("")).delayElements(Duration.ofMillis(300));
        }, 1).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }


}
