package assignment;


public class SkillRule extends AbstractEvaluationRule {

    public SkillRule() {
        super("Skill Match");
    }

    @Override
    public int evaluate(UserProfile user, CareerPath career) {
        int matchedSkills = 0;

        for (String skill : user.getSkills()) {
            if (career.getRequiredSkills().contains(skill)) {
                matchedSkills++;
            }
        }

        int totalRequired = career.getRequiredSkills().size();
        int score = matchedSkills * 10;

        if (matchedSkills == 0) {
            explanation = formatExplanation(
                "No required skills matched. Required: " + career.getRequiredSkills()
            );
        } else if (matchedSkills == totalRequired) {
            explanation = formatExplanation(
                "All " + matchedSkills + " required skills matched! Full skill alignment."
            );
        } else {
            explanation = formatExplanation(
                matchedSkills + " of " + totalRequired + " required skills matched."
                + " Your matched skills contribute " + score + " points."
            );
        }

        return score;
    }
}