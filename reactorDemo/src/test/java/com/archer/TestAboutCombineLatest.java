package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiFunction;
import java.util.function.Function;

public class TestAboutCombineLatest {

    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8))
            .delayElements(Duration.ofMillis(1000));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("a", "b", "c", "e"))
            .delayElements(Duration.ofMillis(3000));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * combineLatest 聚合多个publisher,其中的任何一个流有新元素，都会触发合并操作
     *
     * @throws InterruptedException
     */
    @Test
    public void testCombineLatest() throws InterruptedException {
        Flux.combineLatest(new Function<Object[], String>() {
            @Override
            public String apply(Object[] objects) {
                String res = "";
                for (int i = 0; i < objects.length; i++) {
                    res = res + objects[i];
                }
                return res;
            }
        }, flux, flux2).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    @Test
    public void testCombineLatest2() throws InterruptedException {
        Flux.combineLatest(flux, flux2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) {
                return integer + "  " + s;
            }
        }).subscribe(x -> {
            System.out.println(x);
        });

        holdTheWorld();
    }
}
