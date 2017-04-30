package com.example.jaikh.movies.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.example.jaikh.movies.R;
import com.example.jaikh.movies.ui.MovieDetail;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    /*public static String CLICK_ACTION = "com.example.jaikh.movies.CLICK";*/

    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_gridView,
                new Intent(context, AppWidgetService.class));

        views.setEmptyView(R.id.widget_gridView, R.id.duh_no);
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        System.out.println("updateAppWidget() called");
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        /*Construct the RemoteViews object*/
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        setRemoteAdapter(context, views);
        /** PendingIntent to launch the MainActivity when the widget was clicked **/
        Intent onClickIntent = new Intent(context, MovieDetail.class);
        /*Bundle bundle = new Bundle();
        onClickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        /*onClickIntent.setAction(AppWidget.CLICK_ACTION);
        onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        onClickIntent.setData(Uri.parse(onClickIntent.toUri(Intent.URI_INTENT_SCHEME)));*/
        PendingIntent onClickPendingIntent = PendingIntent.getActivity(context, 0,
                onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_gridView, onClickPendingIntent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_gridView);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            System.out.println("onUpdate() called");
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        /*Intent intent = new Intent(WIDGET_BUTTON);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.MY_BUTTON_ID, pendingIntent );*/
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    /**
     * A general technique for calling the onUpdate method,
     * requiring only the context parameter.
     *
     * @author John Bentley, based on Android-er code.
     * @see <a href="http://android-er.blogspot.com
     * .au/2010/10/update-widget-in-onreceive-method.html">
     * Android-er > 2010-10-19 > Update Widget in onReceive() method</a>
     */
    private void onUpdate(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance
                (context);

        // Uses getClass().getName() rather than MyWidget.class.getName() for
        // portability into any App Widget Provider Class
        ComponentName thisAppWidgetComponentName =
                new ComponentName(context.getPackageName(), getClass().getName()
                );
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onReceive() called");
        System.out.println("Intent recieved - " + intent.getLongExtra("MOVIE_ID", 0));
        /*if (intent.getAction().equals(CLICK_ACTION)) {
            //do some really cool stuff here
            System.out.println("Widget Clicked");
        }*/
        onUpdate(context);
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

