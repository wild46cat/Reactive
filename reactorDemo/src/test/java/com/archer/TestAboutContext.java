package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class TestAboutContext {

    private static Flux<Integer> flux = Flux.range(1, 10);
    private static Flux<Integer> flux2 = Flux.range(100, 10).delayElements(Duration.ofMillis(200));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * 通过subscribeContext设置context的内容
     * 通过Mono.subscriberContext()来访问context中的值
     */
    @Test
    public void testContext() {
        String key = "message";
        String key2 = "message2";
        Mono.just("hello")
                .flatMap(x -> {
                    return Mono.subscriberContext().map(context -> {
                        return x + " " + context.getOrDefault(key, "default") + " " + context.getOrDefault(key2, "...");
                    });
                })
                .subscriberContext(context -> {
                    return context.put(key, "world").put(key2, "!!!");
                })
                .subscribe(x -> System.out.println(x));
    }

    /**
     * 注意subscriberContext的位置，位置决定顺序(自底向上)
     */
    @Test
    public void testContext2() {
        String key = "message";
        String key2 = "message2";
        Mono.just("hello")
                .flatMap(x -> {
                    return Mono.subscriberContext().map(context -> {
                        return x + " " + context.getOrDefault(key, "default") + " " + context.getOrDefault(key2, "...");
                    });
                })
                .subscriberContext(context -> context.put(key, "www"))
                .subscriberContext(context -> {
                    return context.put(key, "world").put(key2, "!!!");
                })
                .subscribe(x -> System.out.println(x));
    }

    /**
     * 看一下context是否有嵌套关系
     * 是有嵌套关系的，如果最内层里有对应的key，那么去最内层的，否则看外层是否有.
     * context存在内外层的依赖，如果内层不存在，可以从外层去找。
     */
    @Test
    public void testContext3() {
        String key = "message";
        String key2 = "message2";
        Mono<String> stringMono = Mono.just("hello")
                .flatMap(x -> {
                    return Mono.subscriberContext().map(context -> {
                        return x + " " + context.getOrDefault(key, "default") + " " + context.getOrDefault(key2, "...");
                    });
                });
//                .subscriberContext(context -> context.put(key, "www"))
//                .subscriberContext(context -> {
//                    return context.put(key, "world").put(key2, "!!!");
//                });
        Mono.just("outer").flatMap(outer -> {
            return stringMono;
        })
                .subscriberContext(context -> context.put(key, "outerworld"))
                .subscribe(x -> System.out.println(x));

    }
}
