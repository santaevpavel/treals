package ru.nsu.fit.nsuschedule.fragment;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.util.Helper;
import ru.nsu.fit.nsuschedule.view.MainScreenItemView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreenFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public static final int CODE_SCHEDULE = 0;
    public static final int CODE_LOGOUT = 1;

    public MainScreenFragment() {
        // Required empty public constructor
    }

    public static MainScreenFragment newInstance() {
        MainScreenFragment fragment = new MainScreenFragment();
        return fragment;
    }

    private MainScreenItemView itemSchedule;
    private MainScreenItemView itemNews;
    private MainScreenItemView itemAcadem;
    private MainScreenItemView itemNotebook;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        root.findViewById(R.id.button_schedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClick(CODE_SCHEDULE);
                }
            }
        });

        /*root.findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClick(CODE_LOGOUT);
                }
            }
        });*/

        TextView changeGroup = (TextView) root.findViewById(R.id.button_change_group);
        changeGroup.setPaintFlags(changeGroup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        changeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClick(CODE_LOGOUT);
                }
            }
        });

        itemSchedule = (MainScreenItemView) root.findViewById(R.id.button_schedule);
        itemNews = (MainScreenItemView) root.findViewById(R.id.button_news);
        itemAcadem = (MainScreenItemView) root.findViewById(R.id.button_academ);
        itemNotebook = (MainScreenItemView) root.findViewById(R.id.button_notebook);

        itemSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClick(CODE_SCHEDULE);
                }
            }
        });

        itemSchedule.setText("Расписание");
        itemNews.setText("Новости НГУ");
        itemAcadem.setText("Академ");
        itemNotebook.setText("Блокнот");

        itemSchedule.setIcon(Helper.tintDrawable(getResources(), R.drawable.ic_today_black_24dp, R.color.main_screen_item_color));
        itemNews.setIcon(Helper.tintDrawable(getResources(), R.drawable.ic_school_black_24dp, R.color.main_screen_item_color));
        itemAcadem.setIcon(Helper.tintDrawable(getResources(), R.drawable.ic_domain_black_24dp, R.color.main_screen_item_color));
        itemNotebook.setIcon(Helper.tintDrawable(getResources(), R.drawable.ic_class_black_24dp, R.color.main_screen_item_color));

        return root;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onClick(int code);
    }
}
