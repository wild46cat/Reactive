package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class TestAboutCollect {
    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(100, 1, 2, 3, 4, 5, 6, 7, 8))
            .delayElements(Duration.ofMillis(50));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testCollectList() throws InterruptedException {
        Mono<List<Integer>> listMono = flux.collectList();
        listMono.subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    @Test
    public void testCollectSortedList() throws InterruptedException {
        Mono<List<Integer>> listMono = flux.collectSortedList();
        listMono.subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    /**
     * 自定义comparator
     *
     * @throws InterruptedException
     */
    @Test
    public void testCollectSortedListWithSelfComparator() throws InterruptedException {
        Mono<List<Integer>> listMono = flux.collectSortedList(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        listMono.subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    @Test
    public void testCollectMap() throws InterruptedException {
        Mono<Map<String, Integer>> mapMono = flux.collectMap(key -> {
            if (key % 2 != 0) {
                return "基数";
            } else {
                return "偶数";
            }
        });
        mapMono.subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    @Test
    public void testCollectMap2() throws InterruptedException {
        Mono<Map<String, Integer>> mapMono = flux.collectMap(key -> {
            if (key % 2 != 0) {
                return "基数";
            } else {
                return "偶数";
            }
        }, value -> {
            return value + 100;
        });
        mapMono.subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    @Test
    public void testCollectMutilMap() throws InterruptedException {
        Mono<Map<String, Collection<Integer>>> mapMono = flux.collectMultimap(key -> {
            if (key % 2 != 0) {
                return "基数";
            } else {
                return "偶数";
            }
        });
        mapMono.subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    @Test
    public void testCollectMutilMap2() throws InterruptedException {
        Mono<Map<String, Collection<Integer>>> mapMono = flux.collectMultimap(key -> {
            if (key % 2 != 0) {
                return "基数";
            } else {
                return "偶数";
            }
        }, value -> {
            return value + 100;
        });
        mapMono.subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }


}
