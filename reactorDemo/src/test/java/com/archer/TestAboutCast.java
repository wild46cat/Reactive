package com.archer;

import lombok.Data;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class TestAboutCast {

    private static Flux<c2> flux = Flux.fromIterable(Arrays.asList(
            new c2(1, "red"),
            new c2(2, "blue"),
            new c2(3, "black"))).delayElements(Duration.ofSeconds(1));

    public static void holdTheWorld() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }


    /**
     * 类型转换
     *
     * @throws InterruptedException
     */
    @Test
    public void testCase() throws InterruptedException {
        flux.cast(c1.class).subscribe(x -> {
            x.getColor();
        });
        holdTheWorld();
    }
}

interface color {
    public void getColor();
}

class c1 implements color {
    public int mycolor;

    public c1(int mycolor) {
        this.mycolor = mycolor;
    }

    @Override
    public void getColor() {
        System.out.println(mycolor);
    }
}

class c2 extends c1 {
    public String colorName;

    public c2(int mycolor, String colorName) {
        super(mycolor);
        this.colorName = colorName;
    }
}