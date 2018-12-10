package at.stefanirndorfer.practicesessions.data;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Session {

    private int id; /* unique identifier created on the client side */
    private String name;
    private Date practicedOnDate;
    private List<Exercise> exercises;
    private float averageBPM;

    public Session(String name, Date practicedOnDate, List<Exercise> exercises) {
        this.name = name;
        this.practicedOnDate = practicedOnDate;
        this.exercises = exercises;
        id = -1;
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

    public Date getPracticedOnDate() {
        return practicedOnDate;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAverageBPM() {
        return averageBPM;
    }

    public void setAverageBPM(float averageBPM) {
        this.averageBPM = averageBPM;
    }
}
