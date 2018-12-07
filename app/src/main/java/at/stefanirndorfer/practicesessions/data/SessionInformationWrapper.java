package at.stefanirndorfer.practicesessions.data;

public class SessionInformationWrapper implements ExerciseItemWrapper {

    private String mSessionName;

    public SessionInformationWrapper(String mSessionName) {
        this.mSessionName = mSessionName;
    }

    public String getmSessionName() {
        return mSessionName;
    }

    public void setmSessionName(String mSessionName) {
        this.mSessionName = mSessionName;
    }
}
