package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiFunction;

public class TestAboutGenerate {
    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));
    private static List<String> list = Arrays.asList("aaaa", "bb", "cccc", "d");

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testGenerate() {
        Flux.generate(synchronousSink -> {
            synchronousSink.next("aaaaa");
            synchronousSink.complete();
        }).subscribe(x -> {
            System.out.println(x);
        });
    }


    @Test
    public void testGenerate2() throws InterruptedException {
        Flux.generate(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(1000);
                return 1;
            }
        }, (x, sink) -> {
            sink.next(x);
            sink.complete();
            return x + 100;
        }).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }


}
