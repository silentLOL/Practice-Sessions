package at.stefanirndorfer.practicesessions.session.adapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;

import java.util.ArrayList;
import java.util.List;

import at.stefanirndorfer.practicesessions.data.SessionRelatedExercise;
import at.stefanirndorfer.practicesessions.data.SessionRelatedExercise.ItemType;
import at.stefanirndorfer.practicesessions.databinding.ExerciseListItemBinding;
import at.stefanirndorfer.practicesessions.databinding.SeperatorListItemBinding;
import at.stefanirndorfer.practicesessions.databinding.SessionListItemBinding;
import at.stefanirndorfer.practicesessions.session.SessionsViewModel;
import at.stefanirndorfer.practicesessions.util.Constants;
import timber.log.Timber;

public class ExercisesRecyclerViewAdapter extends RecyclerView.Adapter<ExercisesRecyclerViewAdapter.ExerciseItemWrapperViewHolder> {


    private final SessionsViewModel mViewModel;
    private List<SessionRelatedExercise> mItemData;

    public ExercisesRecyclerViewAdapter(SessionsViewModel viewModel) {
        this.mViewModel = viewModel;
        mItemData = new ArrayList<>();
        subscribeData();
    }

    private void subscribeData() {
        mViewModel.getSessionRelatedExercises().observeForever(exerciseData -> {
            if (exerciseData != null && !exerciseData.isEmpty()) {
                Timber.d("Received data from viewmodel");
                setData(exerciseData);
            }
        });
    }

    private void setData(List<SessionRelatedExercise> data) {
        this.mItemData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemData.get(position).getItemType().ordinal();
    }


    @NonNull
    @Override
    public ExerciseItemWrapperViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        ItemType itemViewType = ItemType.values()[viewType];
        switch (itemViewType) {
            case Session:
                SessionListItemBinding sessionBinding;
                sessionBinding = SessionListItemBinding.inflate(inflater, viewGroup, false);
                return new SessionInformationViewHolder(sessionBinding);
            case Exercise:
                ExerciseListItemBinding exerciseBinding;
                exerciseBinding = ExerciseListItemBinding.inflate(inflater, viewGroup, false);
                return new ExerciseViewHolder(exerciseBinding);
            case Seperator:
                SeperatorListItemBinding seperatorBinding;
                seperatorBinding = SeperatorListItemBinding.inflate(inflater, viewGroup, false);
                return new SeperatorViewHolder(seperatorBinding);
            default:
                throw new IllegalArgumentException("View Type could not be mapped.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseItemWrapperViewHolder exerciseItemWrapperViewHolder, int i) {
        SessionRelatedExercise itemData = mItemData.get(i);
        exerciseItemWrapperViewHolder.bind(itemData);
    }

    @Override
    public int getItemCount() {
        return mItemData != null ? mItemData.size() : 0;
    }

    public class ExerciseItemWrapperViewHolder extends RecyclerView.ViewHolder {

        public ExerciseItemWrapperViewHolder(@NonNull ViewDataBinding binding) {
            super(binding.getRoot());
        }

        public void bind(SessionRelatedExercise itemData) {
            // intentionally left blank
        }
    }

    public class ExerciseViewHolder extends ExerciseItemWrapperViewHolder {
        private ExerciseListItemBinding binding;

        public ExerciseViewHolder(@NonNull ExerciseListItemBinding binding) {
            super(binding);
            this.binding = binding;
        }

        @Override
        public void bind(SessionRelatedExercise itemData) {
            Timber.d("Binding  exercise item");

            //requesting image
            String imageUrl = Constants.EXERCISE_IMAGE_BASE_URL + itemData.getExercise().getExerciseId() + Constants.EXERCISE_IMAGE_FILE_EXTENTION;

            mViewModel.loadExerciseImage(binding.exerciseImageIv, imageUrl,
                    new Callback() {
                        @Override
                        public void onSuccess() {
                            Timber.d("success loading image for exercise.");
                            binding.exerciseImageIv.setVisibility(View.VISIBLE);
                            binding.exercisePlaceholderIv.setVisibility(View.GONE);
                            binding.exerciseImagePb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            Timber.e("Error loading image for exercise.");
                            binding.exerciseImageIv.setVisibility(View.GONE);
                            binding.exercisePlaceholderIv.setVisibility(View.VISIBLE);
                            binding.exerciseImagePb.setVisibility(View.GONE);
                        }
                    }
            );
            binding.exerciseImagePb.setVisibility(View.VISIBLE);



            binding.setExercise(itemData.getExercise());
        }
    }


    public class SessionInformationViewHolder extends ExerciseItemWrapperViewHolder {
        private SessionListItemBinding binding;

        public SessionInformationViewHolder(@NonNull SessionListItemBinding binding) {
            super(binding);
            this.binding = binding;
        }

        @Override
        public void bind(SessionRelatedExercise itemData) {

            Timber.d("Binding session item");
            binding.setSessionRelatedExercise(itemData);
        }
    }

    public class SeperatorViewHolder extends ExerciseItemWrapperViewHolder {

        private SeperatorListItemBinding binding;

        public SeperatorViewHolder(@NonNull SeperatorListItemBinding binding) {
            super(binding);
            this.binding = binding;
        }

        @Override
        public void bind(SessionRelatedExercise itemData) {
            Timber.d("Binding seperator item");
            // nothing to bind
        }
    }
}


