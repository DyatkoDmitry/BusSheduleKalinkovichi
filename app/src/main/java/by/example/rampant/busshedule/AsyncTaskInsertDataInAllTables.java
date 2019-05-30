package by.example.rampant.busshedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by - on 13.09.2018.
 */

public class AsyncTaskInsertDataInAllTables extends AsyncTask<Void, Integer, Boolean> {
    private SQLiteDatabase db;
    private Context mContext;
    private Boolean isInserted;
    private SharedPreferences sharedPreferences;

    private static final String TagDel = "%!";
    //Log.d(TagDel, "onPreExecute");

    AsyncTaskInsertDataInAllTables(Context mContext) {
        this.mContext = mContext;
        db = mContext.openOrCreateDatabase("databaseShedule.db", Context.MODE_PRIVATE, null);
        isInserted = false;
        sharedPreferences = mContext.getSharedPreferences("Preferences_Main_Table", Context.MODE_PRIVATE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Запишем флаг stateFullBase false, в случае неудачи, база при след. запуске приложения скачается заново
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DataBaseHelper.STATE_FULL_BASE, false);
        editor.commit();

        MainActivity.linearLayoutLoadData.setVisibility(View.VISIBLE);

        // Сделаем невидимыми кнопки во время загрузки данных
        MainActivity.setGoneViews();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        String[] urlOfRoute = {DataBaseHelper.URL_9AutoVokzalZBHWeekdayRoute, DataBaseHelper.URL_9AutoVokzalZBHDayOffRoute, DataBaseHelper.URL_9ZBHAutoVokzalWeekdayRoute, DataBaseHelper.URL_9ZBHAutoVokzalDayOffRoute, DataBaseHelper.URL_201MozyrKalinkovichiWeekdayRoute, DataBaseHelper.URL_201MozyrKalinkovichiDayOffRoute, DataBaseHelper.URL_201KalinkovichiMozyrWeekdayRoute, DataBaseHelper.URL_201KalinkovichiMozyrDayOffRoute};

        try {
            int colRowsInsert = 0;
            for (String url_string : urlOfRoute) {
                URL url = new URL(url_string);
                HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnect.getInputStream();
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

                ArrayList<String> arrayListDataFromURL = getArrayListFromOnePage(r);

                String nameTable = getNameTable(url_string);

                db.beginTransaction();
                try {
                    db.execSQL("DROP TABLE IF EXISTS " + nameTable);
                    db.execSQL("CREATE TABLE IF NOT EXISTS " + nameTable + " (" + DataBaseHelper.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            DataBaseHelper.COLUMN_STATION + " TEXT, " + DataBaseHelper.COLUMN_ARRAYLIST + " TEXT);");

                    for (int i = 0; i < arrayListDataFromURL.size(); i++) {
                        String al = arrayListDataFromURL.get(i).toString();
                        insertIntoOneRow(al, nameTable);
                        colRowsInsert++;
                        publishProgress(colRowsInsert);
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }
            }

            isInserted = true;

        } catch (Exception e) {
            e.printStackTrace();
            isInserted = false;
        }
        db.close();
        return isInserted;
    }

    @Override
    protected void onPostExecute(Boolean isInserted) {
        super.onPostExecute(isInserted);

        MainActivity.linearLayoutLoadData.setVisibility(View.GONE);

        //Если БД полностью скопирована то тру в преференс
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (isInserted) {

            editor.putBoolean(DataBaseHelper.STATE_FULL_BASE, true);
            editor.commit();

            // Сделаем видимыми кнопки после загрузки
            MainActivity.setVisibleViews();

            Toast toast = Toast.makeText(mContext, mContext.getString(R.string.internet_download_base_successfully), Toast.LENGTH_SHORT);
            toast.show();

        } else {
            editor.putBoolean(DataBaseHelper.STATE_FULL_BASE, false);
            editor.commit();

            Toast toast = Toast.makeText(mContext, mContext.getString(R.string.internet_download_error), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        //Log.d(TagDel, "onProgressUpdate :" + String.valueOf(values[0]));
        for (Integer i : values) {
            MainActivity.mHandler.sendEmptyMessage(i);
        }
    }

    // Узнать название таблицы по ссылке
    private String getNameTable(String urlReference) {

        String nameTable = "";
        urlReference = urlReference.trim();

        switch (urlReference) {
            case "http://getfar.ru/201MozyrKalinkovichiWeekdayRoute.html":
                nameTable = DataBaseHelper.TABLE_201_MOZYR_KALINKOVICHI_WEEKDAY;
                break;
            case "http://getfar.ru/201MozyrKalinkovichiDayOffRoute.html":
                nameTable = DataBaseHelper.TABLE_201_MOZYR_KALINKOVICHI_DAYOFF;
                break;
            case "http://getfar.ru/201KalinkovichiMozyrWeekdayRoute.html":
                nameTable = DataBaseHelper.TABLE_201_KALINKOVICHI_MOZYR_WEEKDAY;
                break;
            case "http://getfar.ru/201KalinkovichiMozyrDayOffRoute.html":
                nameTable = DataBaseHelper.TABLE_201_KALINKOVICHI_MOZYR_DAYOFF;
                break;
            case "http://getfar.ru/9AutoVokzalZBHWeekdayRoute.html":
                nameTable = DataBaseHelper.TABLE_9_AUTOVOKZAL_ZBH_WEEKDAY;
                break;
            case "http://getfar.ru/9AutoVokzalZBHDayOffRoute.html":
                nameTable = DataBaseHelper.TABLE_9_AUTOVOKZAL_ZBH_DAYOFF;
                break;
            case "http://getfar.ru/9ZBHAutoVokzalWeekdayRoute.html":
                nameTable = DataBaseHelper.TABLE_9_ZBH_AUTOVOKZAL_WEEKDAY;
                break;
            case "http://getfar.ru/9ZBHAutoVokzalDayOffRoute.html":
                nameTable = DataBaseHelper.TABLE_9_ZBH_AUTOVOKZAL_DAYOFF;
                break;
        }
        return nameTable;
    }

    // Вставить одну строку в таблицу
    private void insertIntoOneRow(String al, String nameTable) {

        String[] StationAndLine = al.split(",", 2);

        db.execSQL("INSERT INTO " + nameTable + " ( " + DataBaseHelper.COLUMN_STATION + "," + DataBaseHelper.COLUMN_ARRAYLIST + ")" + " VALUES('" + StationAndLine[0] + "','" + StationAndLine[1] + "');");

        Log.d(TagDel, StationAndLine[0]);
    }

    private ArrayList<String> getArrayListFromOnePage(BufferedReader r) {

        String liner = "";
        String regex = "<nobr\\s*?>(.*?)</nobr\\s*?>";
        ArrayList<String> arrayListDataFromURL = new ArrayList<String>();

        try {
            while ((liner = r.readLine()) != null) {
                liner = liner.trim();
                Matcher matcherRegex = Pattern.compile(regex).matcher(liner);
                if (matcherRegex.find()) {
                    arrayListDataFromURL.add(matcherRegex.group(1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

            Toast toast = Toast.makeText(mContext, mContext.getString(R.string.internet_download_error), Toast.LENGTH_SHORT);
            toast.show();

        }
        return arrayListDataFromURL;
    }
}
