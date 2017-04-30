package com.example.jaikh.movies.ui.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by jaikh on 14-01-2017.
 */

public class AppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AppWidgetDataProvider(this, intent);
    }
}