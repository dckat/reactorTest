package com.example.reactortest;

import lombok.Data;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class FluxTransformTests {

    // 일정 갯수만큼 skip
    @Test
    public void skipAFew() {
        Flux<String> skipFlux = Flux.just(
                "one", "two", "skip a few", "ninety nine", "one hundred")
                .skip(3);

        StepVerifier.create(skipFlux)
                .expectNext("niety nine", "one hundred")
                .verifyComplete();
    }

    // 일정 시간만큼 skip
    @Test
    public void skipAFewFewSecond() {
        Flux<String> skipFlux = Flux.just(
                "one", "two", "skip a few", "ninety nine", "one hundred")
                .delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofSeconds(4));

        StepVerifier.create(skipFlux)
                .expectNext("ninety nine", "one hundred")
                .verifyComplete();
    }

    // 일정 갯수만큼 take
    @Test
    public void take() {
        Flux<String> nationalParkFlux = Flux.just(
                "YellowStone", "Yosemite", "Grand Canyon",
                "Zion", "Grand Teton")
                .delayElements(Duration.ofSeconds(1))
                        .take(Duration.ofMillis(3500));

        StepVerifier.create(nationalParkFlux)
                .expectNext("YellowStone", "Yosemite", "Grand Canyon")
                .verifyComplete();
    }

    // filter 활용 Flux 필터링
    @Test
    public void filter() {
        Flux<String> nationalParkFlux = Flux.just(
                "YellowStone", "Yosemite", "Grand Canyon",
                "Zion", "Grand Teton")
                .filter(np -> !np.contains(" "));

        StepVerifier.create(nationalParkFlux)
                .expectNext("YellowStones", "Yosemite", "Zion")
                .verifyComplete();
    }

    // distinct 활용 데이터 중복 제거
    @Test
    public void distinct() {
        Flux<String> animalFlux = Flux.just(
                "dog", "cat", "bird", "dog", "bird", "anteater")
                .distinct();

        StepVerifier.create(animalFlux)
                .expectNext("dog", "cat", "bird", "aneater")
                .verifyComplete();
    }

    // 데이터 매핑 (map)
    @Test
    public void map() {
        Flux<Player> playerFlux = Flux
                .just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .map(n -> {
                    String[] split = n.split("\\s");
                    return new Player(split[0], split[1]);
                });

        StepVerifier.create(playerFlux)
                .expectNext(new Player("Michael", "Jordan"))
                .expectNext(new Player("Scottie", "Pippen"))
                .expectNext(new Player("Steve", "Kerr"))
                .verifyComplete();

    }

    // 데이터를 비동기적으로 매핑 (flatMap)
    @Test
    public void flatMap() {
        Flux<Player> playerFlux = Flux
                .just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
                .flatMap(n -> Mono.just(n)
                        .map(p -> {
                            String[] split = p.split("\\s");
                            return new Player(split[0], split[1]);
                        })
                        .subscribeOn(Schedulers.parallel())
                );

        List<Player> playerList = Arrays.asList(
                new Player("Michael", "Jordan"),
                new Player("Scottie", "Pippen"),
                new Player("Steve", "Kerr"));

        StepVerifier.create(playerFlux)
                .expectNextMatches(p -> playerList.contains(p))
                .expectNextMatches(p -> playerList.contains(p))
                .expectNextMatches(p -> playerList.contains(p))
                .verifyComplete();
    }

    @Data
    private static class Player {
        private final String firstName;
        private final String lastName;
    }
}
