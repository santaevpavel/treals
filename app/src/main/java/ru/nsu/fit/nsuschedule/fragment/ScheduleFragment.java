package ru.nsu.fit.nsuschedule.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.api.ApiService;
import ru.nsu.fit.nsuschedule.api.ApiServiceHelper;
import ru.nsu.fit.nsuschedule.api.request.GetGroupsRequest;
import ru.nsu.fit.nsuschedule.api.request.GetLessonsRequest;
import ru.nsu.fit.nsuschedule.api.response.DepartmentListResponse;
import ru.nsu.fit.nsuschedule.api.response.GroupListResponse;
import ru.nsu.fit.nsuschedule.api.response.LessonsResponse;
import ru.nsu.fit.nsuschedule.model.Department;
import ru.nsu.fit.nsuschedule.model.Group;
import ru.nsu.fit.nsuschedule.model.Lesson;
import ru.nsu.fit.nsuschedule.util.DialogHelper;
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
public class ScheduleFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private WeekView weekView;
    private CalendarHeaderView calendarHeaderView;

    private ProgressDialog progressDialog;

    LessonsResponse response;

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

        calendarHeaderView = (CalendarHeaderView) root.findViewById(R.id.weekViewHeader);
        calendarHeaderView.update(Calendar.getInstance());

        calendarHeaderView.setOnDayClickListener(new CalendarHeaderView.OnDayClickListener() {
            @Override
            public void onClickDay(Calendar day) {
                weekView.goToDate(day);
            }
        });
        weekView = (WeekView) root.findViewById(R.id.weekView);
        weekView.setNumberOfVisibleDays(1);
        weekView.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(Calendar newFirstVisibleDay, Calendar oldFirstVisibleDay) {
                calendarHeaderView.update(newFirstVisibleDay);
            }
        });
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.setTimeInMillis(System.currentTimeMillis());

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.setTimeInMillis(System.currentTimeMillis());
        calendarEnd.add(Calendar.DAY_OF_MONTH, 14);

        weekView.setMinDate(calendarStart);
        weekView.setMaxDate(calendarEnd);

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

        /*weekView.setWeekViewLoader(new WeekViewLoader() {
            @Override
            public double toWeekViewPeriodIndex(Calendar instance) {
                return 0;
            }

            @Override
            public List<? extends WeekViewEvent> onLoad(int periodIndex) {
                List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

                Calendar now = Calendar.getInstance();
                now.setTimeInMillis(System.currentTimeMillis());
                Calendar end = Calendar.getInstance();
                now.setTimeInMillis(System.currentTimeMillis()+ 1000 * 60 * 60 * 2);

                //events.add(new WeekViewEvent(0, "Пара", now, end));

                return events;

            }
        });
*/
        return root;
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
                progressDialog.hide();
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

                            WeekViewEvent event = getEventForLesson(l, newYear, newMonth, i);
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

        if (newMonth != startTime.get(Calendar.MONTH)){
            return null;
        }

        startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(lesson.getStartTime().substring(0, 2)));
        startTime.set(Calendar.MINUTE, Integer.valueOf(lesson.getStartTime().substring(3, 5)));

        Calendar endTime = (Calendar) startTime.clone();

        endTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(lesson.getEndTime().substring(0, 2)));
        endTime.set(Calendar.MINUTE, Integer.valueOf(lesson.getEndTime().substring(3, 5)));

        WeekViewEvent event = new WeekViewEvent(1, lesson.getName() + " " + lesson.getRoom()
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
        Calendar now = Calendar.getInstance();
        weekView.goToHour(now.get(Calendar.HOUR_OF_DAY));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_schedule, menu);
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        progressDialog.dismiss();
        progressDialog = null;
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
