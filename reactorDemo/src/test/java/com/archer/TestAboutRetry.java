package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.retry.Retry;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;

public class TestAboutRetry {
    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));
    private static List<String> list = Arrays.asList("aaaa", "bb", "cccc", "d");

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * retry 整体的flux
     */
    @Test
    public void testRetry() {
        flux.map(x -> {
            return x / (x - 2);
        }).retry(2)
                .onErrorContinue((throwable, o) -> {
                    System.out.println("error--------" + o);
                })
                .subscribe(x -> {
                    System.out.println(x);
                });
    }

    public Mono testFluxMethod() {
        System.out.println("execute myMethod");
        return Mono.error(new IllegalArgumentException("tt"));
//        return Mono.error(new Exception("tt"));
    }

    /**
     * 如果有问题，retry2次,现在的问题就是说
     * 如果用了retry那么就不能进行onErrorContinue这种方法进行异常捕获
     * 我们想要的是有问题retry，retry还有问题，能捕获能处理
     */
    @Test
    public void testRetryMono() {
        Mono.defer(() -> this.testFluxMethod())
//        this.testFluxMethod()
                .retry(2)
                .onErrorContinue((throwable, o) -> {
                    System.out.println("error-------" + o);
                })
                .subscribe(x -> {
                    System.out.println(x);
                }, e -> {
                    System.out.println("err------>" + e);
                });
    }

    /**
     * 根据错误类型重试
     */
    @Test
    public void testRetryMono2() {
        Mono.defer(() -> this.testFluxMethod())
//        this.testFluxMethod()
                .retry(2, new Predicate<Throwable>() {
                    @Override
                    public boolean test(Throwable throwable) {
                        return throwable.getClass().equals(IllegalArgumentException.class);
                    }
                })
                .subscribe(x -> {
                    System.out.println(x);
                });
    }

    /**
     * 注意，这里的retry是在reactor-extra包中的
     * 推荐这种用法，更方便更灵活
     * 强调这里的OnErrorContinue是生效的(这个很有意义)
     */
    @Test
    public void testRetryWhen() {
        Retry<Object> retry = Retry.anyOf(Exception.class).retryMax(2)
                .doOnRetry(x -> {
                    System.out.println("retry doing.. exception info:" + x.exception().getMessage());
                });
//        Retry<Object> retry = Retry.allBut(IllegalArgumentException.class, IOException.class).retryMax(2);
        Mono.defer(() -> this.testFluxMethod())
                .retryWhen(retry)
                .onErrorContinue((throwable, o) -> {
                    System.out.println("error----------" + o);
                })
                .subscribe(x -> {
                    System.out.println(x);
                });
    }

    /**
     * flux中元素的retry,某个元素有问题后，不会影响后续元素
     */
    @Test
    public void testRetryWhen2() {
        Retry<Object> retry = Retry.allBut(IllegalArgumentException.class).retryMax(2)
                .doOnRetry(x -> {
                    System.out.println("retry doing.. exception info:" + x.exception().getMessage());
                });
//        Retry<Object> retry = Retry.allBut(IllegalArgumentException.class, IOException.class).retryMax(2);
        flux.flatMap(x -> {
            Mono mono = Mono.defer(() -> Mono.just(x / (x - 2))).retryWhen(retry);
            return mono;
        })
                .onErrorContinue((throwable, o) -> {
                    System.out.println("error----------" + o);
                })
                .subscribe(x -> {
                    System.out.println(x);
                });
    }
}
