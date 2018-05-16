package com.termproject.misaka.timesecretary.controller.activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import static com.termproject.misaka.timesecretary.utils.TimeUtils.cal2dateCalendar;

/**
 * @author misaka
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int NOTIFICATION_JOB_ID = 1;
    private FloatingActionsMenu mFamAdd;
    private FloatingActionButton mAddEvent;
    private FloatingActionButton mAddTask;
    private FloatingActionButton mAddCategory;
    private NoScrollViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private List<Fragment> mFragments;
    private View mVCover;
    private int mPosition;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        cancelAllJobs();
        scheduleNotificationJob();
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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_today);


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
                //TODO: Complete Sync Logic.
                return true;
            default:
                Fragment fragment = mFragmentPagerAdapter.getItem(mPosition);
                return fragment.onOptionsItemSelected(item);
        }
    }

    @Override

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_today) {
            getSupportActionBar().setTitle(item.getTitle());
            mViewPager.setCurrentItem(0);
        } else if (id == R.id.nav_upcoming) {
            getSupportActionBar().setTitle(item.getTitle());
            mViewPager.setCurrentItem(1);
        } else if (id == R.id.nav_categories) {
            getSupportActionBar().setTitle(item.getTitle());
            mViewPager.setCurrentItem(2);
        } else if (id == R.id.nav_analysis) {
            getSupportActionBar().setTitle(item.getTitle());
            Calendar startDate = cal2dateCalendar(Calendar.getInstance());
            Calendar endDate = cal2dateCalendar(Calendar.getInstance());
            endDate.add(Calendar.DATE, 1);
            Intent intent = AnalysisActivity.newIntent(this, startDate, endDate);
            startActivity(intent);
        } else if (id == R.id.nav_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivityBase.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
