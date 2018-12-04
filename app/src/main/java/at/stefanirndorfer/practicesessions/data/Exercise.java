package at.stefanirndorfer.practicesessions.data;

class Exercise {
    private String exerciseId;
    private String name;
    private Integer practicedAtBpm;

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
}
