package com.wywlds.com.demowebview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class MainActivity extends ActionBarActivity {

    private WebView mWebView;
    private Handler mHandler;
    private Handler mUIHanlder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webview);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        mUIHanlder = new Handler();
        HandlerThread handlerThread = new HandlerThread("IOThread");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                final String url = getStringFromFile(getApplicationContext(), "target_url", "utf-8");
                Log.d("wyw", url);
                mUIHanlder.post(new Runnable() {

                    @Override
                    public void run() {
                        mWebView.loadUrl(url);
                    }
                });
            }
        });
    }

    public static String getStringFromFile(Context context,
                                           String fileName, String encoding) {
        InputStream in = null;
        BufferedReader reader = null;
        try {
            StringBuilder builder = new StringBuilder();
            char[] buffer = new char[512];
            in = context.getResources().getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(in, encoding));
            for (int length; (length = reader.read(buffer)) > 0;) {
                builder.append(buffer, 0, length);
            }
            return builder.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
