package at.stefanirndorfer.practicesessions.data;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Session {


    private String name;
    private Date practicedOnDate;
    private List<Exercise> exercises;

    public Session(String name, Date practicedOnDate, List<Exercise> exercises) {
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

    public String getPracticedOnDateAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(practicedOnDate);
    }

    public void setPracticedOnDate(Date practicedOnDate) {
        this.practicedOnDate = practicedOnDate;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}
