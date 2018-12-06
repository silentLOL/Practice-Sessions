package at.stefanirndorfer.practicesessions.session;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import at.stefanirndorfer.practicesessions.data.Session;
import at.stefanirndorfer.practicesessions.databinding.SessionListItemBinding;
import at.stefanirndorfer.practicesessions.session.input.SessionItemActionListener;
import timber.log.Timber;

public class SessionsRecyclerViewAdapter extends RecyclerView.Adapter<SessionsRecyclerViewAdapter.SessionsViewHolder> {


    private final SessionsViewModel mViewModel;
    private List<Session> mSessions;
    private final SessionItemActionListener mListener;

    public SessionsRecyclerViewAdapter(SessionsViewModel viewModel, SessionItemActionListener listener) {
        this.mViewModel = viewModel;
        mListener = listener;
        mSessions = new ArrayList<>();
        subscribeOnSessionData();
    }

    private void subscribeOnSessionData() {
        mViewModel.getSessions().observeForever(new Observer<List<Session>>() {
            @Override
            public void onChanged(@Nullable List<Session> sessions) {
                if (sessions != null && !sessions.isEmpty()) {
                    Timber.d("Received sessions from viewmodel");
                    setSessions(sessions);
                }
            }
        });
    }

    private void setSessions(List<Session> sessions) {
        this.mSessions = sessions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SessionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SessionListItemBinding binding;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        binding = SessionListItemBinding.inflate(inflater, viewGroup, false);
        binding.sessionNameTv.setText(mSessions.get(i).getName());

        SessionItemActionListener userActionListener = mListener::onSessionItemClicked;
        binding.setListener(userActionListener);
        return new SessionsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionsViewHolder sessionsViewHolder, int i) {
        Session session = mSessions.get(i);
        sessionsViewHolder.bind(session);
    }

    @Override
    public int getItemCount() {
        return mSessions != null ? mSessions.size() : 0;
    }

    public class SessionsViewHolder extends RecyclerView.ViewHolder {

        private final SessionListItemBinding binding;

        public SessionsViewHolder(@NonNull SessionListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Session session) {
            Timber.d("Binding session instance");
            binding.setSession(session);
            binding.executePendingBindings();
        }
    }
}
