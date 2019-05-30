package by.example.rampant.busshedule;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by - on 11.10.2018.
 */
public class MyExpandableAdapter extends BaseExpandableListAdapter {

    //Log.d(TagDel, "getGroupView null " + groupPosition);

    private static final String TagDel = "%!";

    private ArrayList<ArrayList<String>> mArrayListsOfarrayList;
    private Context mContext;
    private Integer positionSelectedUser;
    private FrameLayout frameLayoutLeft;
    private String nameActivity;
    private int[] numbersPhotosGroupView;

    MyExpandableAdapter(ArrayList<ArrayList<String>> arrayLists, Context context, String mNameActivity) {
        mArrayListsOfarrayList = arrayLists;
        mContext = context;
        positionSelectedUser = null;
        frameLayoutLeft = null;
        nameActivity = mNameActivity;
        numbersPhotosGroupView = getNumbersPhotoGroupView(nameActivity);
    }

    @Override
    public int getGroupCount() {
        return mArrayListsOfarrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mArrayListsOfarrayList.get(groupPosition).get(0);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mArrayListsOfarrayList.get(groupPosition).get(1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_group_item_with_imagebutton, parent, false);
        }

        // Показывать ли кнопку фотографии расписания?
        ImageButton imageButton = (ImageButton) convertView.findViewById(R.id.id_imageButton_show_photo);
        imageButton.setVisibility(View.GONE);

        for (int numberGroupView : numbersPhotosGroupView) {
            if (numberGroupView == groupPosition) {
                imageButton.setVisibility(View.VISIBLE);
                final int mGroupPosition = groupPosition;
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showPhotoShedule(mGroupPosition);

                    }
                });
                // Если нашли номер группы в фотографиях, то дальше можно цикл не прогонять. break
                break;
            }
        }

        TextView textView = (TextView) convertView.findViewById(R.id.id_tvText_group);
        textView.setText(getGroup(groupPosition).toString());

        TextView textViewNumberStation = (TextView) convertView.findViewById(R.id.id_textView_GroupView_NumberStation);
        textViewNumberStation.setText(String.valueOf(groupPosition + 1));

        /*if (isExpanded) {
            //Изменяем что-нибудь, если текущая Group раскрыта
        } else {
            //Изменяем что-нибудь, если текущая Group скрыта
        }*/

        return convertView;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        // Список данных
        String dataTime[] = ((String) getChild(groupPosition, childPosition)).split(",");

        // Адаптер данных
        final MyBaseAdapter mybaseadapter = new MyBaseAdapter(mContext, dataTime, positionSelectedUser);

        // Создадим ChildView
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_child_item, parent, false);
            // Установим высоту всего childLayout
            //setHeightConvertView(dataTime.length, convertView);
        }

        // Установим высоту всего childLayout (метод-фабрика для высот т.к. высоты повторяются)
        setHeightConvertView(dataTime.length, convertView, mContext);

        // Найдем грид вью
        final GridView gridView = (GridView) convertView.findViewById(R.id.id_gvMain);

        // Установим ширину одной колонки на основании ячейки GridView
        gridView.setColumnWidth(getLayoutParamsTextView(mContext).width);
        gridView.setAdapter(mybaseadapter);

        // Найдем номер нажатого элемента и обновим адаптер
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                positionSelectedUser = position;

                notifyDataMyExpandableAdapter();

            }
        });


        // Оставим или уберем полосу слева
        if (!isGroupLast(groupPosition)) {
            frameLayoutLeft = (FrameLayout) convertView.findViewById(R.id.id_FrameLayout_in_ChildLayout);
            frameLayoutLeft.setBackground(mContext.getResources().getDrawable(R.drawable.bitmap_for_child));
        } else {
            frameLayoutLeft = (FrameLayout) convertView.findViewById(R.id.id_FrameLayout_in_ChildLayout);
            frameLayoutLeft.setBackground(null);
        }

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private static final void setHeightConvertView(int arrayLength, View convertView, Context mContext) {

        // Определим длинну и ширину экрана устройства
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int widthDisplay = display.getWidth();

        // Определим длинну и ширину TextView(ячейки таблицы)
        ViewGroup.LayoutParams layoutParamsOneTextView = getLayoutParamsTextView(mContext);
        int widthTextView = layoutParamsOneTextView.width;
        int heightTextView = layoutParamsOneTextView.height;

        // Параметры полосы слева childLayout
        FrameLayout frameLayout = (FrameLayout) convertView.findViewById(R.id.id_FrameLayout_in_ChildLayout);
        ViewGroup.LayoutParams layoutParamsframeLayoutLeft = frameLayout.getLayoutParams();

        //Кол-во TextView в одной строке
        int countTextViewInOneRow = (widthDisplay - layoutParamsframeLayoutLeft.width) / widthTextView;
        //Кол-во строк в ChildLayout
        int countLineInAllRows = arrayLength / countTextViewInOneRow;

        // Вычислим остаток для дополнительной строки, если нужно
        int remainder = arrayLength % countTextViewInOneRow;
        if (remainder != 0) {
            countLineInAllRows++;
        }

        ViewGroup.LayoutParams layoutParamsConvertView = convertView.getLayoutParams();

        // Высоту child высчитаем кол-во строк*высоту одного элемента и т.к.verticalSpacing 1px добавляем кол-во пикселей равное кол-ву строк
        layoutParamsConvertView.height = (countLineInAllRows * heightTextView) + countLineInAllRows;
    }

    private static final ViewGroup.LayoutParams getLayoutParamsTextView(Context mContext) {

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        FrameLayout frameLayout = new FrameLayout(mContext);
        frameLayout.setLayoutParams(layoutParams);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View textView = inflater.inflate(R.layout.layout_text, frameLayout, false);
        ViewGroup.LayoutParams layoutParamsTextView = textView.getLayoutParams();

        return layoutParamsTextView;
    }

    public void notifyDataMyExpandableAdapter() {

        try {
            this.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
            //Если сгенерируется ошибка, то скорее всего адаптер обновляется myBroadcastReceiverTimeTick
        }
    }

    private boolean isGroupLast(int groupPosition) {
        if (groupPosition == (getGroupCount() - 1)) {
            return true;
        } else {
            return false;
        }
    }

    private void showPhotoShedule(int mGroupPosition) {

        Intent intentShowPhoto = new Intent(mContext, Activity_Show_Photo_Shedule.class);
        intentShowPhoto.putExtra(Activity_Show_Photo_Shedule.groupPosition, mGroupPosition);
        intentShowPhoto.putExtra(Activity_Show_Photo_Shedule.nameActivity, nameActivity);
        mContext.startActivity(intentShowPhoto);

    }

    private int[] getNumbersPhotoGroupView(String nameActivity) {

        // Для отображения иконок фотографий расписания используется массив int[] numbersPhotoGroupView
        // Для изменений количества фотографий достаточно изменить массив int[] в DataBaseHelper

        if (nameActivity == DataBaseHelper.ROUTE_9_ZBH_BUSSTATION) {
            return DataBaseHelper.numbersPhotoGroupViewInRoute_9_ZBH_BUSSTATION;
        }
        if (nameActivity == DataBaseHelper.ROUTE_9_BUSSTATION_ZBH) {
            return DataBaseHelper.numbersPhotoGroupViewInRoute_9_BUSSTATION_ZBH;
        }
        if (nameActivity == DataBaseHelper.ROUTE_201_KALINKOVICHI_MOZYR) {
            return DataBaseHelper.numbersPhotoGroupViewInRoute_201_KALINKOVICHI_MOZYR;
        }
        if (nameActivity == DataBaseHelper.ROUTE_201_MOZYR_KALINKOVICHI) {
            return DataBaseHelper.numbersPhotoGroupViewInRoute_201_MOZYR_KALINKOVICHI;
        }

        return null;
    }


}
