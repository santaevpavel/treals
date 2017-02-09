package ru.nsu.fit.nsuschedule.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.adapter.WeekViewFragmentPagerAdapter;
import ru.nsu.fit.nsuschedule.api.ApiService;
import ru.nsu.fit.nsuschedule.api.ApiServiceHelper;
import ru.nsu.fit.nsuschedule.api.request.GetLessonsRequest;
import ru.nsu.fit.nsuschedule.api.response.LessonsResponse;
import ru.nsu.fit.nsuschedule.model.Lesson;
import ru.nsu.fit.nsuschedule.util.DialogHelper;
import ru.nsu.fit.nsuschedule.util.Helper;
import ru.nsu.fit.nsuschedule.util.PreferenceHelper;
import ru.nsu.fit.nsuschedule.view.CalendarHeaderView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends BaseFragment implements CalendarHeaderView.OnDayClickListener{

    LessonsResponse response;
    private OnFragmentInteractionListener mListener;
    private WeekView weekView;
    private ViewPager viewPager;
    private ProgressDialog progressDialog;
    private Calendar calendarStart;
    private Calendar calendarEnd;
    private WeekViewFragmentPagerAdapter adapter;
    private Calendar selectedDay = Calendar.getInstance();

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onInternetConnected() {
        super.onInternetConnected();
        requestLessons();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Загрузка расписания...");

        viewPager = (ViewPager) root.findViewById(R.id.view_pager);

        weekView = (WeekView) root.findViewById(R.id.weekView);
        weekView.setNumberOfVisibleDays(1);
        weekView.setScrollDuration(200);
        int lineHeight = (int) getResources().getDimension(R.dimen.now_line_height);
        weekView.setNowLineThickness(lineHeight);
        weekView.setmNowCircleRad(getResources().getDimension(R.dimen.now_circle_rad));
        weekView.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
                if (adapter == null || adapter.getCount() != 2){
                    return;
                }
                selectedDay = newFirstVisibleDay;
                boolean isSHowFirst = adapter.update(newFirstVisibleDay);
                viewPager.setCurrentItem(isSHowFirst ? 0 : 1, true);
            }
        });
        calendarStart = Helper.getMondayOfWeek(Calendar.getInstance());

        calendarEnd = (Calendar) calendarStart.clone();
        calendarEnd.add(Calendar.DAY_OF_MONTH, 13);

        weekView.setMinDate(calendarStart);
        weekView.setMaxDate(calendarEnd);

        weekView.setVerticalFlingEnabled(true);
        weekView.setHorizontalFlingEnabled(true);

        weekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                return new ArrayList<>();
            }
        });

        weekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                long id = event.getId();
                DialogHelper.getLessonViewDialog(getContext(), response.lessons.get((int) id)).show();
            }
        });

        requestLessons();

        adapter = new WeekViewFragmentPagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                boolean selectedFirst = adapter.isDateInFirstWeek(selectedDay);
                Calendar dayTo = (Calendar) selectedDay.clone();
                switch (position) {
                    case 0:
                        dayTo.add(Calendar.DAY_OF_YEAR, -7);
                        if (!selectedFirst) {
                            goToDate(dayTo);
                        }
                        break;
                    case 1:
                        dayTo.add(Calendar.DAY_OF_YEAR, 7);
                        if (selectedFirst) {
                            goToDate(dayTo);
                        }
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return root;
    }

    private void showFullStudyDay() {
        int height = 20;
        weekView.setHourHeight(height);
    }

    private void goToDate(Calendar date) {
        selectedDay = date;
        adapter.update(date);
        weekView.goToDate(date);
    }

    private void requestLessons(){
        progressDialog.show();
        String group = PreferenceHelper.getGroup();
        if (group == null){
            Toast.makeText(getContext(), "You didn't choose a group", Toast.LENGTH_SHORT).show();
            progressDialog.hide();
            return;
        }
        if (!requestInfo.tryToRequest()){
            return;
        }

        ApiServiceHelper.getLessons(getContext(), new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                response = (LessonsResponse)
                        resultData.getSerializable(ApiService.KEY_RESPONSE);
                if (progressDialog != null) {
                    progressDialog.hide();
                }
                requestInfo.finish(false);
                if (null != response){
                    if (response.hasError()){
                        Snackbar.make(weekView, response.getErrorMsg(), Snackbar.LENGTH_LONG).show();
                    } else {
                        requestInfo.finish(true);
                        updateLessons();
                    }
                } else {
                    Snackbar.make(weekView, "Ошибка при загрузке расписания", Snackbar.LENGTH_LONG).show();
                }
            }
        }, new GetLessonsRequest(group));
    }

    private void updateLessons(){
        if (null != weekView && response != null && response.lessons != null) {
            weekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
                @Override
                public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                    List<WeekViewEvent> events = new ArrayList<>();
                    for (Lesson l : response.lessons) {
                        for (int i = 0; i < l.getDays().size(); i++) {
                            WeekViewEvent event = getEventForLesson(l, newYear, newMonth - 1, i);
                            if (null != event) {
                                event.setId(response.lessons.indexOf(l));
                                events.add(event);
                            }
                        }
                    }

                    return events;
                }
            });
            weekView.notifyDatasetChanged();
        }
    }

    private WeekViewEvent getEventForLesson(Lesson lesson, int newYear, int newMonth, int dayIdx){
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(lesson.getDays().get(dayIdx));

        if (newMonth != startTime.get(Calendar.MONTH)
                || startTime.before(calendarStart.get(Calendar.DATE))
                || startTime.after(calendarEnd.get(Calendar.DATE))){
            return null;
        }

        startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(lesson.getStartTime().substring(0, 2)));
        startTime.set(Calendar.MINUTE, Integer.valueOf(lesson.getStartTime().substring(3, 5)));

        Calendar endTime = (Calendar) startTime.clone();

        endTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(lesson.getEndTime().substring(0, 2)));
        endTime.set(Calendar.MINUTE, Integer.valueOf(lesson.getEndTime().substring(3, 5)));

        String timeStartStr = lesson.getStartTime().substring(0, 5);
        String timeEndStr = lesson.getEndTime().substring(0, 5);
        String eventText = lesson.getName() + " " + lesson.getRoom() +
                "\n" + timeStartStr + "-" + timeEndStr;
        WeekViewEvent event = new WeekViewEvent(1, eventText
                , startTime, endTime);

        int color = Color.BLUE;
        switch (lesson.getType()){
            case LECTURE:
                color = getContext().getResources().getColor(R.color.lesson_color_lecture);
                break;
            case SEMINAR:
                color = getContext().getResources().getColor(R.color.lesson_color_seminar);
                break;
            case PRACTICUM:
                color = getContext().getResources().getColor(R.color.lesson_color_other);
                break;
            case UNKNOWN:
                color = getContext().getResources().getColor(R.color.lesson_color_other);
                break;
        }
        event.setColor(color);

        return event;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        showFullStudyDay();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_view_one_day:
                weekView.setNumberOfVisibleDays(1);
                break;
            case R.id.action_view_three_day:
                weekView.setNumberOfVisibleDays(3);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        progressDialog.dismiss();
        progressDialog = null;
    }

    @Override
    public void onClickDay(Calendar day) {
        weekView.goToDate(day);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onLogIn2(String department, String group);
    }

}
