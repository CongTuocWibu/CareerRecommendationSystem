package assignment;

import java.util.List;


public class ExplanationGenerator {

    public void printRankedSummary(List<RecommendationResult> results) {
        System.out.println("\n---------------------------------");
        System.out.println("      CAREER RECOMMENDATION RESULTS");
        System.out.println("------------------------------------");
        for (RecommendationResult result : results) {
            System.out.println(result.getSummary());
        }
        System.out.println("------------------------------------\n");
    }

    public void printDetailedExplanation(RecommendationResult result) {
        System.out.println(result.getDetailedView());
    }

    public void printAllExplanations(List<RecommendationResult> results) {
        System.out.println("\n  DETAILED EXPLANATIONS FOR ALL CAREERS\n");
        for (RecommendationResult result : results) {
            System.out.println(result.getDetailedView());
        }
    }

    public List<String> generateExplanation(RecommendationResult result) {
        System.out.println("Total score: " + result.getScore());
        for (String r : result.getExplanations()) {
            System.out.println(r);
        }
        return result.getExplanations();
    }
}