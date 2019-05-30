package by.example.rampant.busshedule;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

// В asyncTaskLoader(DownloadTextTask) надо передавать ссылку, а забирать ширину bitmap. Т.к. webView отображает не bitmap, а изображение напрямую

public class Activity_Show_Photo_Shedule extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Integer> {
    private static final String TagDel = "%!";
    private WebView webView;
    private ProgressBar progressBar;
    private String urlPhoto;
    private String urlTextForPhoto;
    private TextView textView;
    private static String datePhoto;
    static final int ID_LOADER = 1;

    public static final String groupPosition = "groupPosition";
    public static final String nameActivity = "nameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_show_photo_shedule);

        //Log.d(TagDel, "Memory Total Application: " + (int)Runtime.getRuntime().totalMemory()/1024);

        Intent intent = getIntent();
        int groupPosition = intent.getIntExtra(Activity_Show_Photo_Shedule.groupPosition, 0);
        String nameActivity = intent.getStringExtra(Activity_Show_Photo_Shedule.nameActivity);

        urlPhoto = "http://www.getfar.ru/Photos/" + nameActivity + "/" + groupPosition + "/" + groupPosition + ".jpg";
        urlTextForPhoto = "http://www.getfar.ru/Photos/" + nameActivity + "/" + groupPosition + "/" + groupPosition + ".txt";

        progressBar = (ProgressBar) findViewById(R.id.id_progressBar_photo_shedule);
        progressBar.setVisibility(View.VISIBLE);

        textView = (TextView) findViewById(R.id.id_textView_date_of_photo);

        webView = (WebView) findViewById(R.id.id_WebView_show_photo);

        // Устанавливаем черный фон для комфортной работы
        webView.setBackgroundColor(Color.BLACK);
        // Настройка webView
        setSettingsWebView(webView);

        // Ссылка фотографии для loader
        Bundle bundle = new Bundle();
        bundle.putString(LoaderWidthBitmap.KEY_URL_STRING, urlPhoto);

        if (hasConnection(this)) {
            getLoaderManager().initLoader(ID_LOADER, bundle, this);
        } else {
            Toast toast = Toast.makeText(this, getResources().getString(R.string.internet_to_connect), Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

        // Чтобы заново не скачивать текст даты, дату записали в поле datePhoto, показываем не запуская заново asyncTask
        if(datePhoto!=null){
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                textView.setVisibility(View.GONE);
            } else if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
                textView.setVisibility(View.VISIBLE);
                textView.setText(datePhoto);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public Loader<Integer> onCreateLoader(int id, Bundle bundle) {

        //asyncTask для загрузки надписи дата фотографии
        new DownloadTextTask(this).execute(urlTextForPhoto);

        Loader<Integer> loader = new LoaderWidthBitmap(this, bundle);
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer widthBitmap) {

        // Зададим коэффициент для webView, чтобы влазила вся фотка
        setInitialScaleWebView(widthBitmap);

        progressBar.setVisibility(View.GONE);

        // Загрузка данных
        loadDataWebView();

    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {
        //Log.d(TagDel, "onLoaderReset: " + this.hashCode());
    }

    //AsyncTask для загрузки текста
    class DownloadTextTask extends AsyncTask<String, Void, String> {

        Context context;

        DownloadTextTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            String resultText;
            InputStream inputStream;

            try {
                inputStream = new java.net.URL(url).openStream();

                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }

                inputStream.close();

                resultText = result.toString("UTF-8");

                return resultText;

            } catch (Exception e) {
                e.printStackTrace();
                resultText = null;
            }

            return resultText;
        }

        @Override
        protected void onPostExecute(String resultText) {
            super.onPostExecute(resultText);

            // Окончательное пояснение для фотографии
            if (resultText != null) {
                String textDone = getResources().getString(R.string.internet_photo_done);
                resultText = textDone + " " + resultText.trim();
                // Присвоем переменной класса дату
                datePhoto = resultText;

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    textView.setVisibility(View.GONE);
                    Toast toast = Toast.makeText(context, resultText, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    textView.setText(resultText);
                }
            }
        }
    }

    private void setInitialScaleWebView(Integer widthBitmap) {

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int widthDisplay = display.getWidth();

        Double val = 1d;

        if (widthBitmap > widthDisplay) {
            val = new Double(widthDisplay) / new Double(widthBitmap);
        }

        val = val * 100d;

        webView.setInitialScale(val.intValue());
    }

    private void loadDataWebView() {
        //webView.loadData("<html><body><center><img src='" + urlPhoto + "'/></center></body></html>", "text/html", "UTF-8");
        webView.loadDataWithBaseURL(null, "<html><body><center><img src='" + urlPhoto + "'/></center></body></html>", "text/html", "UTF-8", null);

    }

    private void setSettingsWebView(WebView webView) {

        // Очистим кэш. Бурем всегда картинку из интернета, а не из кэша
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        // Другие настройки для изменения размера webView
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);


        // Настройки для webView
        // устанавливаем Zoom control
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);

        // but,We should hide this button, otherwise in the version
        // of Android 3 and above, there is a window leak.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.getSettings().setDisplayZoomControls(false);
        }

        /*// Больше места для картинки
        webView.setPadding(0, 0, 0, 0);

        //полосы прокрутки – внутри изображения, увеличение места для просмотра
        webView.setScrollbarFadingEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);*/
    }

    public static boolean hasConnection(final Context context) {
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

}



