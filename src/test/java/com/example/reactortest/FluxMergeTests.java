package com.example.reactortest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class FluxMergeTests {

    // 시간간격을 두어 두 Flux 요소가 번갈아가면서 나오도록 병합
    @Test
    public void mergeFluxes() {
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500));

        Flux<String> foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);

        StepVerifier.create(mergedFlux)
                .expectNext("Garfield")
                .expectNext("Lasagna")
                .expectNext("Kojak")
                .expectNext("Lollipops")
                .expectNext("Barbossa")
                .expectNext("apples")
                .verifyComplete();
    }

    // zip 활용 merge (번갈아 방출되도록 보장)
    @Test
    public void zipFluxes() {
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa");
        Flux<String> foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples");

        // Tuple2 활용 2개의 다른 객체를 전달
        Flux<Tuple2<String, String>> zippedFlux =
                Flux.zip(characterFlux, foodFlux);

        StepVerifier.create(zippedFlux)
                .expectNextMatches(p ->
                        p.getT1().equals("Garfield") &&
                        p.getT2().equals("Lasagna"))
                .expectNextMatches(p ->
                        p.getT1().equals("Kojak") &&
                        p.getT2().equals("Lollipops"))
                .expectNextMatches(p ->
                        p.getT1().equals("Barbossa") &&
                        p.getT2().equals("Apples"))
                .verifyComplete();
    }

    // 두 Flux를 zip하여 새로운 String 객체 Flux 생성
    @Test
    public void zipFluxesToObject() {
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa");
        Flux<String> foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples");

        Flux<String> zippedFlux =
                Flux.zip(characterFlux, foodFlux, (c, f) -> c + " eats " + f);

        StepVerifier.create(zippedFlux)
                .expectNext("Garfield eats Lasagna")
                .expectNext("Kojak eats Lollipops")
                .expectNext("Barbossa eats Apples")
                .verifyComplete();
    }

    // 먼저 값을 방출하는 Flux 선택
    @Test
    public void firstFlux() {
        Flux<String> slowFlux = Flux.just("tortoise", "snail", "sloth")
                .delaySubscription(Duration.ofMillis(100));
        Flux<String> fastFlux = Flux.just("hare", "cheetah", "squirrel");

        Flux<String> firstFlux = Flux.first(slowFlux, fastFlux);

        StepVerifier.create(firstFlux)
                .expectNext("hare")
                .expectNext("cheetah")
                .expectNext("squirrel")
                .verifyComplete();
    }
}
