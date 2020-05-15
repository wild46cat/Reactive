package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

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
                        return x + " " + context.getOrDefault(key, "default") +
                                " " + context.getOrDefault(key2, "...");
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

    /**
     * context互相访问（平级模块中设置并访问）
     * 有问题,问题处在模块间的平行依赖
     */
    @Test
    public void testContext4() {
        String key = "message";
        String key2 = "message2";
        Mono.just("hello")
                .flatMap(x -> {
                    return Mono.subscriberContext().map(context -> {
                        String keyValue = context.getOrDefault(key, "empty");
                        context.put(key2, "key2value");
                        System.out.println("flatmap1 " + context.getOrDefault(key2, "key2..."));
                        return context;
                    });
                })
                .flatMap(x -> {
                    return Mono.subscriberContext().map(context -> {
                        String keyValue = context.getOrDefault(key, "empty");
                        System.out.println("flatmap2 " + context.getOrDefault(key2, "key2..."));
                        context.put(key2, "key2value");
                        return context;
                    });
                })
                .subscriberContext(context -> context.put(key, "www"))
                .subscribe(x -> System.out.println(x));
    }

    /**
     * 程序主流程demo
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
                        return newx;
                    }).transform(stage2).subscriberContext(context -> context.put(key1, map));
                });


        Flux.just("hello")
                .transform(stage1)
                .subscriberContext(context -> context.put(key, "www"))
                .subscribe(x -> System.out.println(x));
    }

    /**
     * 说明不能写在同一个key上
     */
//    @Test
//    public void testContext6() {
//        String key = "start";
//        String key1 = "stage1";
//        String key2 = "stage2";
//
//        //第二步
//        Function<Mono<String>, Mono<String>> stage2 =
//                f -> f.flatMap(x -> {
//                    //逻辑处理
//                    String newx = x + "..stage2";
//                    final Map<String, Object> map = new HashMap<>();
//                    return Mono.subscriberContext().map(context -> {
//                        System.out.println("stage2 context before change" + context);
//                        HashMap<String, Object> contextOrDefault = context.getOrDefault(key1, new HashMap<String, Object>());
//                        System.out.println("stage2 get Context from stage1" + contextOrDefault);
//                        map.put("addkk", "addvv");
//                        map.put("kk", newx);
//                        System.out.println("stage2 context after change" + context);
//                        return newx;
//                    }).subscriberContext(context -> {
//                        //模拟修改context中的内容
//                        return context.put(key1, map);
//                    });
//                });
//
//        //第一步
//        Function<Flux<String>, Flux<String>> stage1 =
//                f -> f.flatMap(x -> {
//                    //写逻辑处理
//                    String newx = x + "..stage1";
//                    //处理context
//                    final Map<String, Object> map = new HashMap<>();
//                    return Mono.subscriberContext().map(context -> {
//                        System.out.println("stage1 context before change" + context);
//                        //怎么获取context并且操作到变量中
//                        String aDefault = context.getOrDefault(key, "defaultkeyvalue");
//                        map.put("kk", aDefault);
//                        System.out.println("stage1 context after change" + context);
//                        return newx;
//                    }).transform(stage2).subscriberContext(context -> context.put(key1, map));
//                });
//
//
//        Flux.just("hello")
//                .transform(stage1)
//                .subscriberContext(context -> context.put(key, "www"))
//                .subscribe(x -> System.out.println(x));
//    }
}
