package ru.nsu.fit.nsuschedule.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.nsu.fit.nsuschedule.util.RequestInfo;

/**
 * Created by Pavel on 06.10.2016.
 */
public abstract class BaseFragment extends Fragment{

    protected RequestInfo requestInfo = new RequestInfo();
    private ConnectivityChangeReceiver connectivityChangeReceiver;

    @Override
    public void onStart() {
        super.onStart();
        connectivityChangeReceiver = new ConnectivityChangeReceiver();
        getActivity().registerReceiver(connectivityChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }


    @Override
    public void onStop() {
        getActivity().unregisterReceiver(connectivityChangeReceiver);
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class ConnectivityChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
                ConnectivityManager cm =
                        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (null == activeNetwork){
                    return;
                }
                boolean isConnected = activeNetwork.isConnected();
                if (isConnected){
                    if (!requestInfo.isRequestSuccess) {
                        onInternetConnected();
                    }
                }
            }
        }
    }

    public void onInternetConnected(){

    }
}
