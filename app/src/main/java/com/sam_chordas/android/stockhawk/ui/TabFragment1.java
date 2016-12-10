package com.sam_chordas.android.stockhawk.ui;

/**
 * Created by T510 Owner on 11/26/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

public class TabFragment1 extends Fragment {


    private Context mContext;
    boolean isConnected;
    private OkHttpClient client = new OkHttpClient();
    LineChart mLineChart;
    String mSymbol;
    Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mContext = getActivity();


        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        new getHistoricalData().execute();

        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);

        mLineChart = (LineChart) view.findViewById(R.id.linechart);

        return view;
    }

    public class getHistoricalData extends AsyncTask<Object, Object, ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(Object... params) {


            Bundle b = getArguments();
            String s = b.getString("symbol");

            mSymbol = s;


            String oneWeekURL = Utils.getOneWeek(s);

            Log.d(LOG_TAG, oneWeekURL);

            try {
                String getResponse = fetchData(oneWeekURL);

                return Utils.datesToStringArray(getResponse);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);

            if (isConnected) {

                int size = strings.size();
                int halfSize = size / 2;


                List<String> priceList = new ArrayList<>();
                List<String> dateList = new ArrayList<>();

                for (int i = halfSize; i <= size - 1; i++) {


                    priceList.add(strings.get(i));

                }
                for (int j = 0; j < halfSize; j++) {

                    dateList.add(strings.get(j));
                }

                Collections.reverse(dateList);


                Collections.reverse(priceList);


                setData(priceList.size(), priceList, dateList);

            } else {

                networkToast();
            }


        }
    }

    private void setData(int count, List<String> range, List<String> dates) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        Object[] dateValues = dates.toArray();
        final String[] strDates = Arrays.copyOf(dateValues, dateValues.length, String[].class);

        for (int i = 0; i < count; i++) {


            values.add(new Entry(i, Float.parseFloat(range.get(i))));


        }


        LineDataSet set1;

        if (mLineChart.getData() != null && mLineChart.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();

        } else {
            set1 = new LineDataSet(values, mSymbol);

            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(1f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);


            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);

            mLineChart.setData(data);

            mLineChart.invalidate();


            IAxisValueFormatter formatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return strDates[(int) value];
                }


                public int getDecimalDigits() {
                    return 0;
                }
            };

            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(formatter);

        }

    }


    String fetchData(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public void networkToast() {
        Toast.makeText(mContext, getString(R.string.network_toast), Toast.LENGTH_SHORT).show();
    }

}
