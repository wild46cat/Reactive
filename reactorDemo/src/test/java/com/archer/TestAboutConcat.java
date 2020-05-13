package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.io.Serializable;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestAboutConcat {


    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
    private static Flux<Integer> fluxInteger = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * 连接多个publisher
     *
     * @throws InterruptedException
     */
    @Test
    public void testConcat() {
        Flux<? extends Serializable> concat = Flux.concat(flux, flux2, flux);
        concat.subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testConcat2() {
        Flux<? extends Flux<? extends Serializable>> just = Flux.just(flux, flux2);
        Flux<Serializable> concat = Flux.concat(just);
        concat.subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * 文档上说，不会打断sequence
     */
    @Test
    public void testConcatDelayError() {
        Flux<? extends Flux<? extends Serializable>> just = Flux.just(flux, flux2);
        Flux<Serializable> concat = Flux.concatDelayError(just);
        concat.subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * concatMap把当前流中的元素进行转换
     * 注意concatMap和flatMap的区别
     */
    @Test
    public void testConcatMap() {
        flux2.concatMap(x -> {
            return Flux.fromArray(x.split(""));
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * concatMap把当前流中的元素进行转换
     * 注意concatMap和flatMap的区别
     */
    @Test
    public void testConcatMapWithDelayError() {
        flux2.concatMapDelayError(x -> {
            return Flux.fromArray(x.split(""));
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * mapIterable,可以不返回Publisher,直接返回Iteratable
     */
    @Test
    public void testConcatMapIterable() {
        flux2.concatMapIterable(x -> {
            String[] split = x.split("");
            return Arrays.asList(split);
        }).subscribe(x -> {
            System.out.println(x);
        });

    }

    /**
     * concatWith
     */
    @Test
    public void testConcatWith() {
        flux.concatWith(fluxInteger).subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testConcatWithValues() {
        flux.concatWithValues(6, 2, 1, 1, 1).subscribe(x -> {
            System.out.println(x);
        });
    }

}
