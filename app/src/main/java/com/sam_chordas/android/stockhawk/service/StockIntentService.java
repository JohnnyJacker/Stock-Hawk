
package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.gcm.TaskParams;
import com.sam_chordas.android.stockhawk.R;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService {


    private static String LOG_TAG = StockIntentService.class.getSimpleName();

    Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
    }

    public StockIntentService() {
        super(StockIntentService.class.getName());
    }

    public StockIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        StockTaskService stockTaskService = new StockTaskService(this);
        Bundle args = new Bundle();
        if (intent.getStringExtra("tag").equals("add")) {
            args.putString("symbol", intent.getStringExtra("symbol"));
        }


        int result = 0;

        try {


            result = stockTaskService.onRunTask(new TaskParams(intent.getStringExtra("tag"), args));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (result == 2) {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(StockIntentService.this, getString(R.string.stock_error), Toast.LENGTH_LONG).show();

                    }
                });


            }


        }


    }
}
