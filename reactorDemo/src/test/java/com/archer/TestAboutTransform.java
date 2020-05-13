package com.archer;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class TestAboutTransform {

    private static Flux<Integer> flux = Flux.range(1, 10);
    private static Flux<Integer> flux2 = Flux.range(100, 10).delayElements(Duration.ofMillis(200));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * transform的作用
     */
    @Test
    public void testTransform() {
        Function<Flux<String>, Flux<String>> filterAndMap =
                f -> f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);

        Flux<String> fluxWithTransform = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .transform(filterAndMap).transform(filterAndMap);
        fluxWithTransform.subscribe(d -> System.out.println("Subscriber to Transformed MapAndFilter: " + d));
//        fluxWithTransform.subscribe(d -> System.out.println("Subscriber to Transformed MapAndFilter: " + d));
    }

    public Flux<String> doSomething(Flux<String> f) {
        return f.filter(color -> !color.equals("orange"))
                .map(String::toUpperCase);
    }

    @Test
    public void testTransform2() {
        Flux<String> fluxWithTransform = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .transform(this::doSomething);
        fluxWithTransform.subscribe(d -> System.out.println("Subscriber to Transformed MapAndFilter: " + d));
    }

    /**
     * 这里要注意transform和transformdeferrd的区别
     */
    @Test
    public void testTransformiDerferred() {
        AtomicInteger ai = new AtomicInteger();
        Function<Flux<String>, Flux<String>> filterAndMap = f -> {
            if (ai.incrementAndGet() == 1) {
                return f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);
            }
            return f.filter(color -> !color.equals("purple"))
                    .map(String::toUpperCase);
        };

        Flux<String> composedFlux =
                Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                        .transformDeferred(filterAndMap);

        composedFlux.subscribe(d -> System.out.println("Subscriber 1 to Composed MapAndFilter :" + d));
        System.out.println("---------------------------------");
        composedFlux.subscribe(d -> System.out.println("Subscriber 2 to Composed MapAndFilter: " + d));
    }
}
