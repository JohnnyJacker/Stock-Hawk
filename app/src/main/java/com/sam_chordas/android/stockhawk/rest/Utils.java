package com.sam_chordas.android.stockhawk.rest;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.util.Log;

import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by sam_chordas on 10/8/15.
 */
public class Utils {

    private Context mContext;

    private static String LOG_TAG = Utils.class.getSimpleName();

    public static boolean showPercent = true;

    public static ArrayList quoteJsonToContentVals(String JSON) {
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
        JSONObject jsonObject = null;
        JSONArray resultsArray = null;
        try {
            jsonObject = new JSONObject(JSON);
            if (jsonObject.length() != 0) {
                jsonObject = jsonObject.getJSONObject("query");
                int count = Integer.parseInt(jsonObject.getString("count"));
                if (count == 1) {
                    jsonObject = jsonObject.getJSONObject("results")
                            .getJSONObject("quote");

                    String ask = jsonObject.getString("Ask");

                    if (doesSymbolExist(ask)) {

                        try {
                            batchOperations.add(buildBatchOperation(jsonObject));
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "String to JSON failed: " + e);


                        }
                    } else {

                        return null;


                    }


                } else {
                    resultsArray = jsonObject.getJSONObject("results").getJSONArray("quote");

                    if (resultsArray != null && resultsArray.length() != 0) {

                        for (int i = 0; i < resultsArray.length(); i++) {
                            jsonObject = resultsArray.getJSONObject(i);
                            batchOperations.add(buildBatchOperation(jsonObject));


                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "String to JSON failed: " + e);
        }
        return batchOperations;
    }


    public static ArrayList<String> datesToStringArray(String JSON) {

        JSONObject jsonObject = null;
        JSONArray resultsArray = null;

        try {

            jsonObject = new JSONObject(JSON);

            if (jsonObject.length() != 0) {

                jsonObject = jsonObject.getJSONObject("query");

                int count = Integer.parseInt(jsonObject.getString("count"));


                jsonObject = jsonObject.getJSONObject("results");

                resultsArray = jsonObject.getJSONArray("quote");

                ArrayList<String> ar1 = new ArrayList<>();
                ArrayList<String> ar2 = new ArrayList<>();
                ArrayList<String> ar3 = new ArrayList<>();


                for (int i = 0; i < count; i++) {

                    JSONObject stockForecast = resultsArray.getJSONObject(i);


                    String date = stockForecast.getString("Date");
                    String close = stockForecast.getString("Close");

                    ar1.add(date);
                    ar2.add(close);


                }
                ar3.addAll(ar1);
                ar3.addAll(ar2);
                return ar3;


            }


        } catch (JSONException e) {
            Log.e(LOG_TAG, "String to JSON failed: " + e);
        }

        return null;
    }


    public static boolean doesSymbolExist(String ask) {

        if (ask != null && !ask.equals("null")) {

            return true;
        }
        return false;
    }

    public static String getOneWeek(String symbol) {


        Calendar from = Calendar.getInstance();
        from.add(Calendar.DAY_OF_WEEK, -7);


        Calendar to = Calendar.getInstance();

        String oneWeekURL;
        String firstPart = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22";
        String secondPart = "%22%20and%20startDate%20%3D%20%22";
        String thirdPart = "%22%20and%20endDate%20%3D%20%22";
        String lastPart = "%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
        String startDate = printCalendar(from);
        String endDate = printCalendar(to);

        oneWeekURL = firstPart + symbol + secondPart + startDate + thirdPart + endDate + lastPart;


        return oneWeekURL;
    }

    public static String getOneMonth(String symbol) {


        Calendar from = Calendar.getInstance();
        from.add(Calendar.MONTH, -1);


        Calendar to = Calendar.getInstance();

        String oneWeekURL;
        String firstPart = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22";
        String secondPart = "%22%20and%20startDate%20%3D%20%22";
        String thirdPart = "%22%20and%20endDate%20%3D%20%22";
        String lastPart = "%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
        String startDate = printCalendar(from);
        String endDate = printCalendar(to);

        oneWeekURL = firstPart + symbol + secondPart + startDate + thirdPart + endDate + lastPart;


        return oneWeekURL;
    }

    public static String getOneYear(String symbol) {

        Calendar from = Calendar.getInstance();
        from.add(Calendar.YEAR, -1);

        Calendar to = Calendar.getInstance();

        String oneWeekURL;
        String firstPart = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22";
        String secondPart = "%22%20and%20startDate%20%3D%20%22";
        String thirdPart = "%22%20and%20endDate%20%3D%20%22";
        String lastPart = "%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
        String startDate = printCalendar(from);
        String endDate = printCalendar(to);

        oneWeekURL = firstPart + symbol + secondPart + startDate + thirdPart + endDate + lastPart;


        return oneWeekURL;
    }


    public static String printCalendar(Calendar calendar) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String date = sdf.format(calendar.getTime());


        return date;

    }


    public static String truncateBidPrice(String bidPrice) {

        if (bidPrice != null || !bidPrice.equals("null"))


            try {
                bidPrice = String.format(Locale.US, "%.2f", Float.parseFloat(bidPrice));
                return bidPrice;
            } catch (Exception e) {
                e.printStackTrace();
            }
        return "00.00";
    }

    public static String truncateChange(String change, boolean isPercentChange) {
        String weight = change.substring(0, 1);
        String ampersand = "";
        if (isPercentChange) {
            ampersand = change.substring(change.length() - 1, change.length());
            change = change.substring(0, change.length() - 1);
        }
        change = change.substring(1, change.length());

        double round = (double) Math.round(Double.parseDouble(change) * 100) / 100;
        change = String.format(Locale.US, "%.2f", round);

        StringBuilder changeBuffer = new StringBuilder(change);
        changeBuffer.insert(0, weight);
        changeBuffer.append(ampersand);
        change = changeBuffer.toString();
        return change;

    }

    public static ContentProviderOperation buildBatchOperation(JSONObject jsonObject) {

        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                QuoteProvider.Quotes.CONTENT_URI);


        try {


            String change = jsonObject.getString("Change");
            builder.withValue(QuoteColumns.SYMBOL, jsonObject.getString("symbol"));
            builder.withValue(QuoteColumns.BIDPRICE, truncateBidPrice(jsonObject.getString("Bid")));
            builder.withValue(QuoteColumns.PERCENT_CHANGE, truncateChange(
                    jsonObject.getString("ChangeinPercent"), true));
            builder.withValue(QuoteColumns.CHANGE, truncateChange(change, false));
            builder.withValue(QuoteColumns.ISCURRENT, 1);
            if (change.charAt(0) == '-') {
                builder.withValue(QuoteColumns.ISUP, 0);
            } else {
                builder.withValue(QuoteColumns.ISUP, 1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return builder.build();
    }
}
