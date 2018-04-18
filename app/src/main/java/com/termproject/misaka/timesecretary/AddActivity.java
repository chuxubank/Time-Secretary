package com.termproject.misaka.timesecretary;

import android.support.v4.app.Fragment;

public class AddActivity extends BaseSingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AddFragment();
    }

}
