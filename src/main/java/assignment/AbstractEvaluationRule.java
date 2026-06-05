/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment;

public abstract class AbstractEvaluationRule implements EvaluationRule {

    protected String explanation;
    protected String ruleName;

    public AbstractEvaluationRule(String ruleName) {
        this.ruleName = ruleName;
        this.explanation = "";
    }


    @Override
    public abstract int evaluate(UserProfile user, CareerPath career);

    @Override
    public String getExplanation() {
        return explanation;
    }

    public String getRuleName() {
        return ruleName;
    }

    protected String formatExplanation(String detail) {
        return "[" + ruleName + "] " + detail;
    }
}