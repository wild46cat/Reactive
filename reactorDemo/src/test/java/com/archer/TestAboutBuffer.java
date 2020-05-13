package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sun.text.resources.de.FormatData_de_LU;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Predicate;

public class TestAboutBuffer {
    private static Flux<String> flux = Flux.fromIterable(Arrays.asList("a", "b", "c", "d", "e", "f"));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    @Test
    public void testBuffer() {
        Flux<List<String>> buffer = flux.buffer();
        buffer.subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testBufferWithCount() {
        Flux<List<String>> buffer = flux.buffer(3);
        buffer.subscribe(x -> {
            System.out.println(x);
        });
    }

    /**
     * 每次向后移动多少有skip决定
     */
    @Test
    public void testBufferWithCountAndSkip() {
        Flux<List<String>> buffer = flux.buffer(3, 1);
        buffer.subscribe(x -> {
            System.out.println(x);
        });
    }

    @Test
    public void testBufferWithDuration() throws InterruptedException {
        Flux.just(1, 2, 3, 4, 5, 6).delayElements(Duration.ofMillis(200))
                .buffer(Duration.ofMillis(500), Duration.ofMillis(700))
                .subscribe(x -> {
                    System.out.println(x);
                });
        holdTheWorld();
    }

    /**
     * 通过另一个publisher来触发当前的publisher
     *
     * @throws InterruptedException
     */
    @Test
    public void testBufferWithOtherPublisher() throws InterruptedException {
        Flux.just(1, 2, 3, 4, 5, 6).delayElements(Duration.ofMillis(100))
                .buffer(Flux.just("a", "b", "c", "d", "e").delayElements(Duration.ofMillis(300)))
                .subscribe(x -> {
                    System.out.println(x);
                });
        holdTheWorld();
    }

    /**
     * 测试buffertimeout,个数或者时间某一个达到的时候触发
     */
    @Test
    public void testBufferTimeout() throws InterruptedException {
        Flux.just(1, 2, 3, 4, 5, 6).delayElements(Duration.ofMillis(100))
                .bufferTimeout(5, Duration.ofMillis(200))
                .subscribe(x -> {
                    System.out.println(x);
                });
        holdTheWorld();
    }

    /**
     * 测试bufferUntil,处理根据条件分隔出list
     */
    @Test
    public void testBufferUntil() throws InterruptedException {
        Flux.just(1, 2, 3, 4, 8, 6).delayElements(Duration.ofMillis(100))
                .bufferUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) {
                        return integer % 2 == 0;
                    }
                })
                .subscribe(x -> {
                    System.out.println(x);
                });
        holdTheWorld();
    }


    /**
     * bufferUntil中的cutBefore参数作用是分隔的过程不同
     *
     * @throws InterruptedException
     */
    @Test
    public void testBufferUntilCutBefore() throws InterruptedException {
        Flux.just(1, 2, 3, 4, 5, 8, 7, 6).delayElements(Duration.ofMillis(100))
                .bufferUntil(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) {
                        return integer % 2 == 0;
                    }
                }, true)
                .subscribe(x -> {
                    System.out.println(x);
                });
        holdTheWorld();
    }

    /**
     * bufferChanged作用是当发发生变化后把之前的做成一个list
     */
    @Test
    public void testBufferChanged() throws InterruptedException {
        Flux.just(1, 1, 3, 4, 5, 8, 8, 10, 7, 6).delayElements(Duration.ofMillis(100))
                .bufferUntilChanged()
                .subscribe(x -> {
                    System.out.println(x);
                });
        holdTheWorld();
    }

    /**
     * 指定每个元素的标志key
     *
     * @throws InterruptedException
     */
    @Test
    public void testBufferChangedAddKey() throws InterruptedException {
        Flux.just(1, 1, 3, 4, 5, 8, 10, 7, 6).delayElements(Duration.ofMillis(100))
                .bufferUntilChanged(x -> {
                    return x % 2;
                })
                .subscribe(x -> {
                    System.out.println(x);
                });
        holdTheWorld();
    }


    /**
     * bufferwhile 开关控制是否进入到list中
     *
     * @throws InterruptedException
     */
    @Test
    public void testBufferWhile() throws InterruptedException {
        Flux.just(1, 1, 3, 4, 5, 8, 10, 7, 6).delayElements(Duration.ofMillis(100))
                .bufferWhile(x -> {
                    return x % 2 == 0;
                }).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();
    }

    /**
     * 内容有些错乱，但是能够发信其中的运行方式和规律
     *
     * @throws InterruptedException
     */
    @Test
    public void testBufferWhen() throws InterruptedException {
        Flux.just(1, 2, 3, 3, 4, 5).delayElements(Duration.ofMillis(100))
                .bufferWhen(Flux.just("a", "bb", "cc").delayElements(Duration.ofMillis(50)), x -> {
                    return Flux.just(1, 2, 3, 4, 5).delayElements(Duration.ofMillis(500));
                }).subscribe(x -> {
            System.out.println(x);
        });
        holdTheWorld();


    }
}
