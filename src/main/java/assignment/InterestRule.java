package assignment;


public class InterestRule extends AbstractEvaluationRule {

    public InterestRule() {
        super("Interest Match");
    }

    @Override
    public int evaluate(UserProfile user, CareerPath career) {
        int score = 0;

        boolean matched = false;
        for (String interest : user.getInterests()) {
            if (interest.equalsIgnoreCase(career.getName())
                    || career.getName().toLowerCase().contains(interest.toLowerCase())
                    || interest.toLowerCase().contains(career.getName().toLowerCase())) {
                matched = true;
                break;
            }
        }

        if (matched) {
            score = 15;
            explanation = formatExplanation(
                "Career aligns with your stated interests. Strong motivation expected."
            );
        } else {
            explanation = formatExplanation(
                "Career does not directly match your stated interests. "
                + "Consider whether this path aligns with your goals."
            );
        }

        return score;
    }
}