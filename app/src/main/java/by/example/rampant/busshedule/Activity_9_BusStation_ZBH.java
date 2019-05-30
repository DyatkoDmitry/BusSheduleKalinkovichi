package by.example.rampant.busshedule;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

// В коде Активити незабудь поменять название активити toolBar
public class Activity_9_BusStation_ZBH extends AppCompatActivity implements interfaceGetArrayListArray {
    private static final String TagDel = "%!";

    //Log.d(TagDel, "Tab getUnselectPosition: " + tab.getPosition());

    // У каждой Активити свои две таблицы для данных
    /*private static String tableWeekday = DataBaseHelper.TABLE_9_ZBH_AUTOVOKZAL_WEEKDAY;
    private static String tableDayOff = DataBaseHelper.TABLE_9_ZBH_AUTOVOKZAL_DAYOFF;
    private static String nameActivity = DataBaseHelper.ROUTE_9_ZBH_BUSSTATION;*/

    private static String tableWeekday = DataBaseHelper.TABLE_9_AUTOVOKZAL_ZBH_WEEKDAY;
    private static String tableDayOff = DataBaseHelper.TABLE_9_AUTOVOKZAL_ZBH_DAYOFF;
    private static String nameActivity = DataBaseHelper.ROUTE_9_BUSSTATION_ZBH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_route);

        // Находим toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);

        // Заголовок активити
        TextView textToolbar = (TextView) toolbar.findViewById(R.id.id_textview_in_toolbar);
        textToolbar.setText(R.string._9_Autovkz_ZBH);

        //Устанавливаем toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // Получаем ViewPager и устанавливаем в него адаптер
        final ViewPager viewPager = (ViewPager) findViewById(R.id.id_view_pager);
        final MyFragmentPageAdapter myFragmentPageAdapter = new MyFragmentPageAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(myFragmentPageAdapter);

        // Передаём ViewPager в TabLayout
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.id_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // Установка view tabLayout до нажатия
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(myFragmentPageAdapter.getTabView(i));
        }

        if (isDayisWeekday()) {
            tabLayout.getTabAt(0).select();
        } else {
            tabLayout.getTabAt(1).select();
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                View v = tab.getCustomView();
                TextView textView = (TextView) v.findViewById(R.id.id_textview_in_tablayout);
                textView.setTextColor(getResources().getColor(R.color.Color_left_line_child));

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                View v = tab.getCustomView();
                TextView textView = (TextView) v.findViewById(R.id.id_textview_in_tablayout);
                textView.setTextColor(getResources().getColor(R.color.Black));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    // Получим данные для маршрута в будние дни
    @Override
    public ArrayList<ArrayList<String>> getArrayListArrayListWeekday() {
        return getArrayListOfArrayList(tableWeekday);
    }

    // Получим данные для маршрута в выходные дни
    @Override
    public ArrayList<ArrayList<String>> getArrayListArrayListDayOff() {
        return getArrayListOfArrayList(tableDayOff);
    }


    // Данные из таблицы с двумя колонками COLUMN_STATION и COLUMN_ARRAYLIST
    private ArrayList<ArrayList<String>> getArrayListOfArrayList(String tableSqlite) {

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("databaseShedule.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableSqlite, null);

        // Создаем лист всех данных
        ArrayList<ArrayList<String>> arrayListsOfArrayList = new ArrayList<ArrayList<String>>(cursor.getCount());

        cursor.moveToFirst();
        cursor.moveToPrevious();
        while (cursor.moveToNext()) {

            int columnIdStation = cursor.getColumnIndex(DataBaseHelper.COLUMN_STATION);
            String station = cursor.getString(columnIdStation);

            int columnIdarrayTime = cursor.getColumnIndex(DataBaseHelper.COLUMN_ARRAYLIST);
            String arrayTime = cursor.getString(columnIdarrayTime);

            // для хренения одного элемента
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add(station);
            arrayList.add(arrayTime);
            arrayListsOfArrayList.add(arrayList);
        }
        return arrayListsOfArrayList;
    }

    private boolean isDayisWeekday() {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        int dayOfWeek = gregorianCalendar.get(Calendar.DAY_OF_WEEK);

        if ((dayOfWeek != 7) && (dayOfWeek != 1)) {
            return true;
        } else {
            return false;
        }
    }

    // Обработчик стрелки назад
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public String getNameActivity() {
        return nameActivity;
    }

    @Override
    public Context getContext() {
        return this;
    }
}






