package com.romainpiel.lib.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Tipi
 * User: romainpiel
 * Date: 16/10/2013
 * Time: 14:20
 */
public class PageableListView extends ListView {

    private float xDistance, yDistance, lastX, lastY;

    public PageableListView(Context context) {
        super(context);
    }

    public PageableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PageableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if (xDistance > yDistance)
                    return false;
        }

        return super.onInterceptTouchEvent(ev);
    }
}
