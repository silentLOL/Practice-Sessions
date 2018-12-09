package at.stefanirndorfer.practicesessions.data;

/**
 * Wrapped Exercises which hold their related sessions name
 * and the information if it's the latest or earliest exercise of this session
 */
public class SessionRelatedExercise {

    // enum to allow distinction about item type in the adapter class
    public enum ItemType {
        Session, Exercise, Seperator
    }

    private Integer mPracticePerformance;
    private String mSessionName;
    private Exercise mExercise;
    private String mPracticedOnDate;
    private ItemType mItemType;
    private boolean mIsTopPerformance; /* to avoid a static variable */


    private SessionRelatedExercise() {
    }

    public static class Builder {
        private String mSessionName;
        private Exercise mExercise;
        private ItemType mItemType;
        private String mPracticedOnDate;
        private Integer mPracticePerformance;
        private boolean mIsTopPerformance; /* to avoid a static variable */

        public Builder setItemType(ItemType itemType) {
            this.mItemType = itemType;
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

        public Builder setPracticedOnDate(String practicedOnDateAsString) {
            this.mPracticedOnDate = practicedOnDateAsString;
            return this;
        }

        public Builder setPracticePerformance(Integer practicePerformance) {
            this.mPracticePerformance = practicePerformance;
            return this;
        }

        public Builder setIsTopPerformance(boolean mIsTopPerformance) {
            this.mIsTopPerformance = mIsTopPerformance;
            return this;
        }

        public SessionRelatedExercise build() {
            SessionRelatedExercise sessionRelatedExercise = new SessionRelatedExercise();
            sessionRelatedExercise.setExercise(mExercise);
            sessionRelatedExercise.setSessionName(mSessionName);
            sessionRelatedExercise.setItemType(mItemType);
            sessionRelatedExercise.setPracticedOnDate(mPracticedOnDate);
            sessionRelatedExercise.setPracticePerformance(mPracticePerformance);
            sessionRelatedExercise.setIsTopPerformance(mIsTopPerformance);
            return sessionRelatedExercise;
        }


    }

    private void setIsTopPerformance(boolean isTopPerformance) {
        this.mIsTopPerformance = isTopPerformance;
    }

    public boolean isTopPerformance() {
        return mIsTopPerformance;
    }

    private void setPracticePerformance(Integer practicePerformance) {
        if (mPracticePerformance != null) {
            this.mPracticePerformance = practicePerformance;
        }
    }

    public Integer getPracticePerformance() {
        return mPracticePerformance;
    }

    public String getSessionName() {
        return mSessionName;
    }

    public void setSessionName(String mSessionName) {
        this.mSessionName = mSessionName;
    }


    public Exercise getExercise() {
        return mExercise;
    }

    public void setExercise(Exercise mExercise) {
        this.mExercise = mExercise;
    }

    public ItemType getItemType() {
        return mItemType;
    }

    public void setItemType(ItemType mItemType) {
        this.mItemType = mItemType;
    }

    public String getPracticedOnDate() {
        return mPracticedOnDate;
    }

    public void setPracticedOnDate(String mPracticedOnDate) {
        this.mPracticedOnDate = mPracticedOnDate;
    }
}
