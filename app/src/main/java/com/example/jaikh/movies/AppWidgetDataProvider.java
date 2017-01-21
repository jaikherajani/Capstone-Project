package com.example.jaikh.movies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

/**
 * Created by jaikh on 14-01-2017.
 */

public class AppWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Cursor cursor;
    private Intent intent;
    private Long movieid;

    //For obtaining the activity's context and intent
    public AppWidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    private void initCursor() {
        System.out.println("initCursor() called");
        if (cursor != null) {
            cursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        /**This is done because the widget runs as a separate thread
         when compared to the current app and hence the app's data won't be accessible to it
         because I'm using a content provided **/
        cursor = context.getContentResolver().query(MovieProvider.CONTENT_URI, null, null, null, null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onCreate() {
        System.out.println("onCreate() called");
        initCursor();
        if (cursor != null) {
            cursor.moveToFirst();
        }
    }

    @Override
    public void onDataSetChanged() {
        /** Listen for data changes and initialize the cursor again **/
        System.out.println("onDataSetChanged() called");
        initCursor();
    }

    @Override
    public void onDestroy() {
        cursor.close();
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        /** Populate your widget's single list item **/
        final Intent fillInIntent = new Intent();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.rv_item);
        cursor.moveToPosition(i);
        try {
            Bitmap theBitmap = Glide.
                    with(context.getApplicationContext()).
                    load("http://image.tmdb.org/t/p/w185/" + cursor.getString(1))
                    .asBitmap()
                    .into(185, 275) // Width and height
                    .get();

            remoteViews.setImageViewBitmap(R.id.grid_image, theBitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //remoteViews.setTextViewText(R.id.movie_id,cursor.getString(0));
        movieid = cursor.getLong(0);
        fillInIntent.putExtra("MOVIE_ID", movieid);
        System.out.println("AppWidgetDataProvier getViewAt() : URL - " + cursor.getString(1) + " ID - " + movieid);
        remoteViews.setOnClickFillInIntent(R.id.grid_image, fillInIntent);
        return remoteViews;
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
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}