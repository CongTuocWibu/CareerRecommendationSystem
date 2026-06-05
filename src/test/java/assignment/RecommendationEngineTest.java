package assignment;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RecommendationEngineTest {

    private RecommendationEngine engine;

    @Before
    public void setUp() {
        List<EvaluationRule> rules = new ArrayList<>();
        rules.add(new SkillRule());
        rules.add(new InterestRule());
        rules.add(new EducationRule());
        engine = new RecommendationEngine(rules);
    }

    private UserProfile softwareDevProfile() {
        return new UserProfile(
                "Bachelor",
                Arrays.asList("Java", "Python", "SQL", "Git", "OOP"),
                Arrays.asList("Software Developer"));
    }

    @Test
    public void evaluatesAllFiveCareers() {
        List<RecommendationResult> results = engine.generate(softwareDevProfile());
        assertEquals(5, results.size());
    }

    @Test
    public void topResultHasRankOne() {
        List<RecommendationResult> results = engine.generate(softwareDevProfile());
        assertEquals(1, results.get(0).getRank());
    }

    @Test
    public void resultsAreSortedByScoreDescending() {
        List<RecommendationResult> results = engine.generate(softwareDevProfile());
        for (int i = 0; i < results.size() - 1; i++) {
            assertTrue("Result " + i + " should score >= the next one",
                    results.get(i).getScore() >= results.get(i + 1).getScore());
        }
    }

    @Test
    public void bestMatchingCareerIsRankedFirst() {
        List<RecommendationResult> results = engine.generate(softwareDevProfile());
        assertEquals("Software Developer", results.get(0).getCareerName());
    }

    @Test
    public void ranksAreAssignedSequentially() {
        List<RecommendationResult> results = engine.generate(softwareDevProfile());
        for (int i = 0; i < results.size(); i++) {
            assertEquals(i + 1, results.get(i).getRank());
        }
    }
}
