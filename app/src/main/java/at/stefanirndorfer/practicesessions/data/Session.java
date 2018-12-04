package at.stefanirndorfer.practicesessions.data;

import java.util.List;

public class Session {

    private String name;
    private String practicedOnDate; /* TODO: need a date object in the end */
    private List<Exercise> exercises;

    public Session(String name, String practicedOnDate, List<Exercise> exercises) {
        this.name = name;
        this.practicedOnDate = practicedOnDate;
        this.exercises = exercises;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPracticedOnDate() {
        return practicedOnDate;
    }

    public void setPracticedOnDate(String practicedOnDate) {
        this.practicedOnDate = practicedOnDate;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}
