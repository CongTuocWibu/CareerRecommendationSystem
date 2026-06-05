package assignment;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;


public class EducationRuleTest {

    private EducationRule rule;
    private CareerPath anyCareer;

    @Before
    public void setUp() {
        rule = new EducationRule();
        anyCareer = new CareerPath("Any", Arrays.asList("Java"));
    }

    private UserProfile userWith(String education) {
        return new UserProfile(education, Arrays.asList("Java"), new ArrayList<>());
    }

    @Test
    public void phdScoresFifteen() {
        assertEquals(15, rule.evaluate(userWith("PhD"), anyCareer));
    }

    @Test
    public void mastersScoresTwelve() {
        assertEquals(12, rule.evaluate(userWith("Master of IT"), anyCareer));
    }

    @Test
    public void bachelorScoresTen() {
        assertEquals(10, rule.evaluate(userWith("Bachelor"), anyCareer));
    }

    @Test
    public void diplomaScoresFive() {
        assertEquals(5, rule.evaluate(userWith("Diploma"), anyCareer));
    }

    @Test
    public void unknownEducationScoresTwo() {
        assertEquals(2, rule.evaluate(userWith("High School"), anyCareer));
    }

    @Test
    public void emptyEducationScoresZero() {
        assertEquals(0, rule.evaluate(userWith(""), anyCareer));
    }
}
