package assignment;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Unit tests for the SkillRule business logic.
 *
 * Verifies the scoring contract: 10 points per required skill that the user has.
 * Uses a custom CareerPath so the test does not depend on the predefined data.
 */
public class SkillRuleTest {

    private SkillRule rule;
    private CareerPath career;

    @Before
    public void setUp() {
        rule = new SkillRule();
        // Career requiring exactly three skills.
        career = new CareerPath("Test Career", Arrays.asList("Java", "Python", "SQL"));
    }

    @Test
    public void scoresTenPointsPerMatchedSkill() {
        UserProfile user = new UserProfile("Bachelor",
                Arrays.asList("Java", "SQL"), new ArrayList<>());
        // 2 of 3 required skills matched -> 20 points
        assertEquals(20, rule.evaluate(user, career));
    }

    @Test
    public void returnsZeroWhenNoSkillMatches() {
        UserProfile user = new UserProfile("Bachelor",
                Arrays.asList("Cooking", "Painting"), new ArrayList<>());
        assertEquals(0, rule.evaluate(user, career));
    }

    @Test
    public void scoresFullMarksWhenAllSkillsMatch() {
        UserProfile user = new UserProfile("Bachelor",
                Arrays.asList("Java", "Python", "SQL"), new ArrayList<>());
        // 3 of 3 -> 30 points
        assertEquals(30, rule.evaluate(user, career));
    }

    @Test
    public void explanationIsProducedAfterEvaluation() {
        UserProfile user = new UserProfile("Bachelor",
                Arrays.asList("Java"), new ArrayList<>());
        rule.evaluate(user, career);
        assertNotNull(rule.getExplanation());
        assertFalse(rule.getExplanation().isEmpty());
    }
}
