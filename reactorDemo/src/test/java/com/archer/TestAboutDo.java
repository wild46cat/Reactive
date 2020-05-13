package com.archer;

import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TestAboutDo {
    private static Flux<Integer> flux = Flux.fromIterable(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
    private static Flux<String> flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "ee"));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * flux完成后，指定要执行的操作
     * complete error
     */
    @Test
    public void testDoAfterTerminate() {
        flux.doAfterTerminate(() -> {
            System.out.println("terminate");
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testDoFirst() {
        flux.doFirst(() -> {
            System.out.println("three");
        }).doFirst(() -> {
            System.out.println("two");
        }).doFirst(() -> {
            System.out.println("one");
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * 和 doAfterTerminate类似，支持类型有
     * complete, error or cancel
     */
    @Test
    public void testDoFinally() {
        flux.doFinally(signalType -> {
            switch (signalType) {
                case CANCEL:
                    System.out.println("cancel");
                    break;
                case ON_ERROR:
                    System.out.println("onerror");
                    break;
                case ON_COMPLETE:
                    System.out.println("on complete");
                    break;
                default:
                    break;
            }
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * 不知道怎么触发cancel
     */
    @Test
    public void testDoOnCancel() {
        flux.doOnCancel(() -> {
            System.out.println("cancel");
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * doOnEach
     */
    @Test
    public void testDoOnComplete() {
        flux.doOnComplete(() -> {
            System.out.println("complete");
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * 在每个信号后处理，支持的信号有
     * next error complete
     */
    @Test
    public void testDoOnEach() {
        flux.doOnEach(integerSignal -> {
            System.out.println(integerSignal);
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testDoOnError() {
        flux
                .concatWith(Flux.error(new IllegalArgumentException("boom")))
                .concatWith(flux)
                .doOnError(e -> {
                    System.out.println(e);
                }).subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testDoOnNext() {
        flux.doOnNext(x -> {
            System.out.println("doOnNext " + x);
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testDoOnRequest() {
        flux.doOnRequest(x -> {
            System.out.println("doOnRequest " + x);
        }).subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testDoOnSubscribe() {
        flux.doOnSubscribe(new Consumer<Subscription>() {
            @Override
            public void accept(Subscription subscription) {
                System.out.println(subscription);
            }
        }).subscribe(x -> {
            System.out.println(x);
        });
    }


    /**
     * 触发信号是 complete 和 error
     */
    @Test
    public void testDoOnTerminate() {
        flux.doOnTerminate(() -> {
            System.out.println("terminate");
        }).subscribe(x -> {
            System.out.println(x);
        });

    }
}
