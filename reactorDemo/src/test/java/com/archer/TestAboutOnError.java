package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

public class TestAboutOnError {
    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));
    private static List<String> list = Arrays.asList("aaaa", "bb", "cccc", "d");

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testOnErrorContinue() {
        Flux<Integer> merge = Flux.merge(Mono.error(new IllegalArgumentException("error1")),
                flux,
                Mono.error(new TimeoutException("ddd"))
        );

        merge
                .onErrorContinue((e, f) -> {
                    //error 处理
                    System.out.println(e.getMessage() + "  " + f);
                })
                .subscribe(x -> {
                    System.out.println(x);
                });
    }

    /**
     * 通过OnErrorContinue让流继续执行
     */
    @Test
    public void testOnErrorContinue2() {
        flux.map(x -> {
            return x / (x - 2);
        }).onErrorContinue((e, o) -> {
            System.out.println(e + " error object is:" + o);
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * 把throwable转换成指定的throwable
     */
    @Test
    public void testOnErrorMap() {
        flux.map(x -> {
            return x / (x - 2);
        }).onErrorMap(throwable -> {
            System.out.println("origin throwable:" + throwable);
            return new IllegalArgumentException("new error");
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testOnErrorResume() {
        flux.map(x -> {
            return x / (x - 2);
        }).onErrorResume(throwable -> {
            System.out.println("now throwable is " + throwable);
            //用resume中的fallback Publisher替换出错的publisher
            return Flux.just(2, 3, 4);
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testOnErrorReturn() {
        flux.map(x -> {
            return x / (x - 2);
        }).onErrorReturn(9).subscribe(x -> {
            System.out.println(x);
        });

    }
}
