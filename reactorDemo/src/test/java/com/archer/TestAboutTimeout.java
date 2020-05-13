package com.archer;

import lombok.ToString;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class TestAboutTimeout {
    private static Flux<Integer> flux = Flux.range(1, 10);
    private static Flux<Integer> flux2 = Flux.range(100, 10).delayElements(Duration.ofMillis(200));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * 这里的timeout检测到publisher中有第一个元素出来
     *
     * @throws InterruptedException
     */
    @Test
    public void testTimeout() throws InterruptedException {
        flux2.timeout(Duration.ofMillis(100))
                .subscribe(x -> System.out.println(x),
                        e -> System.out.println("error occured timeout"));
        holdTheWorld();
    }

    /**
     * 出现timeout会使用新的publisher代替
     *
     * @throws InterruptedException
     */
    @Test
    public void testTimeoutWithFallBack() throws InterruptedException {
        flux2.timeout(Duration.ofMillis(100), flux)
                .subscribe(x -> System.out.println(x));
        holdTheWorld();
    }

    @Test
    public void testTimeoutWithFallback2() throws InterruptedException {
        Mono.just(1).delayElement(Duration.ofMillis(300))
                .timeout(Duration.ofMillis(200), Mono.just(2))
                .subscribe(x -> System.out.println(x));
        holdTheWorld();
    }

}
