package at.stefanirndorfer.practicesessions.util;

import java.util.Collections;
import java.util.List;

import at.stefanirndorfer.practicesessions.data.Exercise;
import at.stefanirndorfer.practicesessions.data.Session;

public class SessionsUtils {

    /**
     * @param previousSessionsAverageBPM
     * @param currentExerciseBPM
     * @return practicePerformance as Integer value
     */
    public static int calculatePracticePerformance(float previousSessionsAverageBPM, int currentExerciseBPM) {
        if (previousSessionsAverageBPM != 0) { /* that would lead to a division by zero */
            return (int) (currentExerciseBPM / (previousSessionsAverageBPM / 100f));
        }
        throw new IllegalArgumentException("previousSessionAverageBPM is not suppoesed to be 0!");
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
     * with respect to the previous session.
     *
     * @param sessions
     */
    public static void calculatePerformances(List<Session> sessions) {
        int topPerformance = -1; // init with something too low to be real
        List<Exercise> currentSessionsExercises;
        for (int i = 0; i < sessions.size(); i++) {
            int summBPM = 0; // summ of all exercises practiceBPMs to calculate the average

            Session currentSession = sessions.get(i);
            currentSessionsExercises = currentSession.getExercises();
            for (Exercise currExercise : currentSessionsExercises) {
                Integer practicePerformance = null;
                if (i > 0) {
                    practicePerformance = SessionsUtils.calculatePracticePerformance(sessions.get(i - 1).getAverageBPM(), currExercise.getPracticedAtBpm());
                    currExercise.setPracticePerformance(practicePerformance);
                    if (practicePerformance > topPerformance) {
                        topPerformance = practicePerformance;
                    }
                }
                // summing up for the average bpm
                summBPM += currExercise.getPracticedAtBpm();
            }
            // setting the averageBPM for the current session
            currentSession.setAverageBPM((float) summBPM / currentSessionsExercises.size());
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
