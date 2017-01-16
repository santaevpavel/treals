package ru.nsu.fit.nsuschedule.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;

import ru.nsu.fit.nsuschedule.fragment.ScheduleFragment;
import ru.nsu.fit.nsuschedule.fragment.WeekViewHeaderFragment;

/**
 * Created by Pavel on 15.01.2017.
 */
public class WeekViewFragmentPagerAdapter extends FragmentPagerAdapter {

    private WeekViewHeaderFragment first;
    private WeekViewHeaderFragment second;

    public WeekViewFragmentPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        WeekViewHeaderFragment instance = WeekViewHeaderFragment.getInstance(position);
        if (0 == position){
            first = instance;
        } else {
            second = instance;
        }
        return instance;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public boolean update(Calendar newFirstVisibleDay) {
        boolean isShowFirstWeek = true;
        if (first != null && first.getCalendarHeaderView() != null){
            isShowFirstWeek = first.getCalendarHeaderView().update(true, newFirstVisibleDay);
        }
        if (second != null && second.getCalendarHeaderView() != null){
            second.getCalendarHeaderView().update(false, newFirstVisibleDay);
        }
        return isShowFirstWeek;
    }
}
