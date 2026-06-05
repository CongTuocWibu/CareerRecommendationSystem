package assignment;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;


public class RecommendationEngine {

    private List<EvaluationRule> rules;
    private List<CareerPath> careers;

    public RecommendationEngine(List<EvaluationRule> rules, List<CareerPath> careers) {
        this.rules = rules;
        this.careers = careers;
    }


    public RecommendationEngine(List<EvaluationRule> rules) {
        this(rules, CareerPath.getPredefinedCareers());
    }


    public List<RecommendationResult> generate(UserProfile user) {
        List<RecommendationResult> results = new ArrayList<>();

        for (CareerPath career : careers) {
            int totalScore = 0;
            List<String> explanations = new ArrayList<>();

            for (EvaluationRule rule : rules) {
                int ruleScore = rule.evaluate(user, career);
                totalScore += ruleScore;
                explanations.add(rule.getExplanation());
            }

            results.add(new RecommendationResult(career.getName(), totalScore, explanations));
        }

        results.sort(Comparator.comparingInt(RecommendationResult::getScore).reversed());

        for (int i = 0; i < results.size(); i++) {
            results.get(i).setRank(i + 1);
        }

        return results;
    }


    public RecommendationResult generate(UserProfile user, CareerPath career) {
        int totalScore = 0;
        List<String> explanations = new ArrayList<>();

        for (EvaluationRule rule : rules) {
            totalScore += rule.evaluate(user, career);
            explanations.add(rule.getExplanation());
        }

        RecommendationResult result =
            new RecommendationResult(career.getName(), totalScore, explanations);
        result.setRank(1);
        return result;
    }

    public List<EvaluationRule> getRules() {
        return rules;
    }

    public List<CareerPath> getCareers() {
        return careers;
    }
}