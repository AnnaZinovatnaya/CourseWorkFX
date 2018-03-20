package util;

public class ErrorMessage
{
    public static final String FILE_NOT_FOUND          = "Файл configuration.txt не найден!";
    public static final String CANNOT_READ_FILE        = "Невозможно прочитать configuration.txt!";
    public static final String CANNOT_CONNECT_TO_DB    = "Невозможно подключиться к базе данных!";
    public static final String CANNOT_CLOSE_CONNECTION = "Невозможно закрыть базу данных!";
    public static final String BAD_CONF_FILE           = "Некорректный файл конфигурации (configuration.txt)!";
    public static final String CANNOT_EXECUTE_QUERY    = "Невозможно выполнить запрос: ";
    public static final String EMPTY_FIELDS            = "Все поля должны быть заполнены!";
    public static final String WRONG_LOGIN_OR_PASSWORD = "Неверное имя или пароль!";
    public static final String USER_ALREADY_EXISTS     = "Пользователь с таким именем уже есть!";
    public static final String USER_DOES_NOT_EXIST     = "Такого пользователя нет!";
    public static final String EMPTY_USER_CHOICE       = "Выберите пользователя для удаления!";
    public static final String INCORRECT_MASS          = "Масса задана некорректно!";
    public static final String INCORRECT_DELTA_MASS    = "Отклонение по массе задано некорректно!";
    public static final String INCORRECT_MIN_PERCENT   = "Минимальный процент задан некорректно!";
    public static final String INCORRECT_MAX_PERCENT   = "Максимальный процент задан некорректно!";
    public static final String MIN_BIGGER_THAN_MAX     = "Минимальный процен не может быть больше максимального!";
    public static final String EMPTY_COMPONENT_CHOICE  = "Ни один компонент не выбран!";
}
