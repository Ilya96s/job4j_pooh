package ru.job4j.pooh;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class QueueServiceTest {

    @Test
    public void whenPostThenGetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod = "temperature=18";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod)
        );
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text()).isEqualTo("temperature=18");
    }

    @Test
    public void whenNoData() {
        QueueService queueService = new QueueService();
        Resp result = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result.text()).isEqualTo("");
        assertThat(result.status()).isEqualTo("204");
    }

    @Test
    public void when2PostThen2GetQueue() {
        QueueService queueService = new QueueService();
        String paramForPostMethod1 = "temperature=18";
        String paramForPostMethod2 = "temperature=25";
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod1)
        );
        queueService.process(
                new Req("POST", "queue", "weather", paramForPostMethod2)
        );
        Resp result1 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        Resp result2 = queueService.process(
                new Req("GET", "queue", "weather", null)
        );
        assertThat(result1.text()).isEqualTo("temperature=18");
        assertThat(result2.text()).isEqualTo("temperature=25");
    }
}