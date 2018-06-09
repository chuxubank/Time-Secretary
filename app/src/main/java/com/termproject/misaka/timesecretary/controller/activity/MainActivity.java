package com.termproject.misaka.timesecretary.controller.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Calendars;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.termproject.misaka.timesecretary.R;
import com.termproject.misaka.timesecretary.controller.NotificationService;
import com.termproject.misaka.timesecretary.controller.fragment.CategoryListFragment;
import com.termproject.misaka.timesecretary.controller.fragment.ListFragment;
import com.termproject.misaka.timesecretary.controller.fragment.TodayFragment;
import com.termproject.misaka.timesecretary.module.Category;
import com.termproject.misaka.timesecretary.module.CategoryLab;
import com.termproject.misaka.timesecretary.module.Event;
import com.termproject.misaka.timesecretary.module.EventLab;
import com.termproject.misaka.timesecretary.module.Task;
import com.termproject.misaka.timesecretary.module.TaskLab;
import com.termproject.misaka.timesecretary.part.NoScrollViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.WRITE_CALENDAR;
import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateCalendar;

/**
 * @author misaka
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int NOTIFICATION_JOB_ID = 1;

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.termproject.misaka.timesecretary.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.termproject.misaka.timesecretary";
    // The account name
    public static final String ACCOUNT = "default_account";
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
            Calendars._ID,                           // 0
            Calendars.ACCOUNT_NAME,                  // 1
            Calendars.CALENDAR_DISPLAY_NAME,         // 2
            Calendars.OWNER_ACCOUNT,                 // 3
            Calendars.NAME                           // 4
    };
    private static final String CALENDAR_ACCOUNT = "chuxubank@gmail.com";
    private static final int REQUEST_CALENDAR = 1;
    private NavigationView mNavigationView;
    private FloatingActionsMenu mFamAdd;
    private FloatingActionButton mAddEvent;
    private FloatingActionButton mAddTask;
    private FloatingActionButton mAddCategory;
    private NoScrollViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private List<Fragment> mFragments;
    private View mVCover;
    private int mPosition;
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private static final int PROJECTION_NAME = 4;
    // Instance fields
    Account mAccount;

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            return newAccount;
        } else {
            return null;
        }
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the dummy account
        mAccount = CreateSyncAccount(this);
        initView();
        cancelAllJobs();
        scheduleNotificationJob();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        View headerView = mNavigationView.getHeaderView(0);
        TextView tvNickname = headerView.findViewById(R.id.tv_nickname);
        tvNickname.setText(preferences.getString("nickname", getString(R.string.pref_default_display_name)));
    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.first_fragment));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_today);

        mFamAdd = findViewById(R.id.fam_add);
        mFamAdd.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                mVCover.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                mVCover.setVisibility(View.GONE);
            }
        });
        mAddEvent = findViewById(R.id.add_event);
        mAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = new Event();
                event.setCategory(CategoryLab.get(MainActivity.this).getCategories().get(0).getId());
                EventLab.get(MainActivity.this).addEvent(event);
                Intent intent = EventActivity.newIntent(MainActivity.this, event.getId());
                startActivity(intent);
                mFamAdd.collapse();
            }
        });
        mAddTask = findViewById(R.id.add_task);
        mAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                task.setCategory(CategoryLab.get(MainActivity.this).getCategories().get(0).getId());
                TaskLab.get(MainActivity.this).addTask(task);
                Intent intent = TaskActivity.newIntent(MainActivity.this, task.getId());
                startActivity(intent);
                mFamAdd.collapse();
            }
        });
        mAddCategory = findViewById(R.id.add_category);
        mAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category = new Category();
                CategoryLab.get(MainActivity.this).addCategory(category);
                Intent intent = CategoryActivity.newIntent(MainActivity.this, category.getId());
                startActivity(intent);
                mFamAdd.collapse();
            }
        });

        mFragments = new ArrayList<>();
        mFragments.add(new TodayFragment());
        mFragments.add(new ListFragment());
        mFragments.add(new CategoryListFragment());

        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };

        mViewPager = findViewById(R.id.show_view_pager);
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(new NoScrollViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVCover = findViewById(R.id.v_cover);
        mVCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVCover.setVisibility(View.GONE);
                mFamAdd.collapse();
            }
        });
    }

    public void cancelAllJobs() {
        JobScheduler tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.cancelAll();
    }

    private void scheduleNotificationJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = new JobInfo.Builder(NOTIFICATION_JOB_ID, new ComponentName(this, NotificationService.class))
                .setPeriodic(1000 * 60)
//                .setMinimumLatency(500)
//                .setOverrideDeadline(1000)
                .setPersisted(true)
                .build();
        scheduler.schedule(jobInfo);
    }

    @Override

    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sync:
                syncEventsOnDemand();
                return true;

            default:
                Fragment fragment = mFragmentPagerAdapter.getItem(mPosition);
                return fragment.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALENDAR) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                syncEventsOnDemand();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_today:
                getSupportActionBar().setTitle(item.getTitle());
                mViewPager.setCurrentItem(0);
                break;
            case R.id.nav_list:
                getSupportActionBar().setTitle(item.getTitle());
                mViewPager.setCurrentItem(1);
                break;
            case R.id.nav_categories:
                getSupportActionBar().setTitle(item.getTitle());
                mViewPager.setCurrentItem(2);
                break;
            case R.id.nav_analysis:
                getSupportActionBar().setTitle(item.getTitle());
                Calendar startDate = cal2dateCalendar(Calendar.getInstance());
                Calendar endDate = cal2dateCalendar(Calendar.getInstance());
                endDate.add(Calendar.DATE, 1);
                intent = AnalysisActivity.newIntent(this, startDate, endDate);
                startActivity(intent);
                break;
            case R.id.nav_login:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                intent = new Intent(MainActivity.this, SettingsActivityBase.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        updateAllFragment();
        return true;
    }

    private boolean mayRequestCalendar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CALENDAR) || shouldShowRequestPermissionRationale(WRITE_CALENDAR)) {
            Snackbar.make(mNavigationView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CALENDAR, WRITE_CALENDAR}, REQUEST_CALENDAR);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CALENDAR, WRITE_CALENDAR}, REQUEST_CALENDAR);
        }
        return false;
    }

//    private void addAppCalendar() {
//        if (!mayRequestCalendar()) {
//            return;
//        }
//        // Run query
//        Cursor cur = null;
//        ContentResolver cr = getContentResolver();
//        Uri uri = Calendars.CONTENT_URI;
//        String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
//                + Calendars.ACCOUNT_TYPE + " = ?) AND ("
//                + Calendars.OWNER_ACCOUNT + " = ?))";
//        String[] selectionArgs = new String[]{CALENDAR_ACCOUNT, "com.termproject.misaka.timesecretary", CALENDAR_ACCOUNT};
//        // Submit the query and get a Cursor object back.
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
//        boolean appCalendarExists = false;
//        // Use the cursor to step through the returned records
//        while (cur.moveToNext()) {
//            long calID = 0;
//            String name = null;
//            calID = cur.getLong(PROJECTION_ID_INDEX);
//            name = cur.getString(PROJECTION_NAME);
//            String displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
//            Log.i(TAG, displayName + " " + calID + " " + name);
//            if (name.equals(getString(R.string.app_name))) {
//                appCalendarExists = true;
//            }
//        }
//        cur.close();
//        if (!appCalendarExists) {
//            // Pass the settings flags by inserting them in a bundle
//            Bundle settingsBundle = new Bundle();
//            settingsBundle.putBoolean(
//                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
//            settingsBundle.putBoolean(
//                    ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
//            /*
//             * Request the sync for the default account, authority, and
//             * manual sync settings
//             */
//            ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
//        }
//    }

    private void syncEventsOnDemand() {
        if (!mayRequestCalendar()) {
            return;
        }
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);
    }

    private void updateAllFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            f.onResume();
        }
    }
}
