package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class TestAboutSwitch {

    private static Flux<Integer> flux = Flux.range(1, 10);
    private static Flux<Integer> flux2 = Flux.range(100, 10).delayElements(Duration.ofMillis(200));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * 转换
     */
    @Test
    public void testSwitchOnFirst() {
        flux.switchOnFirst((signal, integerFlux) -> {
            if (signal.hasValue()) {
                return integerFlux.map(x -> x + 1);
            }
            return integerFlux;
        }, true).subscribe(x -> System.out.println(x));

    }

    @Test
    public void testSwitchOnNext() {
        flux.switchMap(x -> {
            return Mono.just(x + 3);
        }).subscribe(x -> System.out.println(x));
    }
}
