package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 * Created by T510 Owner on 11/27/2016.
 */


public class WidgetProvider implements RemoteViewsService.RemoteViewsFactory {


    Cursor mCursor = null;
    Context mContext = null;


    public WidgetProvider(Context context, Intent intent) {

        mContext = context;

    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (position == AdapterView.INVALID_POSITION || mCursor == null || !mCursor.moveToPosition(position)) {
            return null;
        }

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        views.setTextViewText(R.id.widget_stock_symbol, mCursor.getString(mCursor.getColumnIndex("symbol")));
        views.setTextViewText(R.id.widget_bid_price, mCursor.getString(mCursor.getColumnIndex("bid_price")));

        int sdk = Build.VERSION.SDK_INT;
        if (mCursor.getInt(mCursor.getColumnIndex(QuoteColumns.ISUP)) == 1) {
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                views.setInt(R.id.widget_change, mContext.getResources().getString(R.string.string_set_background_resource), R.drawable.percent_change_pill_green);
            } else {
                views.setInt(R.id.widget_change, mContext.getResources().getString(R.string.string_set_background_resource), R.drawable.percent_change_pill_green);
            }
        } else {
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                views.setInt(R.id.widget_change, mContext.getResources().getString(R.string.string_set_background_resource), R.drawable.percent_change_pill_red);
            } else {
                views.setInt(R.id.widget_change, mContext.getResources().getString(R.string.string_set_background_resource), R.drawable.percent_change_pill_red);
            }
        }
        if (Utils.showPercent) {
            views.setTextViewText(R.id.widget_change, mCursor.getString(mCursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE)));
        } else {
            views.setTextViewText(R.id.widget_change, mCursor.getString(mCursor.getColumnIndex(QuoteColumns.CHANGE)));
        }

        Bundle extras = new Bundle();
        extras.putString(CollectionWidget.EXTRA_ITEM, mCursor.getString(mCursor.getColumnIndex("symbol")));
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

        return views;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {

        long identityToken = Binder.clearCallingIdentity();


        try {
            mCursor = mContext.getContentResolver().query(
                    QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{
                            QuoteColumns._ID,
                            QuoteColumns.SYMBOL,
                            QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE,
                            QuoteColumns.CHANGE,
                            QuoteColumns.ISUP
                    },
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},
                    null);

        } finally {
            Binder.restoreCallingIdentity(identityToken);
        }

    }
}
