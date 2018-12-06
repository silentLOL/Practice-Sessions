package at.stefanirndorfer.practicesessions.data;

/**
 * Wrapped Exercises which hold their related sessions name
 * and the information if it's the latest or earliest exercise of this session
 */
public class SessionRelatedExercise {

    private boolean mLatest;
    private boolean mEarliest;
    private String mSessionName;
    private Exercise mExercise;

    private SessionRelatedExercise() {
    }

    public static class Builder {
        private boolean mLatest;
        private boolean mEarliest;
        private String mSessionName;
        private Exercise mExercise;

        public Builder setIsLatest(boolean isLatest) {
            this.mLatest = isLatest;
            return this;
        }

        public Builder setIsEarlies(boolean isEarliest) {
            this.mEarliest = isEarliest;
            return this;
        }

        public Builder setSessionName(String sessionName) {
            this.mSessionName = sessionName;
            return this;
        }

        public Builder setExercise(Exercise exercise) {
            this.mExercise = exercise;
            return this;
        }

        public SessionRelatedExercise build() {
            SessionRelatedExercise sessionRelatedExercise = new SessionRelatedExercise();
            sessionRelatedExercise.setEarliest(mEarliest);
            sessionRelatedExercise.setLatest(mLatest);
            sessionRelatedExercise.setExercise(mExercise);
            sessionRelatedExercise.setSessionName(mSessionName);
            return sessionRelatedExercise;
        }

    }

    public String getSessionName() {
        return mSessionName;
    }

    public void setSessionName(String mSessionName) {
        this.mSessionName = mSessionName;
    }

    public boolean isLatest() {
        return mLatest;
    }

    public void setLatest(boolean mLatest) {
        this.mLatest = mLatest;
    }

    public boolean isEarliest() {
        return mEarliest;
    }

    public void setEarliest(boolean mEarliest) {
        this.mEarliest = mEarliest;
    }

    public Exercise getExercise() {
        return mExercise;
    }

    public void setExercise(Exercise mExercise) {
        this.mExercise = mExercise;
    }
}

