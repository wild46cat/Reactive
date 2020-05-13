package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestAboutDelay {

    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
    private static Flux<Integer> fluxInteger = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * delay flux中的element
     *
     * @throws InterruptedException
     */
    @Test
    public void testDelayElement() throws InterruptedException {
        flux.delayElements(Duration.ofMillis(200))
                .subscribe(x -> {
                    System.out.println(x);
                });
        holdTheWorld();
    }


    /**
     * delay 整个flux
     *
     * @throws InterruptedException
     */
    @Test
    public void testDelaySequence() throws InterruptedException {
        flux.delaySequence(Duration.ofSeconds(2))
                .subscribe(x -> {
                    System.out.println(x);
                });
        holdTheWorld();
    }


    /**
     * delay 整个flux指定的时间
     *
     * @throws InterruptedException
     */
    @Test
    public void testDelaySubscription() throws InterruptedException {
        flux.delaySubscription(Duration.ofSeconds(2)).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    /**
     * 由另一个publisher触发
     *
     * @throws InterruptedException
     */
    @Test
    public void testDelaySubscription2() throws InterruptedException {
        Flux<Integer> fluxSignal = Flux.just(1).delayElements(Duration.ofSeconds(2));
        flux.delaySubscription(fluxSignal).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    /**
     * delayuntil  延迟，直到publisher terminamte
     *
     * @throws InterruptedException
     */
    @Test
    public void testDelayUntil() throws InterruptedException {
        flux.delayUntil(x -> {
            return Flux.just(x).delayElements(Duration.ofMillis(500));
        }).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }


}
