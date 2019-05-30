package by.example.rampant.busshedule;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by - on 05.11.2018.
 */

public class MyFragmentPageAdapter extends FragmentPagerAdapter {

    private static final String TagDel = "%!";
    private final int PAGE_COUNT = 2;
    private Context context;
    private boolean isDayIsWeekday;
    //Log.d(TagDel, "_getItem In FragmentPageAdapter " + position);

    MyFragmentPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        isDayIsWeekday = isDayisWeekday();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return Fragment_route.newInstance(1);
        } else {
            return Fragment_route.newInstance(2);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    // Используется до нажатия
    public View getTabView(int position) {

        // Подключаем свою разметку с компонентами TextView и ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.layout_tab_layout, null);
        TextView textView = (TextView) v.findViewById(R.id.id_textview_in_tablayout);

        if (isDayIsWeekday) {
            if (position == 0) {
                textView.setText(context.getString(R.string.weekday_text));
                textView.setTextColor(context.getResources().getColor(R.color.Color_left_line_child));
            } else if (position == 1) {
                textView.setText(context.getString(R.string.dayoff_text));
                textView.setTextColor(context.getResources().getColor(R.color.Black));
            }
        } else {
            if (position == 0) {
                textView.setText(context.getString(R.string.weekday_text));
                textView.setTextColor(context.getResources().getColor(R.color.Black));
            } else if (position == 1) {
                textView.setText(context.getString(R.string.dayoff_text));
                textView.setTextColor(context.getResources().getColor(R.color.Color_left_line_child));
            }
        }
        return v;
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

}
