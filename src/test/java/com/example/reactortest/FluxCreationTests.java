package com.example.reactortest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FluxCreationTests {

    // 객체로 생성
    @Test
    public void createFlux_just() {
        Flux<String> fruitFlux = Flux
                .just("Apple", "Orange", "Grape", "Banana", "Strawberry");

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    // 배열로 생성
    @Test
    public void createFlux_fromArray() {
        String[] fruits = new String [] {
                "Apple", "Orange", "Grape", "Banana", "Strawberry"
        };

        Flux<String> fruitFlux = Flux.fromArray(fruits);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    // List 생성
    @Test
    public void createFlux_fromIterable() {
        List<String> fruitList = new ArrayList<>();

        fruitList.add("Apple");
        fruitList.add("Orange");
        fruitList.add("Grape");
        fruitList.add("Banana");
        fruitList.add("Strawberry");

        Flux<String> fruitFlux = Flux.fromIterable(fruitList);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    // 스트림으로 생성
    @Test
    public void createFlux_fromStream() {
        Stream<String> fruitStream =
                Stream.of("Apple", "Orange", "Grape", "Banana", "Strawberry");

        Flux<String> fruitFlux = Flux.fromStream(fruitStream);

        StepVerifier.create(fruitFlux)
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    // 카운터 Flux 생성 (range)
    @Test
    public void createFlux_range() {
        Flux<Integer> intervalFlux =
                Flux.range(1, 5);

        StepVerifier.create(intervalFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    // 카운터 Flux 생성 (interval)
    @Test
    public void createFlux_interval() {
        Flux<Long> intervalFlux =
                Flux.interval(Duration.ofSeconds(1))
                        .take(5);

        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }
}