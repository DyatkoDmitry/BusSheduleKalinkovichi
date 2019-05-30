package by.example.rampant.busshedule;

import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by - on 17.12.2018.
 */

public class LoaderWidthBitmap extends Loader<Integer> {
    private static final String TagDel = "%!";

    String url;
    static String KEY_URL_STRING = "urlString";

    public LoaderWidthBitmap(Context context, Bundle bundle) {
        super(context);

        if (bundle != null) {
            url = bundle.getString(KEY_URL_STRING);
        }

        if (TextUtils.isEmpty(url)) {
            Toast toast = Toast.makeText(context, context.getResources().getString(R.string.internet_photo_error), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();

        if (url == null) {
            onCancelLoad();
        }

        AsyncTaskLoadWidthBitmap asyncTaskLoadWidthBitmap = new AsyncTaskLoadWidthBitmap();
        //asyncTaslLoadBitmap.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
        asyncTaskLoadWidthBitmap.execute(url);
    }

    void getResultFromTask(Integer widthBitmap) {
        deliverResult(widthBitmap);
    }

    class AsyncTaskLoadWidthBitmap extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String url = params[0];
            Integer widthBitmap = null;
            InputStream inputStream;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            try {
                inputStream = new java.net.URL(url).openStream();

                //bitmap = BitmapFactory.decodeStream(inputStream);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                widthBitmap = options.outWidth;
                //Log.d(TagDel, "bitmap Size: " + (int) bitmap.getByteCount() / 1024);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                widthBitmap = 1;
            }
            return widthBitmap;
        }

        @Override
        protected void onPostExecute(Integer widthBitmap) {
            super.onPostExecute(widthBitmap);

            getResultFromTask(widthBitmap);

        }
    }
}
