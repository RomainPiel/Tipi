package com.romainpiel.lib.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.romainpiel.tipi.R;
import com.romainpiel.lib.ui.view.inflate.InflateHelper;
import com.romainpiel.lib.ui.view.inflate.OnViewChangedListener;
import com.romainpiel.model.ApplicationInfo;

import butterknife.InjectView;
import butterknife.Views;

/**
 * BlaBlaCar
 * User: romainpiel
 * Date: 02/09/2013
 * Time: 14:40
 */
public class AppItemView extends LinearLayout implements OnViewChangedListener {

    @InjectView(R.id.item_app_label) TextView label;

    InflateHelper inflateHelper;
    Rect oldBounds;
    int iconSize;

    public AppItemView(Context context) {
        super(context);
        init();
    }

    private void init() {
        inflateHelper = new InflateHelper(getContext(), this, this, R.layout.item_app);
        oldBounds = new Rect();
        iconSize = getResources().getDimensionPixelSize(R.dimen.item_app_icon_size);
    }

    @Override
    protected void onFinishInflate() {
        inflateHelper.onFinishInflate();
        super.onFinishInflate();
    }

    public static AppItemView build(Context context) {
        AppItemView instance = new AppItemView(context);
        instance.onFinishInflate();
        return instance;
    }

    public void bind(ApplicationInfo info) {
        if (info != null) {

            Drawable icon = info.icon;

            if (!info.filtered) {

                final int iconWidth = icon.getIntrinsicWidth();
                final int iconHeight = icon.getIntrinsicHeight();

                if (icon instanceof PaintDrawable) {
                    PaintDrawable painter = (PaintDrawable) icon;
                    painter.setIntrinsicWidth(iconSize);
                    painter.setIntrinsicHeight(iconSize);
                }

                if (iconSize > 0 && iconSize > 0 && (iconSize < iconWidth || iconSize < iconHeight)) {
                    final float ratio = (float) iconWidth / iconHeight;

                    if (iconWidth > iconHeight) {
                        iconSize = (int) (iconSize / ratio);
                    } else if (iconHeight > iconWidth) {
                        iconSize = (int) (iconSize * ratio);
                    }

                    final Bitmap.Config c =
                            icon.getOpacity() != PixelFormat.OPAQUE ?
                                    Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
                    final Bitmap thumb = Bitmap.createBitmap(iconSize, iconSize, c);
                    final Canvas canvas = new Canvas(thumb);
                    canvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.DITHER_FLAG, 0));
                    // Copy the old bounds to restore them later
                    // If we were to do oldBounds = icon.getBounds(),
                    // the call to setBounds() that follows would
                    // change the same instance and we would lose the
                    // old bounds
                    oldBounds.set(icon.getBounds());
                    icon.setBounds(0, 0, iconSize, iconSize);
                    icon.draw(canvas);
                    icon.setBounds(oldBounds);
                    icon = info.icon = new BitmapDrawable(thumb);
                    info.filtered = true;
                }
            }

            label.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
            label.setText(info.title);
        }
    }

    @Override
    public void onViewChanged() {
        Views.inject(this);
    }
}
