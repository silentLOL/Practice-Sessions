package at.stefanirndorfer.practicesessions.data;

public class ExerciseWrapper implements ExerciseItemWrapper {
    private Exercise exercise;

    public ExerciseWrapper(Exercise exercise) {
        this.exercise = exercise;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}
