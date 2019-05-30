package by.example.rampant.busshedule;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by - on 05.11.2018.
 */

public class Fragment_route extends Fragment {

    //Log.d(TagDel, "_onCreate Fragment " + mPage);

    private static final String TagDel = "%!";

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private interfaceGetArrayListArray listArrayFromActivity;

    private MyExpandableAdapter myExpandableAdapter;

    private MyBroadcastReceiverTimeTick myBroadcastReceiverTimeTick;


    public static Fragment_route newInstance(int page) {

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_PAGE, page);
        Fragment_route fragment = new Fragment_route();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listArrayFromActivity = (interfaceGetArrayListArray) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Зарегестрируем
        myBroadcastReceiverTimeTick = new MyBroadcastReceiverTimeTick(myExpandableAdapter);
        listArrayFromActivity.getContext().registerReceiver(myBroadcastReceiverTimeTick, new IntentFilter("android.intent.action.TIME_TICK"));

    }

    @Override
    public void onResume() {
        super.onResume();
        // Обновим адаптер данных после вызова из бекстека
        myExpandableAdapter.notifyDataMyExpandableAdapter();
    }

    @Override
    public void onStop() {
        super.onStop();

        // Снимем регистрацию
        listArrayFromActivity.getContext().unregisterReceiver(myBroadcastReceiverTimeTick);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_of_route, container, false);

        if (mPage == 1) {
            // Адаптер для расширенного списка будних дней
            myExpandableAdapter = new MyExpandableAdapter(listArrayFromActivity.getArrayListArrayListWeekday(), getContext(), listArrayFromActivity.getNameActivity());
        } else if (mPage == 2) {
            // Адаптер для расширенного списка выходных дней
            myExpandableAdapter = new MyExpandableAdapter(listArrayFromActivity.getArrayListArrayListDayOff(), getContext(), listArrayFromActivity.getNameActivity());
        }

        MyExpandableListView myExpandableListView = new MyExpandableListView(getContext());

        myExpandableListView.setAdapter(myExpandableAdapter);

        // Установим одну полосу для groupView
        myExpandableListView.setDivider(null);
        myExpandableListView.setChildDivider(getResources().getDrawable(R.color.transparent));

        // Установим индикатор
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            myExpandableListView.setIndicatorBounds(63, 100);
        } else {
            myExpandableListView.setIndicatorBoundsRelative(63, 100);
        }

        // Добавим сам лист во фрагмент
        ViewGroup viewGroup = (RelativeLayout) view.findViewById(R.id.id_rel_layout_listView_in_fragment);
        viewGroup.addView(myExpandableListView);

        return view;
    }

}
