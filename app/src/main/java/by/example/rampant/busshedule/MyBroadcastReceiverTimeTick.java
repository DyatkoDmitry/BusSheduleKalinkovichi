package by.example.rampant.busshedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcastReceiverTimeTick extends BroadcastReceiver {

    private MyExpandableAdapter myExpandableAdapter;

    MyBroadcastReceiverTimeTick(MyExpandableAdapter myExpandableAdapter) {
        this.myExpandableAdapter = myExpandableAdapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        myExpandableAdapter.notifyDataSetChanged();
    }
}
