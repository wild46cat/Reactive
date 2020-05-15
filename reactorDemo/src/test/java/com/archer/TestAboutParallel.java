package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TestAboutParallel {

    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"))
            .delayElements(Duration.ofMillis(1000));
    private static List<String> list = Arrays.asList("aaaa", "bb", "cccc", "d");

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testParallel() throws InterruptedException {
        flux.parallel(3).runOn(Schedulers.parallel())
                .subscribe(x -> {
                    System.out.println(Thread.currentThread().getName() + "  " + x);
                });
    }

    @Test
    public void testParallel2() throws InterruptedException {
        flux.parallel(3).runOn(Schedulers.elastic())
                .subscribe(x -> {
                    System.out.println(Thread.currentThread().getName() + "  " + x);
                });
    }


    @Test
    public void testParallel3() throws InterruptedException {
        flux.parallel(3).runOn(Schedulers.single())
                .subscribe(x -> {
                    System.out.println(Thread.currentThread().getName() + "  " + x);
                });
    }

    @Test
    public void testParallel4() throws InterruptedException {
        flux.publishOn(Schedulers.single())
                .subscribe(x -> {
                    System.out.println(Thread.currentThread().getName() + "  " + x);
                });
    }

    /**
     * 模拟请求多个gprc或者http的阻塞行为
     */
    @Test
    public void testParallel5() throws InterruptedException {
        Integer start = Flux.just("start").flatMap(x -> {
            return Flux.just(300, 200, 100)
//                    .publishOn(Schedulers.parallel(), 3)
                    .parallel(3).runOn(Schedulers.parallel(), 3)
                    .flatMap(k -> {
                        try {
                            System.out.println("begin sleep " + Thread.currentThread().getName() + "  " + k);
                            Thread.sleep(k);
                            System.out.println("end sleep " + k);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return Mono.just(k);
                    });
        }).blockFirst();
        System.out.println(start);
        holdTheWorld();
    }

}
