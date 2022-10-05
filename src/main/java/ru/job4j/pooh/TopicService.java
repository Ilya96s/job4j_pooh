package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * TopicService - сервис для режима работы Topic, для каждого потребителя существует своя уникальная очередь с данными
 *
 * @author Ilya Kaltygin
 */
public class TopicService implements Service {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topic = new ConcurrentHashMap<>();

    /**
     * Логика работы режима Topic
     * @param req распарсенный запрос в виде объекта типа Req
     * @return ответ от сервиса в виде объекта типа Resp
     */
    @Override
    public Resp process(Req req) {
        String text = "";
        String status = "200";
        if (POST.equals(req.httpRequestType())) {
            for (ConcurrentLinkedQueue<String> queue : topic.getOrDefault(
                    req.getSourceName(), new ConcurrentHashMap<>()).values()) {
                queue.offer(req.getParam());
            }
        } else if (GET.equals(req.httpRequestType())) {
            topic.putIfAbsent(req.getSourceName(), new ConcurrentHashMap<>());
            topic.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            text = topic.get(req.getSourceName()).get(req.getParam()).poll();
            if (text == null) {
                text = "";
                status = "204";
            }
        }
        return new Resp(text, status);
    }
}
