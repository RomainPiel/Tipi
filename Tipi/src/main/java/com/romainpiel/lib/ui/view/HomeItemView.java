package com.romainpiel.lib.ui.view;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.romainpiel.lib.ui.adapter.HomePagerAdapter;
import com.romainpiel.lib.ui.view.inflate.InflateHelper;
import com.romainpiel.lib.ui.view.inflate.OnViewChangedListener;
import com.romainpiel.lib.utils.ScreenUtils;
import com.romainpiel.tipi.R;

import butterknife.InjectView;
import butterknife.Views;

/**
 * BlaBlaCar
 * User: romainpiel
 * Date: 02/09/2013
 * Time: 14:40
 */
public class HomeItemView extends FrameLayout implements OnViewChangedListener {

    @InjectView(R.id.item_home_pager) ViewPager pager;

    InflateHelper inflateHelper;
    WindowManager windowManager;

    public HomeItemView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflateHelper = new InflateHelper(getContext(), this, this, R.layout.item_home);
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    protected void onFinishInflate() {
        inflateHelper.onFinishInflate();
        super.onFinishInflate();
    }

    public static HomeItemView build(Context context) {
        HomeItemView instance = new HomeItemView(context);
        instance.onFinishInflate();
        return instance;
    }

    @Override
    public void onViewChanged() {
        Views.inject(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Point screen = ScreenUtils.getSize(getContext());
        super.onMeasure(
                MeasureSpec.makeMeasureSpec(screen.x, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(screen.y, MeasureSpec.EXACTLY)
        );
    }

    public void setAdapter(HomePagerAdapter adapter) {
        pager.setAdapter(adapter);
    }

    public void showSpace(int position) {
        pager.setCurrentItem(position);
    }
}
