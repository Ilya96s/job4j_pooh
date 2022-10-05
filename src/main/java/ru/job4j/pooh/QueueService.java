package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * QueueService - сервис для режима работы Queue, потребители полуают данные из одной и той же очереди
 *
 * @author Ilya Kaltygin
 */
public class QueueService implements Service {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    /**
     * Логика работы режима Queue
     * @param req распарсенный запрос в виде объекта типа Req
     * @return ответ от сервиса в виде объекта типа Resp
     */
    @Override
    public Resp process(Req req) {
        String text = "";
        String status = "200";
        if (POST.equals(req.httpRequestType())) {
            queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            queue.get(req.getSourceName()).offer(req.getParam());
        } else if (GET.equals(req.httpRequestType())) {
            text = queue.getOrDefault(req.getSourceName(), new ConcurrentLinkedQueue<>()).poll();
            if (text == null) {
                text = "";
                status = "204";
            }
        }
        return new Resp(text, status);
    }
}
