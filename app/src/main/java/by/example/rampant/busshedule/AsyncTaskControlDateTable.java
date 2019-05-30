package by.example.rampant.busshedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
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
 * Created by - on 29.08.2018.
 */

public class AsyncTaskControlDateTable extends AsyncTask<String, Void, Boolean> {

    private Context mContext;
    private Boolean isDateMatch;

    private static final String TagDel = "%!";

    //Log.d(TagDel, "ControlMainTable: doInBackground");

    AsyncTaskControlDateTable(Context mContext) {
        this.mContext = mContext;
        isDateMatch = true;
    }

    @Override
    protected Boolean doInBackground(String... urlOfRoute) {

        try {
            URL url = new URL(urlOfRoute[0]);
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnect.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

            ArrayList<String> arrayListDataFromURL = getArrayListFromOnePage(r);

            SharedPreferences sharedPreferences = mContext.getSharedPreferences("Preferences_Main_Table", Context.MODE_PRIVATE);
            String lastTimeOfShrPrf = "";

            for (int i = 0; i < arrayListDataFromURL.size(); i++) {

                String[] StationAndLastTime = arrayListDataFromURL.get(i).split(",", 2);
                lastTimeOfShrPrf = sharedPreferences.getString(StationAndLastTime[0], "").trim();

                if (lastTimeOfShrPrf.equals(StationAndLastTime[1].trim().toString())) {
                    continue;
                } else {
                    isDateMatch = false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            isDateMatch = false;
        }

        return isDateMatch;
    }

    @Override
    protected void onPostExecute(Boolean isDateMatch) {
        super.onPostExecute(isDateMatch);

        //isDateMatch=false;
        if (!isDateMatch) {

            AsyncTaskInsertDataInAllTables asyncTaskInsertDataInAllTables = new AsyncTaskInsertDataInAllTables(mContext);
            asyncTaskInsertDataInAllTables.execute();

            AsyncTaskInsertInPreferencesDatesTable asyncTaskInsertInPreferencesDatesTable = new AsyncTaskInsertInPreferencesDatesTable(mContext);
            asyncTaskInsertInPreferencesDatesTable.execute(DataBaseHelper.URL_IndexContolDates);

        }
    }

    private ArrayList<String> getArrayListFromOnePage(BufferedReader r) {

        String liner = "";
        String regex = "<nobr\\s*?>(.*?)</nobr\\s*?>";
        ArrayList<String> arrayListDataFromURL = new ArrayList<String>();

        try {
            while ((liner = r.readLine()) != null) {

                liner = liner.trim();

                // Считывание только строк подряд, если пробел в строке ты сброс. Сделано для скорости, чтобы не считывать каждый раз весь html
                if(liner.length()==0){
                    break;
                }

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
