package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class TestAboutThen {

    private static Flux<Integer> flux = Flux.range(1, 10);
    private static Flux<Integer> flux2 = Flux.range(100, 10).delayElements(Duration.ofMillis(200));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * then()返回一个空节点Mono<void>
     */
    @Test
    public void testThen() {
        flux.then().subscribe(x -> System.out.println(x));
    }

    @Test
    public void testThen2() {
        flux.map(x -> x + 3).then(Mono.just(1))
                .subscribe(x -> System.out.println(x));
    }

    @Test
    public void testThenMany() throws InterruptedException {
        flux.thenMany(flux2).subscribe(x -> System.out.println(x));
        holdTheWorld();
    }



}
