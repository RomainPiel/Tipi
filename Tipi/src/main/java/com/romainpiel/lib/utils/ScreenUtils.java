package com.romainpiel.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * MPme
 * User: romainpiel
 * Date: 26/03/2013
 * Time: 10:16
 */
public class ScreenUtils {

    public static Point getSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        return getSize(display);
    }

    public static Point getSize(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return getSize(display);
    }

    private static Point getSize(Display display) {
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            display.getSize(size);
        } else {
            int width = display.getWidth();
            int height = display.getHeight();
            size = new Point(width,height);
        }

        return size;
    }

    public static int getOrientation(Activity activity) {
        Display getOrient = activity.getWindowManager().getDefaultDisplay();

        int width, height;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Point size = new Point();
            getOrient.getSize(size);
            width = size.x;
            height = size.y;
        } else {
            width = getOrient.getWidth();
            height = getOrient.getHeight();
        }

        int orientation;
        if (width == height) {
            orientation = Configuration.ORIENTATION_SQUARE;
        } else if (width < height) {
            orientation = Configuration.ORIENTATION_PORTRAIT;
        } else {
            orientation = Configuration.ORIENTATION_LANDSCAPE;
        }

        return orientation;
    }

    public static int getDensity(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        return getDensity(display);
    }

    public static int getDensity(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return getDensity(display);
    }

    public static int getDensity(Display display) {
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.densityDpi;
    }
}
