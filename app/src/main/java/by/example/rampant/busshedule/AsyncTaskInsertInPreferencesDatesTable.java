package by.example.rampant.busshedule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

public class AsyncTaskInsertInPreferencesDatesTable extends AsyncTask<String, Void, Boolean> {

    private Context mContext;
    private Boolean isInserted;
    private SharedPreferences sharedPreferences;

    private static final String TagDel = "%!";
    //Log.d(TagDel, "InsertInPreference: onPreExecute");

    AsyncTaskInsertInPreferencesDatesTable(Context mContext) {
        this.mContext = mContext;
        isInserted = false;
        sharedPreferences = mContext.getSharedPreferences("Preferences_Main_Table", Context.MODE_PRIVATE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Перед загрузкой установим флаг в false, в случае неудачи в след. раз заново загрузка данных начнется
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DataBaseHelper.STATE_DATEs_COPIED, false);
        editor.commit();
    }

    @Override
    protected Boolean doInBackground(String... urlOfRoute) {

        try {
            URL url = new URL(urlOfRoute[0]);
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnect.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

            ArrayList<String> arrayListDataFromURL = getArrayListFromOnePage(r);

            SharedPreferences.Editor editor = sharedPreferences.edit();

            for (int i = 0; i < arrayListDataFromURL.size(); i++) {
                String[] StationAndLastTime = arrayListDataFromURL.get(i).split(",", 2);
                editor.putString(StationAndLastTime[0].trim(), StationAndLastTime[1].trim());
                editor.commit();
            }
            isInserted = true;

        } catch (Exception e) {
            e.printStackTrace();
            isInserted = false;
        }

        return isInserted;
    }

    @Override
    protected void onPostExecute(Boolean isInserted) {
        super.onPostExecute(isInserted);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (isInserted) {
            editor.putBoolean(DataBaseHelper.STATE_DATEs_COPIED, true);
            editor.commit();
        } else {
            editor.putBoolean(DataBaseHelper.STATE_DATEs_COPIED, false);
            editor.commit();
        }
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
