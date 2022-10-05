package ru.job4j.pooh;

/**
 * Сервис
 *
 * @author Ilya Kaltygin
 */
public interface Service {
    /**
     * Логика работы сервиса
     * @param req распарсенный запрос в виде объекта типа Req
     * @return ответ от сервиса в виде объекта типа Resp
     */
    Resp process(Req req);
}
