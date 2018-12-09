package at.stefanirndorfer.practicesessions.util;

public class SessionsUtils {

    /**
     * @param previousExerciseBPM
     * @param currentExerciseBPM
     * @return practicePerformance as Integer value
     */
    public static int calculatePracticePerformance(int previousExerciseBPM, int currentExerciseBPM) {
        if (previousExerciseBPM != 0) { /* that would lead to a division by zero */
            return (int) (currentExerciseBPM / (previousExerciseBPM / 100f));
        }
        throw new IllegalArgumentException("previousExerciseBPM is not suppoesed to be 0!");
    }
}
