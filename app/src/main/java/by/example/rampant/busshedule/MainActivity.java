package by.example.rampant.busshedule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TagDel = "%!";

    public static Button button_201_Mozyr_Kalchi, button_201_Kalchi_Mozyr, button_9_BusStation_ZBH, button_9_ZBH_BusStation;
    public ProgressBar mProgressBar;
    public static Handler mHandler;
    static LinearLayout linearLayoutLoadData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        initializationViews();

        if (isBaseFull() && isDatesCopied() && isOnline(getApplicationContext())) {

            // AsyncTask для контроля наличия актуальных данных, по датам
            AsyncTaskControlDateTable asyncTaskControlDateTable = new AsyncTaskControlDateTable(getApplicationContext());
            asyncTaskControlDateTable.execute(DataBaseHelper.URL_IndexContolDates);

        } else if (isOnline(getApplicationContext())) {
            copyedAllData();
        } else if (!isBaseFull()) {
            setGoneViews();
            Toast toast = Toast.makeText(this, getString(R.string.internet_download_all_and_restart_app), Toast.LENGTH_LONG);
            toast.show();
        }


        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mProgressBar.setProgress(msg.what);
            }
        };
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.idButton201MozyrKalnkvch:
                Intent intent201MK = new Intent(this, Activity_201_Mozyr_Kalinkovichi.class);
                startActivity(intent201MK);
                break;

            case R.id.idButton201KalnkvchMozyr:
                Intent intent201KM = new Intent(this, Activity_201_Kalinkovichi_Mozyr.class);
                startActivity(intent201KM);
                break;

            case R.id.idButton9BusStationZBH:
                Intent intent9BusSZBH = new Intent(this, Activity_9_BusStation_ZBH.class);
                startActivity(intent9BusSZBH);
                break;

            case R.id.idButton9ZBHBusStation:
                Intent intent9ZBHBusS = new Intent(this, Activity_9_ZBH_BusStation.class);
                startActivity(intent9ZBHBusS);
                break;

            default:
                break;
        }
    }

    private void initializationViews() {

        button_201_Mozyr_Kalchi = (Button) findViewById(R.id.idButton201MozyrKalnkvch);
        button_201_Mozyr_Kalchi.setOnClickListener(this);

        button_201_Kalchi_Mozyr = (Button) findViewById(R.id.idButton201KalnkvchMozyr);
        button_201_Kalchi_Mozyr.setOnClickListener(this);

        button_9_BusStation_ZBH = (Button) findViewById(R.id.idButton9BusStationZBH);
        button_9_BusStation_ZBH.setOnClickListener(this);

        button_9_ZBH_BusStation = (Button) findViewById(R.id.idButton9ZBHBusStation);
        button_9_ZBH_BusStation.setOnClickListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.idProgressBar);
        mProgressBar.setMax(120);

        linearLayoutLoadData = (LinearLayout) findViewById(R.id.idlinearLayoutLoadData);

        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar_main_activity);
        setSupportActionBar(toolbar);

    }

    // Проверка наличия базы данных по флагу stateFullBase
    private boolean isBaseFull() {
        SharedPreferences sharedPreferences = getSharedPreferences("Preferences_Main_Table", MODE_PRIVATE);

        if (sharedPreferences.getBoolean(DataBaseHelper.STATE_FULL_BASE, false)) {
            return true;
        } else {
            return false;
        }
    }

    // Проверка скопирована ли таблица обновлений Main table
    private boolean isDatesCopied() {
        SharedPreferences sharedPreferences = getSharedPreferences("Preferences_Main_Table", MODE_PRIVATE);

        if (sharedPreferences.getBoolean(DataBaseHelper.STATE_DATEs_COPIED, false)) {
            return true;
        } else {
            return false;
        }
    }

    // Копирование всех данных
    private void copyedAllData() {
        try {

            AsyncTaskInsertDataInAllTables asyncTaskInsertDataInAllTables = new AsyncTaskInsertDataInAllTables(getApplicationContext());
            asyncTaskInsertDataInAllTables.execute();

            AsyncTaskInsertInPreferencesDatesTable asyncTaskInsertInPreferencesDatesTable = new AsyncTaskInsertInPreferencesDatesTable(getApplicationContext());
            asyncTaskInsertInPreferencesDatesTable.execute(DataBaseHelper.URL_IndexContolDates);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void setVisibleViews() {
        button_201_Mozyr_Kalchi.setVisibility(View.VISIBLE);
        button_201_Kalchi_Mozyr.setVisibility(View.VISIBLE);
        button_9_BusStation_ZBH.setVisibility(View.VISIBLE);
        button_9_ZBH_BusStation.setVisibility(View.VISIBLE);
    }

    static void setGoneViews() {
        button_201_Mozyr_Kalchi.setVisibility(View.GONE);
        button_201_Kalchi_Mozyr.setVisibility(View.GONE);
        button_9_BusStation_ZBH.setVisibility(View.GONE);
        button_9_ZBH_BusStation.setVisibility(View.GONE);
    }

    // Проверка наличия подключения к сети
    public static boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }

        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }

        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idItem = item.getItemId();

        switch (idItem) {
            case R.id.scheme_routes:
                // На сервере папка SCHEME_ROUTES/0/
                showPhoto("SCHEME_ROUTES", 0);
                break;

            case R.id.download_data:
                if (isOnline(this)) {
                    copyedAllData();
                } else {
                    Toast toast = Toast.makeText(this, getString(R.string.internet_to_connect), Toast.LENGTH_LONG);
                    toast.show();
                }

                break;

            case R.id.marshrutka_9_autovkzl_zbh:
                // На сервере папка MARCH_9_AUTOVOKZAL_ZBH/0/
                showPhoto("MARCH_9_AUTOVOKZAL_ZBH", 0);
                break;

            case R.id.about_application:

                FragmentManager fragmentManager = getSupportFragmentManager();
                DialogFragmentAboutApp dialogFragmentAboutApp = new DialogFragmentAboutApp();
                dialogFragmentAboutApp.show(fragmentManager, "dialogFragmentAboutApplication");

                break;

            case R.id.about_help:

                FragmentManager fragmentManager2 = getSupportFragmentManager();
                DialogFragmentAboutHelp dialogFragmentAboutHelp = new DialogFragmentAboutHelp();
                dialogFragmentAboutHelp.show(fragmentManager2,"dialogFragmentAboutHelp");

                break;
        }

        return true;
    }

    private void showPhoto(String nameActivity, int groupPosition) {

        Intent intentShowPhoto = new Intent(this, Activity_Show_Photo_Shedule.class);
        intentShowPhoto.putExtra(Activity_Show_Photo_Shedule.groupPosition, groupPosition);
        intentShowPhoto.putExtra(Activity_Show_Photo_Shedule.nameActivity, nameActivity);
        startActivity(intentShowPhoto);
    }
}
