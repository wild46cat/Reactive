package com.archer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Main {
    public static void main(String[] args) {
        Request request = Request.builder().requestId(1).name("abc").value1("value1").value2("value2").build();
        Flux.just(request).flatMap(x -> {
            Flux<String> stringFlux = Flux.fromArray(x.getName().split(""));
            return stringFlux;
        }).subscribe(x -> {
            System.out.println(x);
        });
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class Request {
    private int requestId;
    private String name;
    private String value1;
    private String value2;
}
