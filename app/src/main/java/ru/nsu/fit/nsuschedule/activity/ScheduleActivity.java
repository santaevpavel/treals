package ru.nsu.fit.nsuschedule.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.Calendar;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.fragment.ScheduleFragment;
import ru.nsu.fit.nsuschedule.view.CalendarHeaderView;

public class ScheduleActivity extends AppCompatActivity implements CalendarHeaderView.OnDayClickListener{

    private Toolbar toolbar;
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        container = (FrameLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        openSchedule();
    }

    private void openSchedule(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, ScheduleFragment.newInstance(), "TAG");
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.commit();

        getSupportActionBar().setTitle("Расписание");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickDay(Calendar day) {
        ScheduleFragment fragmentByTag = (ScheduleFragment) getSupportFragmentManager().findFragmentByTag("TAG");
        if (fragmentByTag != null){
            fragmentByTag.onClickDay(day);
        }
    }
}
