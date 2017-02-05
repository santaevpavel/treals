package ru.nsu.fit.nsuschedule.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.fragment.LoginFragment;
import ru.nsu.fit.nsuschedule.fragment.MainScreenFragment;
import ru.nsu.fit.nsuschedule.util.PreferenceHelper;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener,
        MainScreenFragment.OnFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Toolbar toolbar;
    private FrameLayout container;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        container = (FrameLayout) findViewById(R.id.container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        setStatusBarTranslucent(true);

        if (PreferenceHelper.getGroup() == null) {
            openLoginFragment();
        } else {
            openMainScreenFragment();
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        PreferenceHelper.setLocation(-1, -1);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            PreferenceHelper.setLocation(location.getLatitude(), location.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
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

    private void openSettings(){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    private void openNews(){
        Intent i = new Intent(this, NewsActivity.class);
        startActivity(i);
    }

    private void openAcadem(){
        Intent i = new Intent(this, AcademActivity.class);
        startActivity(i);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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
            case MainScreenFragment.CODE_SETTINGS:
                openSettings();
                break;
            case MainScreenFragment.CODE_NEWS:
                openNews();
                break;
            case MainScreenFragment.CODE_ACADEM:
                openAcadem();
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
