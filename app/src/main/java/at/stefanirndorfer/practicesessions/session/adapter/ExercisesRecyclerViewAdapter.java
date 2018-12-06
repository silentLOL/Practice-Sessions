package at.stefanirndorfer.practicesessions.session.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import at.stefanirndorfer.practicesessions.data.Exercise;
import at.stefanirndorfer.practicesessions.databinding.ExerciseListItemBinding;
import timber.log.Timber;

public class ExercisesRecyclerViewAdapter extends RecyclerView.Adapter<ExercisesRecyclerViewAdapter.ExercisesViewHolder> {


    private final SessionDetailViewModel mViewModel;
    private List<Exercise> mExercises;

    public ExercisesRecyclerViewAdapter(SessionDetailViewModel viewModel) {
        this.mViewModel = viewModel;
        mExercises = new ArrayList<>();
        subscribeOnSessionData();
    }

    private void subscribeOnSessionData() {
        mViewModel.getSession().observeForever(session -> {
            if (session != null && session.getExercises() != null) {
                Timber.d("Recieved session object from repository");
                setExercises(session.getExercises());
            }
        });
    }

    private void setExercises(List<Exercise> exercises) {
        this.mExercises = exercises;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ExercisesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ExerciseListItemBinding binding;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        binding = ExerciseListItemBinding.inflate(inflater, viewGroup, false);
        Exercise currElement = mExercises.get(i);
        binding.exerciseIdTv.setText(currElement.getExerciseId());
        binding.exerciseNameTv.setText(currElement.getName());
        binding.praticedAtBpmTv.setText(String.valueOf(currElement.getPracticedAtBpm()));


        return new ExercisesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesViewHolder exercisesViewHolder, int i) {
        Exercise exercise = mExercises.get(i);
        exercisesViewHolder.bind(exercise);
    }

    @Override
    public int getItemCount() {
        return mExercises != null ? mExercises.size() : 0;
    }

    public class ExercisesViewHolder extends RecyclerView.ViewHolder {

        private final ExerciseListItemBinding binding;

        public ExercisesViewHolder(@NonNull ExerciseListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Exercise exercise) {
            Timber.d("Binding exercise instance");
            binding.setExercise(exercise);
            binding.executePendingBindings();
        }
    }
}
