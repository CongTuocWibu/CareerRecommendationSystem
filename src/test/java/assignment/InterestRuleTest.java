package assignment;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Unit tests for the InterestRule business logic.
 *
 * Awards 15 points when one of the user's stated interests matches the
 * career name; 0 otherwise.
 */
public class InterestRuleTest {

    private InterestRule rule;
    private CareerPath career;

    @Before
    public void setUp() {
        rule = new InterestRule();
        career = new CareerPath("Software Developer", Arrays.asList("Java"));
    }

    @Test
    public void scoresFifteenWhenInterestMatchesCareerName() {
        UserProfile user = new UserProfile("Bachelor",
                Arrays.asList("Java"), Arrays.asList("Software Developer"));
        assertEquals(15, rule.evaluate(user, career));
    }

    @Test
    public void matchIsCaseInsensitive() {
        UserProfile user = new UserProfile("Bachelor",
                Arrays.asList("Java"), Arrays.asList("software developer"));
        assertEquals(15, rule.evaluate(user, career));
    }

    @Test
    public void scoresZeroWhenNoInterestMatches() {
        UserProfile user = new UserProfile("Bachelor",
                Arrays.asList("Java"), Arrays.asList("Cooking"));
        assertEquals(0, rule.evaluate(user, career));
    }
}
