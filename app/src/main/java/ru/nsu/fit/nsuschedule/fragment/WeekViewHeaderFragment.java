package ru.nsu.fit.nsuschedule.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.view.CalendarHeaderView;

/**
 * Created by Pavel on 15.01.2017.
 */

public class WeekViewHeaderFragment extends Fragment implements CalendarHeaderView.OnDayClickListener {

    public static final String KEY_NUM = "KEY_NUM";
    private CalendarHeaderView calendarHeaderView;

    public static WeekViewHeaderFragment getInstance(int num){
        WeekViewHeaderFragment fragment = new WeekViewHeaderFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_NUM, num);
        fragment.setArguments(args);
        return fragment;
    }

    private CalendarHeaderView.OnDayClickListener onDayClickListener;
    private int num;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        num = getArguments().getInt(KEY_NUM);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onDayClickListener = (CalendarHeaderView.OnDayClickListener) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        calendarHeaderView = (CalendarHeaderView) inflater.inflate(R.layout.fragment_week_view_header, container, false);
        ViewGroup.LayoutParams layoutParams = calendarHeaderView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        calendarHeaderView.setLayoutParams(layoutParams);
        calendarHeaderView.update(0 == num , Calendar.getInstance());
        calendarHeaderView.setOnDayClickListener(this);
        return calendarHeaderView;
    }

    public CalendarHeaderView getCalendarHeaderView() {
        return calendarHeaderView;
    }

    @Override
    public void onClickDay(Calendar day) {
        onDayClickListener.onClickDay(day);
    }
}
