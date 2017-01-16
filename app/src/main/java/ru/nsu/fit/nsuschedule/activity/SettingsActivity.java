package ru.nsu.fit.nsuschedule.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.fragment.SettingsFragment;
import ru.nsu.fit.nsuschedule.util.PreferenceHelper;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnFragmentInteractionListener {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Настройки");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        openLoginFragment();
    }

    private void openLoginFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.container, SettingsFragment.newInstance());
        transaction.commit();
        //setTransparentToolbar(true);
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
    public void onLogIn(String groupId, String groupName) {
        PreferenceHelper.setGroup(groupId);
        PreferenceHelper.setGroupName(groupName);
    }
}
