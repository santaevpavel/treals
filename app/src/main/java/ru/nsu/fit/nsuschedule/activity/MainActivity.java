package ru.nsu.fit.nsuschedule.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.fragment.LoginFragment;
import ru.nsu.fit.nsuschedule.fragment.MainScreenFragment;
import ru.nsu.fit.nsuschedule.fragment.ScheduleFragment;
import ru.nsu.fit.nsuschedule.util.PreferenceHelper;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
        MainScreenFragment.OnFragmentInteractionListener{

    private Toolbar toolbar;
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        container = (FrameLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setVisibility(View.GONE);
        //setSupportActionBar(toolbar);

        setStatusBarTranslucent(true);

        if (PreferenceHelper.getGroup() == null){
            openLoginFragment();
        } else {
            openMainScreenFragment();
        }
        //openMainScreenFragment();
    }


    private void openLoginFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.container, LoginFragment.newInstance());
        transaction.commit();
        setTransparentToolbar(true);
    }

    private void openMainScreenFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.container, MainScreenFragment.newInstance());
        transaction.commit();
        setTransparentToolbar(true);
    }

    private void openSchedule(){
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, ScheduleFragment.newInstance());
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.addToBackStack(null);
        transaction.commit();
        setTransparentToolbar(false);*/
        Intent i = new Intent(this, ScheduleActivity.class);
        startActivity(i);
    }

    @Override
    public void onLogIn(String groupId, String groupName) {
        PreferenceHelper.setGroup(groupId);
        PreferenceHelper.setGroupName(groupName);
        openMainScreenFragment();
    }


    @Override
    public void onClick(int code) {
        switch (code){
            case MainScreenFragment.CODE_SCHEDULE:
                openSchedule();
                break;
            case MainScreenFragment.CODE_LOGOUT:
                PreferenceHelper.setGroup(null);
                openLoginFragment();
                break;
        }
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private void setTransparentToolbar(boolean isTransparent){
        /*if (isTransparent){
            toolbar.setVisibility(View.GONE);
            toolbar.setBackgroundColor(Color.TRANSPARENT);
            container.setPadding(0, 0, 0, 0);
            //setStatusBarTranslucent(true);
        } else {
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
            container.setPadding(0, toolbar.getHeight(), 0, 0);
            //setStatusBarTranslucent(false);
        }*/
    }
}
