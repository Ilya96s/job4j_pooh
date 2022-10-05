package ru.job4j.pooh;

/**
 * Req - парсинг входящего запроса
 *
 * httpRequestType - Тип запроса GET или POST
 * poohMode - Указываем режим работы: queue или topic
 * sourceName - Имя очереди или топика
 * param - Содержимое запроса
 *
 * @author Ilya Kaltygin
 */
public class Req {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    /**
     * Создание объекта Req из полученных данных входящего запроса
     * @param content
     * @return
     */
    public static Req of(String content) {
        String[] contentArray = content(content);
        return new Req(contentArray[0], contentArray[1], contentArray[2], contentArray[3]);
    }

    /**
     * Парсинг данных
     * @param content входящий запрос
     * @return массив с данными входящего запроса
     */
    private static String[] content(String content) {
        String[] rsl = new String[4];
        String[] contentArray = content.split("/", 4);
        String[] sourceName = contentArray[2].split(" ");
        rsl[0] = contentArray[0].trim();
        rsl[1] = contentArray[1];
        rsl[2] = sourceName[0];

        if (POST.equals(rsl[0]) && "queue".equals(rsl[1])) {
            String[] param = contentArray[3].split(System.lineSeparator());
            rsl[3] = param[param.length - 1];
        }

        if (POST.equals(rsl[0]) && "topic".equals(rsl[1])) {
            String[] param = contentArray[3].split(System.lineSeparator());
            rsl[3] = param[param.length - 1];
        }

        if (GET.equals(rsl[0]) && "queue".equals(rsl[1])) {
            rsl[3] = "";
        }

        if (GET.equals(rsl[0]) && "topic".equals(rsl[1])) {
            String[] param = contentArray[3].split(" ");
            rsl[3] = param[0];
        }
        return rsl;
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }

}
