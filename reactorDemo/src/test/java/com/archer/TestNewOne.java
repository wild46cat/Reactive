package com.archer;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TestNewOne {
    /**
     * 判断是否所有的都满足指定条件
     */
    @Test
    public void test_all() {
        Mono<Boolean> all = Flux.fromIterable(Arrays.asList(1, 2, 3, 4))
                .all(i -> {
                    return i > 0;
                });
        all.subscribe(x -> System.out.println(x));
    }

    /**
     * 判断是否有一个满足条件
     */
    @Test
    public void test_any() {
        Mono<Boolean> all = Flux.fromIterable(Arrays.asList(1, 2, 3, 4))
                .any(i -> {
                    return i == 3;
                });
        all.subscribe(x -> System.out.println(x));
    }

    /**
     * as可以转换成非Flux或者Mono的普通对象
     */
    @Test
    public void test_as() {
        Flux<String> flux = Flux.fromIterable(Arrays.asList("a", "BB", "cc"));
        Stream<String> as = flux.as(stringFlux -> {
            return stringFlux.toStream();
        });
        System.out.println(as.count());
    }
}
