package assignment;

import java.util.List;
import java.util.ArrayList;


public class RecommendationResult {

    private String careerName;
    private int score;
    private List<String> explanations;
    private int rank;
    private String userReflection;
    private int id = -1;

    public RecommendationResult(String careerName, int score, List<String> explanations) {
        this.careerName = careerName;
        this.score = score;
        this.explanations = (explanations != null) ? explanations : new ArrayList<>();
        this.rank = 0;
        this.userReflection = "";
    }

    public RecommendationResult(int score, List<String> explanations) {
        this("Unknown Career", score, explanations);
    }

    public String getCareerName() {
        return careerName;
    }

    public void setCareerName(String careerName) {
        this.careerName = careerName;
    }

    public int getScore() {
        return score;
    }

    public List<String> getExplanations() {
        return explanations;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUserReflection() {
        return userReflection;
    }

    public void setUserReflection(String userReflection) {
        this.userReflection = (userReflection != null) ? userReflection : "";
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSummary() {
        return String.format("#%d  %-25s  Score: %d", rank, careerName, score);
    }
    

    public String getDetailedView() {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------------\n");
        sb.append(String.format("  Rank #%d -- %s\n", rank, careerName));
        sb.append(String.format("  Total Score: %d\n", score));
        sb.append("---------------------------------\n");
        sb.append("  Explanation:\n");
        for (String exp : explanations) {
            sb.append("    - ").append(exp).append("\n");
        }
        if (userReflection != null && !userReflection.isEmpty()) {
            sb.append("---------------------------------\n");
            sb.append("  Your Reflection:\n");
            sb.append("    ").append(userReflection).append("\n");
        }
        sb.append("---------------------------------\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "RecommendationResult{career='" + careerName
            + "', score=" + score + ", rank=" + rank + "}";
    }
}