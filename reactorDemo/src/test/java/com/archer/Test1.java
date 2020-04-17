package com.archer;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Test1 {
    public static String[] strArr = {"1", "2", "0", "3"};

    @Test
    public void test() {
        Flux.just("hello world");
//                .subscribe(x -> System.out.println(x));
        Flux.just("hello world2")
                .subscribe(System.out::println);
    }

    @Test
    public void test2() {
        Flux<String> stringFlux = Flux.fromArray(strArr);
                stringFlux.subscribe(x -> System.out.println(x));
    }

    int tt(int a){
        return a-1;
    }
    @Test
    public void Test21() {
        Flux.range(1, 6).map(i -> i * i)
                .map(x-> {
                    return x-1;
                })
                .subscribe(x-> System.out.println(x));
    }

    @Test
    public void test22() throws Exception {
        Flux<String> stringFlux = Flux.just("abcde", "fghijk").flatMap(x -> {
            return Flux.fromArray(x.split(""));
//                    .delayElements(Duration.ofMillis(100));
        });
        stringFlux.subscribe(x -> System.out.println(x));

        Flux<String> stringFlux2 = Flux.just("aa", "bb").flatMap(x -> {
            return Flux.fromArray(x.split(""));
        }).map(x->{
            return x+"xcvcv";
        });
        stringFlux2.subscribe(x -> System.out.println(x));
    }

    @Test
    public void test23() {
        StepVerifier.create(
                Flux.just("flux", "mono")
                        .flatMap(s -> Flux.fromArray(s.split(""))
                                .delayElements(Duration.ofMillis(100)))
                        .doOnNext(System.out::print))
                .expectNextCount(8)
                .verifyComplete();
    }

    @Test
    public void test24() {
        Flux<Integer> flux = Flux.range(5, 10);
        flux.subscribe(x -> System.out.println(x));

        System.out.println("===================");
        Flux<Integer> filterFlux = flux.filter(x -> {
            return x > 10;
        });

        filterFlux.subscribe(x -> System.out.println(x));
    }

    @Test
    public void test25() {
        Flux flux1 = Flux.range(1, 10);
        Flux flux2 = Flux.fromIterable(Arrays.asList("aa", "bb", "cc", "dd"));
        Flux.zip(flux1, flux2, (x, y) -> {
            return x + ":" + y;
        }).subscribe(x -> System.out.println(x));
    }

    @Test
    public void test3() {
        Flux<String> flux = Flux.range(0, 10)
                .checkpoint("range checkpoint")
                .map(i -> {
                    if (i > 1) {
                        return "now is :" + i;
                    } else {
                        throw new RuntimeException("error number");
                    }
                })
                .checkpoint("map checkpoint");
        flux.subscribe(x-> System.out.println(x));
//        flux.subscribe(x -> System.out.println(x),
//                error -> {
//                    System.out.println("error:" + error);
//                },
//                () -> {
//                    System.out.println("finish");
//                },
//                subscription -> {
//                    subscription.request(100);
//                });
    }

    @Test
    public void test4() {
        SampleSubscriber<String> ss = new SampleSubscriber<>();
        Flux.range(5, 11)
                .checkpoint("range checkpoint")
                .map(i -> {
                    if (i > 1) {
                        return "now is :" + i;
                    } else {
                        throw new RuntimeException("error number");
                    }
                })
                .checkpoint("map checkpoint")
                .subscribe(ss);
    }

    @Test
    public void test5() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))
                .map(x -> "now: " + x)
                .subscribe(x -> {
                    System.out.println(x);
                });
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

    @Test
    public void test6() {
        Flux.range(0, 10).limitRate(2).subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void test7() {
        Flux.generate(() -> 0,
                (state, sink) -> {
                    System.out.println("now state is " + state);
                    if (state == 10) {
                        sink.complete();
                    }
                    return state + 1;
                }).subscribe(x -> System.out.println(x));
    }

    @Test
    public void test8() throws InterruptedException {
        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 10) sink.complete();
                    return state + 1;
                });
        flux.subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void test9() {
        Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next(state);
                    if (state == 10) {
                        sink.complete();
                    }
                    return state + 1;
                }).subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void test10() {
        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }, FluxSink.OverflowStrategy.BUFFER).subscribe(x -> System.out.println(x));
    }

    @Test
    public void test11() {
        //
        Flux.push(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }, FluxSink.OverflowStrategy.BUFFER).subscribe(x -> System.out.println(x));
    }

    public String alphabet(int letterNumber) {
        if (letterNumber < 1 || letterNumber > 26) {
            return null;
        }
        int letterIndexAscii = 'A' + letterNumber - 1;
        return "" + (char) letterIndexAscii;
    }

    @Test
    public void test12() {
        Flux<String> alphabet = Flux.just(-1, 30, 13, 9, 20)
                .handle((i, sink) -> {
                    String letter = alphabet(i);
                    if (letter != null)
                        sink.next(letter);
                });

        alphabet.subscribe(System.out::println);

        System.out.println("=========");
        Flux<String> alphabet2 = Flux.just(-1, 30, 13, 9, 20)
                .map(x -> {
                    return  alphabet(x);
                }).filter(x -> {
                    return x != null;
                });

        alphabet2.subscribe(System.out::println);
    }

    @Test
    public void t(){
        Flux.just(-1, 30, 13, 9, 20).filter(x->{
            return x > 0;
        }).subscribe(x-> System.out.println(x));
    }

    @Test
    public void test13() {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

        final Flux<String> flux = Flux
                .range(1, 3)
                .map(i -> 10 + i + " first-- " + Thread.currentThread().getName())
                .publishOn(s)
                .map(i -> i + " second-- " + Thread.currentThread().getName());
        flux.subscribe(x -> System.out.println(x));

//        new Thread(() -> flux.subscribe(System.out::println));
    }

    @Test
    public void test14() {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);

        final Flux<String> flux = Flux
                .range(1, 100)
                .map(i -> i + " first-- " + Thread.currentThread().getName())
                .subscribeOn(s)
                .map(i -> {
                    return i + " second-- " + Thread.currentThread().getName();
                });
        flux.subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void test15() {
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4);
        Scheduler ss = Schedulers.newParallel("parallel-new", 4);

        final Flux<String> flux = Flux
                .range(1, 3)
                .map(i -> 10 + i + " first--" + Thread.currentThread().getName())
                .publishOn(s)
                .map(i -> i + " second--" + Thread.currentThread().getName())
                .publishOn(s)
                .map(i -> i + " Third--" + Thread.currentThread().getName())
                .subscribeOn(ss)
                .map(i -> i + " end--" + Thread.currentThread().getName());
        flux.subscribe(x -> {
            System.out.println(x);
        });
    }


    @Test
    public void test16() {
        try {
            for (int i = 0; i < 2; i++) {
                System.out.println(3 / i);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        //the same
        Flux<Integer> flux = Flux.range(0, 3)
                .map(i -> {
                    return 3 / i;
                });
        flux.subscribe(x -> {
            System.out.println(x);
        }, error -> {
            System.out.println(error);
        });
    }

    @Test
    public void testBackpressure() {
        Flux.range(1, 6)    // 1
                .doOnRequest(n -> System.out.println("Request " + n + " values..."))    // 2
                .subscribe(new BaseSubscriber<Integer>() {  // 3
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) { // 4
                        System.out.println("Subscribed and make a request...");
                        request(1); // 5
                    }

                    @Override
                    protected void hookOnNext(Integer value) {  // 6
                        try {
                            TimeUnit.SECONDS.sleep(1);  // 7
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Get value [" + value + "]");    // 8
                        request(1); // 9
                    }
                });
    }

    public double someThingError(int x) {
        try {
            double k = 3 / x;
            return k;
        } catch (Exception e) {
            return 0;
        }
    }

    @Test
    public void Test17() {
        System.out.println(someThingError(0));

        Flux.just(0).map(x -> {
            return 3 / x;
        }).onErrorReturn(0).subscribe(x -> System.out.println(x));

    }

    public double calulate(int x) {
        return 3 / x;
    }

    public double calulateSafe(int x) {
        return x;
    }

    public double someThingErr2(int x) {
        try {
            return calulate(x);
        } catch (Exception e) {
            return calulateSafe(x);
        }
    }

    public <T> Flux<T> appendBoomError(Flux<T> source) {
        return source.concatWith(Mono.error(new IllegalArgumentException("boom")));
    }

    @Test
    public void testAppendBoomError() {
        Flux<String> source = Flux.just("thing1", "thing2");

        StepVerifier.create(appendBoomError(source))
                .expectNext("thing2")
                .expectNext("thing1")
                .expectErrorMessage("boom")
                .verify();
    }
}

class SampleSubscriber<T> extends BaseSubscriber<T> {
    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        System.out.println("subscription");
        request(1);
    }

    @Override
    protected void hookOnNext(T value) {
        System.out.println(value);
        request(1);
    }

    @Override
    protected void hookOnComplete() {
        System.out.println("finish...");
    }
}
