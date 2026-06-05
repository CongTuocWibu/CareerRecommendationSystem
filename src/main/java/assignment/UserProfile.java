package assignment;

import java.util.List;
import java.util.ArrayList;

public class UserProfile {

    private String educationLevel;
    private List<String> skills;
    private List<String> interests;
    private String workingStyle;
    private String careerGoal;
    private List<String> strengths;

    public UserProfile(String educationLevel,
                       List<String> skills,
                       List<String> interests,
                       String workingStyle,
                       String careerGoal,
                       List<String> strengths) {
        this.educationLevel = educationLevel;
        this.skills = (skills != null) ? skills : new ArrayList<>();
        this.interests = (interests != null) ? interests : new ArrayList<>();
        this.workingStyle = (workingStyle != null) ? workingStyle : "";
        this.careerGoal = (careerGoal != null) ? careerGoal : "";
        this.strengths = (strengths != null) ? strengths : new ArrayList<>();
    }

    public UserProfile(String educationLevel,
                       List<String> skills,
                       List<String> interests) {
        this(educationLevel, skills, interests, "", "", new ArrayList<>());
    }


    public String getEducationLevel() {
        return educationLevel;
    }

    public List<String> getSkills() {
        return skills;
    }

    public List<String> getInterests() {
        return interests;
    }

    public String getWorkingStyle() {
        return workingStyle;
    }

    public String getCareerGoal() {
        return careerGoal;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    // ── Setters (needed for GUI form binding) ─────────────────────────────────

    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public void setWorkingStyle(String workingStyle) {
        this.workingStyle = workingStyle;
    }

    public void setCareerGoal(String careerGoal) {
        this.careerGoal = careerGoal;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    // ── Validation ─────────────────────────────────────────────────────────

    /**
     * Returns true if the profile has the minimum required fields filled in.
     */
    public boolean isValid() {
        return educationLevel != null && !educationLevel.trim().isEmpty()
            && skills != null && !skills.isEmpty()
            && interests != null && !interests.isEmpty();
    }

    @Override
    public String toString() {
        return "UserProfile{"
            + "education='" + educationLevel + "'"
            + ", skills=" + skills
            + ", interests=" + interests
            + ", workingStyle='" + workingStyle + "'"
            + ", careerGoal='" + careerGoal + "'"
            + ", strengths=" + strengths
            + "}";
    }
}