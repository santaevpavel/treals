package ru.nsu.fit.nsuschedule.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.fragment.ScheduleFragment;

public class ScheduleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        container = (FrameLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
        openSchedule();
    }

    private void openSchedule(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, ScheduleFragment.newInstance());
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}
