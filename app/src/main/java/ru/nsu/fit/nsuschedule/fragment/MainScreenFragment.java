package ru.nsu.fit.nsuschedule.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import ru.nsu.fit.nsuschedule.NsuScheduleApplication;
import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.api.ApiService;
import ru.nsu.fit.nsuschedule.api.ApiServiceHelper;
import ru.nsu.fit.nsuschedule.api.response.WeatherResponse;
import ru.nsu.fit.nsuschedule.model.Weather;
import ru.nsu.fit.nsuschedule.util.ImageLoaderSingleton;
import ru.nsu.fit.nsuschedule.view.MainScreenItemView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainScreenFragment extends BaseFragment implements ImageLoader.ImageListener {

    public static final int CODE_SCHEDULE = 0;
    public static final int CODE_LOGOUT = 1;
    public static final int CODE_SETTINGS = 2;
    public static final int CODE_NEWS = 3;
    public static final int CODE_ACADEM = 4;
    private OnFragmentInteractionListener mListener;
    private MainScreenItemView itemSchedule;
    private MainScreenItemView itemNews;
    private MainScreenItemView itemAcadem;
    private MainScreenItemView itemWeather;
    public MainScreenFragment() {
        // Required empty public constructor
    }

    public static MainScreenFragment newInstance() {
        MainScreenFragment fragment = new MainScreenFragment();
        return fragment;
    }

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
                if (mListener != null){
                    mListener.onClick(CODE_NEWS);
                }
            }
        });

        itemAcadem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClick(CODE_ACADEM);
                }
            }
        });

        itemWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClick(CODE_SETTINGS);
                }
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
                    Weather weather = response.weather;
                    if (weather != null && weather.temp != null && !weather.temp.isEmpty()) {
                        itemWeather.setText(weather.temp + "°C");
                        ImageLoaderSingleton.getInstance(NsuScheduleApplication.getAppContext())
                                .getImageLoader().get(response.weather.img, MainScreenFragment.this);
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

    @Override
    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
        if (response.getBitmap() == null) {
            return;
        }
        ImageView icon = itemWeather.getIcon();
        icon.setVisibility(View.VISIBLE);
        icon.setImageBitmap(response.getBitmap());
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Snackbar.make(itemWeather, "Не удалось загрузить иконку погоды", Snackbar.LENGTH_LONG).show();
        error.printStackTrace();
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
