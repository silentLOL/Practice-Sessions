package at.stefanirndorfer.practicesessions.session;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.stefanirndorfer.practicesessions.R;
import at.stefanirndorfer.practicesessions.databinding.FragmentSessionsListBinding;
import at.stefanirndorfer.practicesessions.util.ViewModelFactory;
import timber.log.Timber;

public class SessionDetailFragment extends Fragment {

    private static final String SESSION_NAME_KEY = "session_name";

    private RecyclerView mRecyclerViewExercises;
    private LinearLayoutManager mLayoutManager;
    private ExercisesRecyclerViewAdapter mAdapter;
    FragmentSessionsListBinding mBinding;
    private SessionDetailViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSessionsListBinding.inflate(inflater, container, false);
        Timber.d("onCreateView");
        mViewModel = obtainViewModel((SessionActivity) this.getActivity());
        mBinding.setViewModel(mViewModel);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        assert getArguments() != null;
        String sessionName = getArguments().getString(SESSION_NAME_KEY, getResources().getString(R.string.default_session_name_value));
        mViewModel.start(sessionName);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("onActivityCreated");
        setupRecyclerViewAdapter();
    }

    private void setupRecyclerViewAdapter() {
        Timber.d("Setting up sessions recyclerView");
        mRecyclerViewExercises = mBinding.recyclerViewExercisesListRv;
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerViewExercises.setLayoutManager(mLayoutManager);
        mRecyclerViewExercises.setHasFixedSize(true);
        mAdapter = new ExercisesRecyclerViewAdapter(mViewModel);
        mRecyclerViewExercises.setAdapter(mAdapter);
    }


    public static SessionDetailFragment newInstance(String sessionName) {
        SessionDetailFragment sessionDetailFragment = new SessionDetailFragment();
        Bundle args = new Bundle();
        args.putString(SESSION_NAME_KEY, sessionName);
        sessionDetailFragment.setArguments(args);
        return sessionDetailFragment;
    }

    private SessionDetailViewModel obtainViewModel(SessionActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        SessionDetailViewModel viewModel = ViewModelProviders.of(activity, factory).get(SessionDetailViewModel.class);
        return viewModel;
    }

}
