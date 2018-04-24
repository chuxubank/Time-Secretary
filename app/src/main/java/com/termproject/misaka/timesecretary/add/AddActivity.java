package com.termproject.misaka.timesecretary.add;

import android.support.v4.app.Fragment;

import com.termproject.misaka.timesecretary.base.BaseSingleFragmentActivity;

/**
 * @author misaka
 */
public class AddActivity extends BaseSingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        return new NewEventFragment();
    }

}
