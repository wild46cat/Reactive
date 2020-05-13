package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

public class TestAboutElapsed {

    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7))
            .delayElements(Duration.ofMillis(200));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"))
            .delayElements(Duration.ofMillis(300));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * 获取元素执行的毫秒数
     *
     * @throws InterruptedException
     */
    @Test
    public void testElpased() throws InterruptedException {
        flux2.elapsed()
                .subscribe(x -> {
                    String value = x.getT2();
                    System.out.println(x.getT1() + "  " + value);
                });
        holdTheWorld();
    }

}
