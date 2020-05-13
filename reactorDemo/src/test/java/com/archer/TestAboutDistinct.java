package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestAboutDistinct {

    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 1, 1, 2, 3, 4, 4, 5, 2, 6, 3));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * 获取distinct对象
     */
    @Test
    public void testDistince() {
        flux.distinct().subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testDistince2() {
        flux.distinct(x -> {
            return x % 2;
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testDistince3() {
        flux.distinct(x -> {
            return x % 2;
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * distinct until changed 去重复，只和前一个元素比
     */
    @Test
    public void testDistinctUntilChanged() {
        flux.distinctUntilChanged().subscribe(x -> {
            System.out.println(x);
        });
    }
}
