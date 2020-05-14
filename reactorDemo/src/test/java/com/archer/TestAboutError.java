package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

public class TestAboutError {

    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testError() {
        Flux<Object> test_error = Flux.error(new IllegalArgumentException("test error"));
        test_error.concatWith(flux);
        test_error.subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testError2() {
        Flux<Object> test_error = Flux.error(new IllegalArgumentException("test error"), true);
        test_error.subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testError3() {
        Flux<Object> test_error = Flux.error(new IllegalArgumentException("test error"), false);
        test_error.subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * 程序主流程demo 带error的
     * 采用嵌套的方式,实现流程的执行,同时实现context的值相互传递
     */
    @Test
    public void testContext5() {
        String key = "start";
        String key1 = "stage1";
        String key2 = "stage2";

        //第二步
        Function<Mono<String>, Mono<String>> stage2 =
                f -> f.flatMap(x -> {
                    //逻辑处理
                    String newx = x + "..stage2";
                    final Map<String, Object> map = new HashMap<>();
                    return Mono.subscriberContext().map(context -> {
                        System.out.println("stage2 context before change" + context);
                        HashMap<String, Object> contextOrDefault = context.getOrDefault(key1, new HashMap<String, Object>());
                        System.out.println("stage2 get Context from stage1" + contextOrDefault);
                        map.put("addkk", "addvv");
                        map.put("kk", newx);
                        System.out.println("stage2 context after change" + context);
                        if (x.length() < 1000) {
                            throw new IllegalArgumentException("stage2 error");
                        }
                        return newx;
                    }).subscriberContext(context -> {
                        //模拟修改context中的内容
                        return context.put(key2, map);
                    });
                });

        //第一步
        Function<Flux<String>, Flux<String>> stage1 =
                f -> f.flatMap(x -> {
                    //写逻辑处理
                    String newx = x + "..stage1";
                    //处理context
                    final Map<String, Object> map = new HashMap<>();
                    return Mono.subscriberContext().map(context -> {
                        System.out.println("stage1 context before change" + context);
                        //怎么获取context并且操作到变量中
                        String aDefault = context.getOrDefault(key, "defaultkeyvalue");
                        map.put("kk", aDefault);
                        System.out.println("stage1 context after change" + context);
                        if (x.length() < 1000) {
                            throw new IllegalArgumentException("stage1 error");
                        }
                        return newx;
                    }).transform(stage2).subscriberContext(context -> context.put(key1, map));
                });


        Flux.just("hello")
                .transform(stage1)
                .subscriberContext(context -> context.put(key, "www"))
                .onErrorResume(e -> {
                    System.out.println("there is an error" + e.getMessage());
                    return Mono.just("ERROR~!!!");
                })
                .subscribe(x -> System.out.println(x));
    }
}
