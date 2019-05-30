package by.example.rampant.busshedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by - on 13.10.2018.
 */
public class MyBaseAdapter extends BaseAdapter {

    //Log.d(TagDel, "int position, View convertView, ViewGroup parent: " + position + ", " + convertView + ", " + parent);

    private static final String TagDel = "%!";

    private String[] dataTime;
    private Context context;
    private Integer positionSelectedUser;
    private int positionSelectedNextTrip;

    MyBaseAdapter(Context mContext, String[] mDataTime, Integer mPositionSelectedUser) {
        context = mContext;
        dataTime = mDataTime;
        positionSelectedUser = mPositionSelectedUser;
        positionSelectedNextTrip = getPositionSelectedNextTrip();
    }

    @Override
    public int getCount() {
        return dataTime.length;
    }

    @Override
    public Object getItem(int position) {
        return dataTime[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_text, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.id_textView_single);
        textView.setText(getItem(position).toString());

        // Выделим пункт следующего рейса
        if ((positionSelectedNextTrip != -1) & (positionSelectedNextTrip == position)) {
            textView.setBackgroundResource(R.drawable.background_next_bus);
        }

        // Проверка на выделенный пункт
        if (positionSelectedUser != null && (position == positionSelectedUser)) {
            textView.setBackgroundColor(context.getResources().getColor(R.color.Color_selected_item));
        }

        return textView;
    }

    // Для нахождения номера след. рейса
    private int getPositionSelectedNextTrip() {

        // Для сравнения текущего времени и расписания
        GregorianCalendar gregorianCalendarNextTrip = new GregorianCalendar();
        GregorianCalendar gregorianCalendarCurrent = new GregorianCalendar();

        // Если текущее время больше 23 и меньше 04 часа, то ближайшее время на автобус выделяться не будет. Передадим -1
        int hourCurrent = gregorianCalendarCurrent.get(Calendar.HOUR_OF_DAY);
        if ((hourCurrent >= 23) | (hourCurrent <= 4)) {
            return -1;
        }

        int indexNextTrip = 0;
        Pattern p = Pattern.compile("(\\d{2}):(\\d{2})");
        int i = 0;

        // Пробежимся по все данным расписания оставновки
        while (i < dataTime.length) {

            Matcher m = p.matcher(dataTime[i]);
            if (m.find()) {

                String hourString = m.group(1);
                String minuteString = m.group(2);

                int hour = Integer.parseInt(hourString);
                int minute = Integer.parseInt(minuteString);

                gregorianCalendarNextTrip.set(Calendar.HOUR_OF_DAY, hour);
                gregorianCalendarNextTrip.set(Calendar.MINUTE, minute);

                if (gregorianCalendarCurrent.after(gregorianCalendarNextTrip)) {
                    i++;
                    continue;
                } else {
                    indexNextTrip = i;
                    break;
                }
            }
            i++;
        }
        return indexNextTrip;
    }
}
