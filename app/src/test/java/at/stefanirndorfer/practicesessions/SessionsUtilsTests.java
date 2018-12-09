package at.stefanirndorfer.practicesessions;

import org.junit.Test;

import at.stefanirndorfer.practicesessions.util.SessionsUtils;

import static org.junit.Assert.assertEquals;

public class SessionsUtilsTests {

    @Test
    public void calculateCorrectPracticePerformance() {
        int performance = SessionsUtils.calculatePracticePerformance(43, 64);
        assertEquals(148, performance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tryClaculatePracticePerformanceWithAZero() {
        int performance = SessionsUtils.calculatePracticePerformance(0, 64);
    }
}
