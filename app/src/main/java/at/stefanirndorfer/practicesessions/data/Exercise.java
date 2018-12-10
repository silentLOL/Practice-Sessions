package at.stefanirndorfer.practicesessions.data;

public class Exercise {
    private String exerciseId;
    private String name;
    private Integer practicedAtBpm;
    private Integer practicePerformance;
    private boolean isTopPerformance = false; // flag to mark top performances. default false

    public Exercise(String exerciseId, String name, Integer practicedAtBpm) {
        this.exerciseId = exerciseId;
        this.name = name;
        this.practicedAtBpm = practicedAtBpm;
    }

    public String getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(String exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPracticedAtBpm() {
        return practicedAtBpm;
    }

    public void setPracticedAtBpm(Integer practicedAtBpm) {
        this.practicedAtBpm = practicedAtBpm;
    }

    public Integer getPracticePerformance() {
        return practicePerformance;
    }

    public void setPracticePerformance(Integer practicePerformance) {
        this.practicePerformance = practicePerformance;
    }

    public boolean isTopPerformance() {
        return isTopPerformance;
    }

    public void setTopPerformance(boolean topPerformance) {
        isTopPerformance = topPerformance;
    }
}
