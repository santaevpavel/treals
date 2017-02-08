package ru.nsu.fit.nsuschedule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import ru.nsu.fit.nsuschedule.R;

/**
 * Created by Pavel on 03.10.2016.
 */
public class CalendarHeaderView extends FrameLayout{

    private TextView daysTextViews[] = new TextView[7];
    private ImageView daysBgs[] = new ImageView[7];
    private View daysLayout[] = new View[7];

    private OnDayClickListener onDayClickListener;

    public CalendarHeaderView(Context context) {
        super(context);
        init();
    }

    public CalendarHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnDayClickListener(OnDayClickListener listener){
        onDayClickListener = listener;
    }

    public boolean update(boolean isFirst, Calendar toSelect){
        Calendar today = Calendar.getInstance();
        today.setFirstDayOfWeek(Calendar.MONDAY);
        toSelect.setFirstDayOfWeek(Calendar.MONDAY);
        int dayOfWeek = toSelect.get(Calendar.DAY_OF_WEEK);
        int mondayOffset = ((dayOfWeek - 2) + 7) % 7;
        int mondayOffsetToday = ((today.get(Calendar.DAY_OF_WEEK) - 2) + 7) % 7;
        Calendar monday = (Calendar) toSelect.clone();
        Calendar mondayToday = (Calendar) today.clone();
        monday.add(Calendar.DATE, -1 * mondayOffset);
        mondayToday.add(Calendar.DATE, -1 * mondayOffsetToday);
        boolean isInFirstWeek = monday.get(Calendar.DAY_OF_YEAR) == mondayToday.get(Calendar.DAY_OF_YEAR);
        boolean isShowSelectedDay = isFirst == isInFirstWeek;

        if (isFirst){
            monday.add(Calendar.DATE, (isInFirstWeek ? 0 : -1) * 7);
        } else {
            monday.add(Calendar.DATE, (isInFirstWeek ? 1 : 0) * 7);
        }
        for (int i = 0; i < 7; i++){
            int bgId = (i != mondayOffset || !isShowSelectedDay)
                    ? R.drawable.calendar_header_day_bg
                    : R.drawable.calendar_header_day_bg_selected;
            int textColorId = (i != mondayOffset || !isShowSelectedDay)
                    ? R.color.calendar_header_day_num_text_color
                    : R.color.calendar_header_day_num_text_color_selected;
            daysBgs[i].setImageResource(bgId);
            daysTextViews[i].setText("" + monday.get(Calendar.DATE));

            if (today.get(Calendar.DAY_OF_YEAR) == monday.get(Calendar.DAY_OF_YEAR)){
                if (i == mondayOffset && isShowSelectedDay) {
                    daysBgs[i].setBackgroundResource(R.drawable.calendar_header_day_point_bg_today);
                }
            }
            daysTextViews[i].setTextColor(getContext().getResources().getColor(textColorId));


            final Calendar dayDate = (Calendar) monday.clone();
            daysLayout[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDayClickListener != null){
                        onDayClickListener.onClickDay(dayDate);
                    }
                }
            });

            monday.add(Calendar.DATE, 1);
        }

        return isInFirstWeek;
    }

    public boolean isInFirstWeek(boolean isFirst, Calendar toSelect) {
        Calendar today = Calendar.getInstance();
        today.setFirstDayOfWeek(Calendar.MONDAY);
        toSelect.setFirstDayOfWeek(Calendar.MONDAY);
        int dayOfWeek = toSelect.get(Calendar.DAY_OF_WEEK);
        int mondayOffset = ((dayOfWeek - 2) + 7) % 7;
        int mondayOffsetToday = ((today.get(Calendar.DAY_OF_WEEK) - 2) + 7) % 7;
        Calendar monday = (Calendar) toSelect.clone();
        Calendar mondayToday = (Calendar) today.clone();
        monday.add(Calendar.DATE, -1 * mondayOffset);
        mondayToday.add(Calendar.DATE, -1 * mondayOffsetToday);
        return monday.get(Calendar.DAY_OF_YEAR) == mondayToday.get(Calendar.DAY_OF_YEAR);
    }

    private void init(){
        View root = View.inflate(getContext(), R.layout.calendar_header_layout, this);
        TextView day = (TextView) root.findViewById(R.id.day1);
        daysTextViews[0] = day;
        day = (TextView) root.findViewById(R.id.day2);
        daysTextViews[1] = day;
        day = (TextView) root.findViewById(R.id.day3);
        daysTextViews[2] = day;
        day = (TextView) root.findViewById(R.id.day4);
        daysTextViews[3] = day;
        day = (TextView) root.findViewById(R.id.day5);
        daysTextViews[4] = day;
        day = (TextView) root.findViewById(R.id.day6);
        daysTextViews[5] = day;
        day = (TextView) root.findViewById(R.id.day7);
        daysTextViews[6] = day;

        ImageView bg = (ImageView) root.findViewById(R.id.day1bg);
        daysBgs[0] = bg;
        bg = (ImageView) root.findViewById(R.id.day2bg);
        daysBgs[1] = bg;
        bg = (ImageView) root.findViewById(R.id.day3bg);
        daysBgs[2] = bg;
        bg = (ImageView) root.findViewById(R.id.day4bg);
        daysBgs[3] = bg;
        bg = (ImageView) root.findViewById(R.id.day5bg);
        daysBgs[4] = bg;
        bg = (ImageView) root.findViewById(R.id.day6bg);
        daysBgs[5] = bg;
        bg = (ImageView) root.findViewById(R.id.day7bg);
        daysBgs[6] = bg;

        View dayLayout = root.findViewById(R.id.day1layout);
        daysLayout[0] = dayLayout;
        dayLayout = root.findViewById(R.id.day2layout);
        daysLayout[1] = dayLayout;
        dayLayout = root.findViewById(R.id.day3layout);
        daysLayout[2] = dayLayout;
        dayLayout = root.findViewById(R.id.day4layout);
        daysLayout[3] = dayLayout;
        dayLayout = root.findViewById(R.id.day5layout);
        daysLayout[4] = dayLayout;
        dayLayout = root.findViewById(R.id.day6layout);
        daysLayout[5] = dayLayout;
        dayLayout = root.findViewById(R.id.day7layout);
        daysLayout[6] = dayLayout;

    }

    public interface OnDayClickListener {
        void onClickDay(Calendar day);
    }



}
