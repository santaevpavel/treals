package ru.nsu.fit.nsuschedule.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.api.ApiService;
import ru.nsu.fit.nsuschedule.api.ApiServiceHelper;
import ru.nsu.fit.nsuschedule.api.response.WeatherResponse;
import ru.nsu.fit.nsuschedule.util.Helper;
import ru.nsu.fit.nsuschedule.util.PreferenceHelper;
import ru.nsu.fit.nsuschedule.util.RequestInfo;
import ru.nsu.fit.nsuschedule.view.MainScreenItemView;
import ru.nsu.fit.nsuschedule.view.WeatherView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreenFragment extends BaseFragment {

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
    private MainScreenItemView itemWeather;

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

        //TextView changeGroup = (TextView) root.findViewById(R.id.button_change_group);
        String groupName = PreferenceHelper.getGroupName();
        /*changeGroup.setText(null != groupName ? groupName : "Сменить группу");
        changeGroup.setPaintFlags(changeGroup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        changeGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClick(CODE_LOGOUT);
                }
            }
        });*/

        itemWeather = (MainScreenItemView) root.findViewById(R.id.button_weather);
        itemSchedule = (MainScreenItemView) root.findViewById(R.id.button_schedule);
        itemNews = (MainScreenItemView) root.findViewById(R.id.button_news);
        itemAcadem = (MainScreenItemView) root.findViewById(R.id.button_academ);


        itemSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClick(CODE_SCHEDULE);
                }
            }
        });

        itemNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        itemAcadem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        itemWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        itemSchedule.setText("РАСПИСАНИЕ");
        itemNews.setText("НОВОСТИ НГУ");
        itemAcadem.setText("АКАДЕМ");
        itemWeather.setText("ПОГОДА");

        itemSchedule.setBackground(R.drawable.menu_img_schedule);
        itemNews.setBackground(R.drawable.menu_img_nsu_news);
        itemAcadem.setBackground(R.drawable.menu_img_academ);
        itemWeather.setBackground(R.drawable.menu_img_weather);

        requestWeather();
        return root;
    }

    private void requestWeather(){
        //weatherView.showProgress();
        if (!requestInfo.tryToRequest()){
            return;
        }
        ApiServiceHelper.getWeather(getContext(), new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                requestInfo.finish(false);
                if (!isAdded()){
                    return;
                }
                WeatherResponse response = (WeatherResponse)
                        resultData.getSerializable(ApiService.KEY_RESPONSE);
                if (response == null){
                    showErrorInWeather();
                    return;
                }
                if (response.hasError()){
                    Snackbar.make(itemWeather, response.getErrorMsg(), Snackbar.LENGTH_LONG).show();
                    showErrorInWeather();
                } else {
                    String temp = response.temp;
                    if (temp != null && !temp.isEmpty()) {
                        itemWeather.setText(temp + "°C ДОЖДЬ");
                        requestInfo.finish(true);
                    } else {
                        showErrorInWeather();
                    }
                }
            }
        });
    }

    @Override
    public void onInternetConnected() {
        super.onInternetConnected();
        requestWeather();
    }

    private void showErrorInWeather(){
        itemWeather.setText("Погода \nнедоступна");
        requestInfo.finish(false);
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
