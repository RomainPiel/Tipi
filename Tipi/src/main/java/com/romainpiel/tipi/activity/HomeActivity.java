package com.romainpiel.tipi.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.romainpiel.lib.ui.adapter.ApplicationsAdapter;
import com.romainpiel.lib.ui.adapter.HomePagerAdapter;
import com.romainpiel.lib.ui.effect.Blur;
import com.romainpiel.lib.ui.view.HomeItemView;
import com.romainpiel.model.ApplicationInfo;
import com.romainpiel.tipi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.InjectView;
import butterknife.Views;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.AndroidObservables;
import rx.concurrency.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Home
 * User: romainpiel
 * Date: 11/10/2013
 * Time: 19:04
 */
public class HomeActivity extends FragmentActivity implements Observer<Bitmap> {

    @InjectView(R.id.activity_home_wallpaper) ImageView wallpaper;
    @InjectView(R.id.activity_home_wallpaper_blurred) ImageView blurredWallpaper;
    @InjectView(R.id.activity_home_listview) ListView listView;
    HomeItemView homeView;
    View touchTarget;

    private Subscription imageBlurSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Views.inject(this);

        List<ApplicationInfo> apps = loadApplications();

        ApplicationsAdapter listAdapter = new ApplicationsAdapter(this, apps);

        homeView = HomeItemView.build(this);
        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        homeView.setAdapter(pagerAdapter);
        homeView.showSpace(2);

        listView.addHeaderView(homeView);
        listView.setAdapter(listAdapter);
        linkWallpaperWithListView();

        wallpaper.setImageResource(R.drawable.image);

        imageBlurSubscription = AndroidObservables.fromActivity(this, blurImage())
                .subscribeOn(Schedulers.newThread())
                .subscribe(this);
    }

    @Override
    protected void onDestroy() {
        imageBlurSubscription.unsubscribe();
        super.onDestroy();
    }

    public void linkWallpaperWithListView() {
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
    }

    private Observable<Bitmap> blurImage() {
        return Observable.create(new Observable.OnSubscribeFunc<Bitmap>() {
            @Override
            public Subscription onSubscribe(Observer<? super Bitmap> observer) {
                Bitmap inputBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
                final Bitmap outputBitmap = Blur.fastblur(HomeActivity.this, inputBitmap, 25);
                observer.onNext(outputBitmap);
                observer.onCompleted();
                return Subscriptions.empty();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchTarget != null) {
            boolean wasProcessed = touchTarget.onTouchEvent(ev);

            if (!wasProcessed) {
                touchTarget = null;
            }

            return wasProcessed;
        }
        return super.dispatchTouchEvent(ev);
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

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onNext(Bitmap bitmap) {
        blurredWallpaper.setImageBitmap(bitmap);
    }
}
