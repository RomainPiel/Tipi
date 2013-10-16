package com.romainpiel.lib.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romainpiel.tipi.R;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Tipi
 * User: romainpiel
 * Date: 15/10/2013
 * Time: 19:32
 */
public class HomeSpaceFragment extends Fragment {

    @InjectView(R.id.fragment_home_space_label) TextView label;

    private int id;

    public static HomeSpaceFragment newInstance(int id) {
        HomeSpaceFragment result = new HomeSpaceFragment();
        result.id = id;
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_home_space, null);
        Views.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        label.setText(String.format("Space %d", id));
    }
}
