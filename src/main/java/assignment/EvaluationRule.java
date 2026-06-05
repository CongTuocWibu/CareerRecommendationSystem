package assignment;

interface EvaluationRule {
	public int 	evaluate(UserProfile user, CareerPath career);
	public String getExplanation();
}
