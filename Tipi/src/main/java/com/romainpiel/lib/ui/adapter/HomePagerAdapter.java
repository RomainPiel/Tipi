package com.romainpiel.lib.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.romainpiel.lib.ui.fragment.HomeSpaceFragment;

/**
 * Tipi
 * User: romainpiel
 * Date: 15/10/2013
 * Time: 19:49
 */
public class HomePagerAdapter extends FragmentStatePagerAdapter {

    SparseArray<HomeSpaceFragment> registeredFragments = new SparseArray<HomeSpaceFragment>();

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public HomeSpaceFragment getItem(int i) {
        return HomeSpaceFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        HomeSpaceFragment fragment = (HomeSpaceFragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public HomeSpaceFragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
