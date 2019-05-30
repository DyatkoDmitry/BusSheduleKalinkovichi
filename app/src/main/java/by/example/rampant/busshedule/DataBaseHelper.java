package by.example.rampant.busshedule;

/**
 * Created by - on 23.06.2018.
 */
public class DataBaseHelper {
    private static final String TagDel = "%!";

    public static final String DATABASE_NAME = "databaseShedule.db";
    private static final int SCHEMA = 1;
    public static final String TABLE_201_MOZYR_KALINKOVICHI_WEEKDAY = "_201_MK_Weekday";
    public static final String TABLE_201_MOZYR_KALINKOVICHI_DAYOFF = "_201_MK_Dayoff";
    public static final String TABLE_201_KALINKOVICHI_MOZYR_WEEKDAY = "_201_KM_Weekday";
    public static final String TABLE_201_KALINKOVICHI_MOZYR_DAYOFF = "_201_KM_Dayoff";
    public static final String TABLE_9_AUTOVOKZAL_ZBH_WEEKDAY = "_9_AutoStZBH_Weekday";
    public static final String TABLE_9_AUTOVOKZAL_ZBH_DAYOFF = "_9_AutoStZBH_Dayoff";
    public static final String TABLE_9_ZBH_AUTOVOKZAL_WEEKDAY = "_9_ZBHAustoSt_Weekday";
    public static final String TABLE_9_ZBH_AUTOVOKZAL_DAYOFF = "_9_ZBHAustoSt_Dayoff";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_STATION = "station";
    public static final String COLUMN_ARRAYLIST = "arrayList";

    public static final String ROUTE_9_ZBH_BUSSTATION = "ROUTE_9_ZBH_BUSSTATION";
    public static final String ROUTE_9_BUSSTATION_ZBH = "ROUTE_9_BUSSTATION_ZBH";
    public static final String ROUTE_201_KALINKOVICHI_MOZYR = "ROUTE_201_KALINKOVICHI_MOZYR";
    public static final String ROUTE_201_MOZYR_KALINKOVICHI = "ROUTE_201_MOZYR_KALINKOVICHI";

    public static String DB_PATH = "/data/data/Busshedule/databases/";


    // Номера фотографий расписаний по маршрутам, при добавлении на сайт здесь дописать номер обязательно!
    public static final int[] numbersPhotoGroupViewInRoute_9_ZBH_BUSSTATION = {0, 3, 6, 10};
    public static final int[] numbersPhotoGroupViewInRoute_9_BUSSTATION_ZBH = {0, 5, 10, 13, 16, 20};
    public static final int[] numbersPhotoGroupViewInRoute_201_KALINKOVICHI_MOZYR = {0, 5};
    public static final int[] numbersPhotoGroupViewInRoute_201_MOZYR_KALINKOVICHI = {0, 6, 7};

    public static final String STATE_FULL_BASE = "STATE_FULL_BASE";
    public static final String STATE_DATEs_COPIED = "STATE_DATEs_COPIED";

    // Ссылки на страницы сети,
    // На каждой странице расписание для каждого фрагмента приложения
    public static final String URL_WEB_SITE = "http://getfar.ru";
    public static final String URL_9AutoVokzalZBHWeekdayRoute = "http://getfar.ru/9AutoVokzalZBHWeekdayRoute.html";
    public static final String URL_9AutoVokzalZBHDayOffRoute = "http://getfar.ru/9AutoVokzalZBHDayOffRoute.html";
    public static final String URL_9ZBHAutoVokzalWeekdayRoute = "http://getfar.ru/9ZBHAutoVokzalWeekdayRoute.html";
    public static final String URL_9ZBHAutoVokzalDayOffRoute = "http://getfar.ru/9ZBHAutoVokzalDayOffRoute.html";
    public static final String URL_201MozyrKalinkovichiWeekdayRoute = "http://getfar.ru/201MozyrKalinkovichiWeekdayRoute.html";
    public static final String URL_201MozyrKalinkovichiDayOffRoute = "http://getfar.ru/201MozyrKalinkovichiDayOffRoute.html";
    public static final String URL_201KalinkovichiMozyrWeekdayRoute = "http://getfar.ru/201KalinkovichiMozyrWeekdayRoute.html";
    public static final String URL_201KalinkovichiMozyrDayOffRoute = "http://getfar.ru/201KalinkovichiMozyrDayOffRoute.html";
    public static final String URL_IndexContolDates = "http://getfar.ru/index.html";

    public static final String URL_GROUP_SUPPORT_VK = "https://vk.com/auto_kalinkovichi";

}
