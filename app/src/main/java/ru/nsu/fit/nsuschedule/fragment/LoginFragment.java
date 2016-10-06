package ru.nsu.fit.nsuschedule.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import ru.nsu.fit.nsuschedule.R;
import ru.nsu.fit.nsuschedule.api.ApiService;
import ru.nsu.fit.nsuschedule.api.ApiServiceHelper;
import ru.nsu.fit.nsuschedule.api.response.GroupListResponse;
import ru.nsu.fit.nsuschedule.model.Group;
import ru.nsu.fit.nsuschedule.model.GroupSuggestion;
import ru.nsu.fit.nsuschedule.util.Helper;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    public static final int MAX_SUGGESTIONS_COUNT = 5;
    private OnFragmentInteractionListener mListener;

    private GroupListResponse response;
    private String groupId;
    private FloatingSearchView searchView;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        //Helper.addStatusBarPaddingToView(root.findViewById(R.id.content_frame), getContext());
        Helper.addStatusBarPaddingToView(root.findViewById(R.id.floating_search_view), getContext());

        final Context context = getContext();

        ApiServiceHelper.getGroups(context, new android.support.v4.os.ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if (!isAdded()){
                    return;
                }
                response = (GroupListResponse)
                        resultData.getSerializable(ApiService.KEY_RESPONSE);
                if (response == null){
                    Toast.makeText(getContext(), "Error due loading groups", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.hasError()){
                    Snackbar.make(searchView, response.getErrorMsg(), Snackbar.LENGTH_LONG).show();
                }
                updateSuggestions();
            }

        });
        root.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearch();
            }
        });

        searchView = (FloatingSearchView) root.findViewById(R.id.floating_search_view);
        searchView.setSearchFocusable(true);
        updateSuggestions();
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                searchView.setOnFocusChangeListener((FloatingSearchView.OnFocusChangeListener) null);
                hideSearch(true, ((GroupSuggestion)searchSuggestion).getGroup());
            }

            @Override
            public void onSearchAction(String currentQuery) {}
        });

        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {}

            @Override
            public void onFocusCleared() {
                hideSearch(false, null);
                searchView.clearQuery();
            }
        });
        return root;
    }

    private void updateSuggestions(){
        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                ArrayList<SearchSuggestion> suggestions = new ArrayList<>();
                if (response == null || response.groups == null || response.groups.isEmpty()){
                    return;
                } else {
                    List<Group> groups = response.groups;
                    for (int i = 0; i < groups.size() && MAX_SUGGESTIONS_COUNT >= suggestions.size(); i++) {
                        if (0 == groups.get(i).getName().indexOf(newQuery)) {
                            suggestions.add(new GroupSuggestion(groups.get(i)));
                        }
                    }
                }
                searchView.swapSuggestions(suggestions);
            }
        });

    }

    private void showSearch(){
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y / 3;

        searchView.setAlpha(0);
        searchView.setVisibility(View.VISIBLE);
        searchView.setY(height);
        searchView.animate().
                alpha(1.0f).
                translationYBy(height * -1).
                setDuration(100).
                setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                searchView.setVisibility(View.VISIBLE);
                searchView.setSearchFocused(true);
            }
        });

    }

    private void hideSearch(final boolean openMainScreen, final Group group){
        Helper.hideKeyboard(getActivity().getWindow());
        searchView.animate().alpha(0.0f).setDuration(100).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                searchView.setVisibility(View.GONE);
                if (null != mListener && openMainScreen){
                    mListener.onLogIn(group.getId(), group.getName());
                }
            }
        });
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
        void onLogIn(String groupId, String groupName);
    }

}
