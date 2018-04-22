package com.termproject.misaka.timesecretary;

import android.support.v4.app.Fragment;

/**
 * @author misaka
 */
public class AddActivity extends BaseSingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new NewEventFragment();
    }

}
