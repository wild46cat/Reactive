package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class TestAboutTake {

    private static Flux<Integer> flux = Flux.range(1, 10);
    private static Flux<Integer> flux2 = Flux.range(100, 10).delayElements(Duration.ofMillis(200));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    /**
     * 根据个数获取publisher中的数据
     */
    @Test
    public void testTake() {
        flux.take(3).subscribe(x -> System.out.println(x));
    }

    /**
     * 根据时间获取publisher中的数据
     *
     * @throws InterruptedException
     */
    @Test
    public void testTake2() throws InterruptedException {
        flux2.take(Duration.ofMillis(900)).subscribe(x -> System.out.println(x));
        holdTheWorld();
    }

    /**
     * 取后面的n个数据
     */
    @Test
    public void testTakeLast() {
        flux.takeLast(3).subscribe(x -> System.out.println(x));
    }

    /**
     * 默认获取，直到条件true时，不再获取
     */
    @Test
    public void testTakeUntil() {
        flux.takeUntil(x -> x > 5).subscribe(x -> System.out.println(x));
    }

    @Test
    public void testTakeUntilOther() throws InterruptedException {
        flux2.takeUntilOther(Flux.just("flag").delayElements(Duration.ofMillis(1200)))
                .subscribe(x -> System.out.println(x));
        holdTheWorld();
    }

    /**
     * 默认不take数据，只有当条件满足才take
     */
    @Test
    public void testTakeWhile() {
        flux.takeWhile(x -> x < 3).subscribe(x -> System.out.println(x));
    }
}
