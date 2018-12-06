package at.stefanirndorfer.practicesessions.session;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.stefanirndorfer.practicesessions.R;

public class SessionDetailFragment extends Fragment {

    private static final String SESSION_NAME_KEY = "session_name";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assert getArguments() != null;
        getArguments().getString(SESSION_NAME_KEY, getResources().getString(R.string.default_session_name_value));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static SessionDetailFragment newInstance(String sessionName) {
        SessionDetailFragment sessionDetailFragment = new SessionDetailFragment();
        Bundle args = new Bundle();
        args.putString(SESSION_NAME_KEY, sessionName);
        sessionDetailFragment.setArguments(args);
        return sessionDetailFragment;
    }

}
