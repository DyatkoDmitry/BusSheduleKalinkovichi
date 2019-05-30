package by.example.rampant.busshedule;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by - on 27.01.2019.
 */
public class DialogFragmentAboutApp extends DialogFragment{
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Повесим слушателя на ссылку в ВК в диалоге о приложении
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_dialogfragment_about_application,null);


        TextView textViewSupport = (TextView)view.findViewById(R.id.id_textView_GoToVKSupport2);
        textViewSupport.setPaintFlags(textViewSupport.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        textViewSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Intent intent;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DataBaseHelper.URL_GROUP_SUPPORT_VK));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.internet_to_connect), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle((R.string.about_application)).
        setView(view)
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }
}
