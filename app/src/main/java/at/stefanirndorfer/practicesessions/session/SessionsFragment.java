package at.stefanirndorfer.practicesessions.session;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import at.stefanirndorfer.practicesessions.databinding.FragmentSessionsListBinding;
import at.stefanirndorfer.practicesessions.session.adapter.SessionsRecyclerViewAdapter;
import at.stefanirndorfer.practicesessions.session.input.SessionItemActionListener;
import at.stefanirndorfer.practicesessions.util.ViewModelFactory;
import timber.log.Timber;

public class SessionsFragment extends Fragment {

    FragmentSessionsListBinding mBinding;
    private SessionsViewModel mViewModel;
    private RecyclerView mRecyclerViewSessions;
    private LinearLayoutManager mLayoutManager;
    private SessionsRecyclerViewAdapter mAdapter;
    private SessionItemActionListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSessionsListBinding.inflate(inflater, container, false);
        Timber.d("onCreateView");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false); /* there is nowhere to go to from this fragment */
        mViewModel = obtainViewModel((SessionActivity) this.getActivity());
        mBinding.setViewModel(mViewModel);
        mViewModel.start();
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SessionItemActionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement SessionItemActionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("onActivityCreated");
        setupSessionsRecyclerViewAdapter();
    }

    private void setupSessionsRecyclerViewAdapter() {
        Timber.d("Setting up sessions recyclerView");
        mRecyclerViewSessions = mBinding.recyclerViewSessionsListRv;
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerViewSessions.setLayoutManager(mLayoutManager);
        mRecyclerViewSessions.setHasFixedSize(true);
        mAdapter = new SessionsRecyclerViewAdapter(mViewModel, mListener);
        mRecyclerViewSessions.setAdapter(mAdapter);
    }


    private SessionsViewModel obtainViewModel(SessionActivity activity) {
        // Use a Factory to inject dependencies into the ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        SessionsViewModel viewModel = ViewModelProviders.of(activity, factory).get(SessionsViewModel.class);
        return viewModel;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.getSessions().removeObservers(this);
    }
}
