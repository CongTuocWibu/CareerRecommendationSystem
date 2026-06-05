package assignment;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

/**
 * Unit tests for the RuleFactory (Factory pattern).
 * Verifies the factory returns the correct concrete rule for each id.
 */
public class RuleFactoryTest {

    @Test
    public void createsSkillRule() {
        assertTrue(RuleFactory.createRule("skill") instanceof SkillRule);
    }

    @Test
    public void createsInterestRule() {
        assertTrue(RuleFactory.createRule("interest") instanceof InterestRule);
    }

    @Test
    public void createsEducationRule() {
        assertTrue(RuleFactory.createRule("education") instanceof EducationRule);
    }

    @Test
    public void isCaseInsensitive() {
        assertTrue(RuleFactory.createRule("SKILL") instanceof SkillRule);
    }

    @Test
    public void returnsNullForUnknownType() {
        assertNull(RuleFactory.createRule("nonsense"));
    }

    @Test
    public void buildsThreeDefaultRules() {
        List<EvaluationRule> rules = RuleFactory.createDefaultRules();
        assertEquals(3, rules.size());
    }
}
