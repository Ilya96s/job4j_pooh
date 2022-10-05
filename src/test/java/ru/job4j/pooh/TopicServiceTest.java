package ru.job4j.pooh;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TopicServiceTest {

    @Test
    public void whenTopic() {
        TopicService topicService = new TopicService();
        String paramForPublisher = "temperature=18";
        String paramForSubscriber1 = "client407";
        String paramForSubscriber2 = "client6565";
        /* Режим topic. Подписываемся на топик weather. client407. */
        topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Добавляем данные в топик weather. */
        topicService.process(
                new Req("POST", "topic", "weather", paramForPublisher)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client407. */
        Resp result1 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber1)
        );
        /* Режим topic. Забираем данные из индивидуальной очереди в топике weather. Очередь client6565.
        Очередь отсутствует, т.к. еще не был подписан - получит пустую строку */
        Resp result2 = topicService.process(
                new Req("GET", "topic", "weather", paramForSubscriber2)
        );
        assertThat(result1.text()).isEqualTo("temperature=18");
        assertThat(result2.text()).isEqualTo("");
    }

    @Test
    public void whenTopicAll() {
        TopicService topicService = new TopicService();
        String paramForPublishers = "temperature=25";
        String pub1 = "client01";
        String pub2 = "client02";
        topicService.process(
                new Req("GET", "topic", "weather", pub1)
        );

        topicService.process(
                new Req("GET", "topic", "weather", pub2)
        );

        topicService.process(
                new Req("POST", "topic", "weather", paramForPublishers)
        );

        Resp r1 = topicService.process(
                new Req("GET", "topic", "weather", pub1)
        );
        Resp r2 = topicService.process(
                new Req("GET", "topic", "weather", pub2)
        );

        assertThat(r1.text()).isEqualTo("temperature=25");
        assertThat(r2.text()).isEqualTo("temperature=25");
    }

    @Test
    void whenTextIsNull() {
        TopicService topicService = new TopicService();
        String paramForPublishers = "temperature=25";
        String pub1 = "client01";
        String pub2 = "client02";
        topicService.process(
                new Req("GET", "topic", "weather", pub2)
        );
        Resp r1 = topicService.process(
                new Req("GET", "topic", "weather", pub1)
        );
        assertThat(r1.text()).isEqualTo("");
        assertThat(r1.status()).isEqualTo("204");
    }

}