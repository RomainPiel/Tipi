package com.romainpiel.tipi.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.romainpiel.lib.ui.adapter.ApplicationsAdapter;
import com.romainpiel.lib.ui.effect.Blur;
import com.romainpiel.lib.ui.view.HomeItemView;
import com.romainpiel.lib.utils.BackgroundExecutor;
import com.romainpiel.lib.utils.Debug;
import com.romainpiel.model.ApplicationInfo;
import com.romainpiel.tipi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Home
 * User: romainpiel
 * Date: 11/10/2013
 * Time: 19:04
 */
public class HomeActivity extends Activity {

    @InjectView(R.id.activity_home_wallpaper) ImageView wallpaper;
    @InjectView(R.id.activity_home_wallpaper_blurred) ImageView blurredWallpaper;
    @InjectView(R.id.activity_home_listview) ListView listView;
    HomeItemView homeView;

    ApplicationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Views.inject(this);

        List<ApplicationInfo> apps = loadApplications();

        adapter = new ApplicationsAdapter(this, apps);

        homeView = HomeItemView.build(this);

        listView.addHeaderView(homeView);
        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            /**
             * Listen to the list scroll. This is where magic happens ;)
             */
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Calculate the ratio between the scroll amount and the list
                // header weight to determinate the top picture alpha
                float alpha = (float) -homeView.getTop() / (float) 700;
                Debug.out(alpha);
                // Apply a ceil
                if (alpha > 1) {
                    alpha = 1;
                }
                blurredWallpaper.setAlpha(alpha);
                // Parallax effect : we apply half the scroll amount to our
                // views
                blurredWallpaper.setTop(homeView.getTop() / 2);
                wallpaper.setTop(homeView.getTop() / 2);
            }
        });

        wallpaper.setImageResource(R.drawable.image);

        BackgroundExecutor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        Bitmap inputBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
                        final Bitmap outputBitmap = Blur.fastblur(HomeActivity.this, inputBitmap, 25);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                blurredWallpaper.setImageBitmap(outputBitmap);
                            }
                        });
                    }
                }
        );
    }

    private List<ApplicationInfo> loadApplications() {

        List<ApplicationInfo> applications = null;

        PackageManager manager = getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

        if (apps != null) {
            final int count = apps.size();

            applications = new ArrayList<ApplicationInfo>(count);

            for (ResolveInfo app : apps) {
                ApplicationInfo application = new ApplicationInfo();

                application.title = app.loadLabel(manager);
                application.setActivity(new ComponentName(
                        app.activityInfo.applicationInfo.packageName,
                        app.activityInfo.name),
                        Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                application.icon = app.activityInfo.loadIcon(manager);

                applications.add(application);
            }
        }

        return applications;
    }

}
