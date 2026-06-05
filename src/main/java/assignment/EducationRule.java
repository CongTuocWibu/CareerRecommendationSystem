package assignment;


public class EducationRule extends AbstractEvaluationRule {

    public EducationRule() {
        super("Education Level");
    }

    @Override
    public int evaluate(UserProfile user, CareerPath career) {
        int score = 0;
        String education = user.getEducationLevel();

        if (education == null || education.isEmpty()) {
            explanation = formatExplanation("No education level provided.");
            return 0;
        }

        String edu = education.trim().toLowerCase();

        if (edu.contains("phd") || edu.contains("doctorate")) {
            score = 15;
            explanation = formatExplanation(
                "PhD/Doctorate level education -- highest qualification bonus applied."
            );
        } else if (edu.contains("master")) {
            score = 12;
            explanation = formatExplanation(
                "Master's degree -- strong academic foundation for IT careers."
            );
        } else if (edu.contains("bachelor") || edu.contains("degree")) {
            score = 10;
            explanation = formatExplanation(
                "Bachelor's degree -- meets standard entry requirements for most IT roles."
            );
        } else if (edu.contains("diploma") || edu.contains("certificate")) {
            score = 5;
            explanation = formatExplanation(
                "Diploma/Certificate level -- suitable for some IT roles; "
                + "further study may strengthen your application."
            );
        } else {
            score = 2;
            explanation = formatExplanation(
                "Education level recorded but does not match common IT qualifications. "
                + "Consider additional certifications."
            );
        }

        return score;
    }
}