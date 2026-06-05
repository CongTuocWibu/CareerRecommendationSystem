package assignment;

import java.util.ArrayList;
import java.util.List;


public class RuleFactory {


    public static EvaluationRule createRule(String type) {
        if (type == null) {
            return null;
        }
        if ("skill".equalsIgnoreCase(type)) {
            return new SkillRule();
        }
        if ("interest".equalsIgnoreCase(type)) {
            return new InterestRule();
        }
        if ("education".equalsIgnoreCase(type)) {
            return new EducationRule();
        }
        return null;
    }

    public static List<EvaluationRule> createDefaultRules() {
        List<EvaluationRule> rules = new ArrayList<>();
        rules.add(createRule("skill"));
        rules.add(createRule("interest"));
        rules.add(createRule("education"));
        return rules;
    }
}
