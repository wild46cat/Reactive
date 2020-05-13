package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.util.context.Context;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestAboutDematerialize {
    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * dematerialize 只传递 onnext oncomplete onerror
     */
    @Test
    public void testDematerialize() {
    }

    @Test
    public void testMaterialize() {
        flux.concatWith(Mono.error(new IllegalArgumentException("ttt")))
                .materialize().subscribe(x -> {
            System.out.println("-------" + x.getType());
            switch (x.getType()) {
                case ON_NEXT:
                    System.out.println(x.get());
                    break;
                case ON_ERROR:
                    Throwable throwable = x.getThrowable();
                    System.out.println(throwable);
                    break;
                case ON_COMPLETE:
                    System.out.println("complete");
                default:
                    break;
            }
            System.out.println(x);
        });
    }
}
