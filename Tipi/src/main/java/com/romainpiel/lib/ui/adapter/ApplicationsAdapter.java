package com.romainpiel.lib.ui.adapter;

import android.app.Activity;
import android.view.View;

import com.romainpiel.lib.ui.view.AppItemView;
import com.romainpiel.model.ApplicationInfo;

import java.util.List;

/**
 * Home
 * User: romainpiel
 * Date: 13/10/2013
 * Time: 10:40
 */
public class ApplicationsAdapter extends BucketListAdapter<ApplicationInfo> {

    private Activity activity;

    public ApplicationsAdapter(Activity activity, List<ApplicationInfo> apps) {
        super(activity, apps, 4);

        this.activity = activity;
    }

    @Override
    protected View getBucketElement(int position, ApplicationInfo currentElement, View convertView) {

        if (convertView == null) {
            convertView = AppItemView.build(activity);
        }

        AppItemView itemView = (AppItemView) convertView;
        itemView.bind(currentElement);

        return convertView;
    }
}
