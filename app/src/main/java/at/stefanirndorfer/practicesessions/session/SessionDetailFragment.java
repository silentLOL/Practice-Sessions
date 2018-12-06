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

import java.util.Objects;

import at.stefanirndorfer.practicesessions.databinding.FragmentSessionDetailBinding;
import at.stefanirndorfer.practicesessions.session.adapter.ExercisesRecyclerViewAdapter;
import at.stefanirndorfer.practicesessions.util.ViewModelFactory;
import timber.log.Timber;

public class SessionDetailFragment extends Fragment {

    private static final String SESSION_ID_KEY = "session_id";
    private static final int DEFAULT_SESSION_ID = 0;

    private RecyclerView mRecyclerViewExercises;
    private LinearLayoutManager mLayoutManager;
    private ExercisesRecyclerViewAdapter mAdapter;
    FragmentSessionDetailBinding mBinding;
    private SessionDetailViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSessionDetailBinding.inflate(inflater, container, false);
        Timber.d("onCreateView");
        mViewModel = obtainViewModel((SessionActivity) Objects.requireNonNull(this.getActivity()));
        mBinding.setViewModel(mViewModel);

        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        assert getArguments() != null;
        int sessionId = getArguments().getInt(SESSION_ID_KEY, DEFAULT_SESSION_ID);
        mViewModel.start(sessionId);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("onActivityCreated");
        setupRecyclerViewAdapter();
    }

    private void setupRecyclerViewAdapter() {
        Timber.d("Setting up exercise recyclerView");
        mRecyclerViewExercises = mBinding.recyclerViewExerciseRv;
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerViewExercises.setLayoutManager(mLayoutManager);
        mRecyclerViewExercises.setHasFixedSize(true);
        mAdapter = new ExercisesRecyclerViewAdapter(mViewModel);
        mRecyclerViewExercises.setAdapter(mAdapter);
    }


    public static SessionDetailFragment newInstance(int sessionId) {
        SessionDetailFragment sessionDetailFragment = new SessionDetailFragment();
        Bundle args = new Bundle();
        args.putInt(SESSION_ID_KEY, sessionId);
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
