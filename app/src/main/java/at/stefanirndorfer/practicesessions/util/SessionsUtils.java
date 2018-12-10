package at.stefanirndorfer.practicesessions.util;

import java.util.Collections;
import java.util.List;

import at.stefanirndorfer.practicesessions.data.Exercise;
import at.stefanirndorfer.practicesessions.data.Session;

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


    /**
     * sorts a list of Session objects
     * by their PracticedOnDate.
     *
     * @param sessions
     * @return
     */
    public static void sortSessionByPracticeDate(List<Session> sessions) {
        Collections.sort(sessions, (o1, o2) -> o1.getPracticedOnDate().compareTo(o2.getPracticedOnDate()));
    }

    /**
     * Reverses the order of the Sessions list so that the latest is first
     *
     * @param sessions
     */
    public static void reverseSessionsList(List<Session> sessions) {
        Collections.reverse(sessions);
    }


    /**
     * iterates over the contained exercises and calculates the practice performance
     * with respect to the previous exercise.
     * This works under the assumption that all Exercises are in the correct order already (accordingly to their id)
     *
     * @param sessions
     */
    public static void calculatePerformances(List<Session> sessions) {

        int topPerformance = -1; // init with something too low to be real
        int previousPerformance = -1;
        List<Exercise> currentSessionsExercises;
        for (Session currentSession : sessions) {
            currentSessionsExercises = currentSession.getExercises();
            for (Exercise currExercise : currentSessionsExercises) {
                Integer practicePerformance = null;
                if (previousPerformance > 0) {
                    practicePerformance = SessionsUtils.calculatePracticePerformance(previousPerformance, currExercise.getPracticedAtBpm());
                    if (practicePerformance > topPerformance) {
                        topPerformance = practicePerformance;
                    }
                }
                previousPerformance = currExercise.getPracticedAtBpm();
                if (practicePerformance != null) {
                    currExercise.setPracticePerformance(practicePerformance);
                }
            }
        }
        flagTopPerformances(sessions, topPerformance);
    }

    private static void flagTopPerformances(List<Session> sessions, int topPerformance) {
        for (Session currSession : sessions) {
            for (Exercise currExercise : currSession.getExercises()) {
                if (currExercise.getPracticePerformance() != null && currExercise.getPracticePerformance() == topPerformance) {
                    currExercise.setTopPerformance(true);
                }
            }
        }
    }

}
