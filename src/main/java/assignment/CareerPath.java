package assignment;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class CareerPath {

    private String name;
    private List<String> requiredSkills;
    private String description;

    public CareerPath(String name, List<String> requiredSkills, String description) {
        this.name = name;
        this.requiredSkills = (requiredSkills != null) ? requiredSkills : new ArrayList<>();
        this.description = (description != null) ? description : "";
    }

    public CareerPath(String name, List<String> requiredSkills) {
        this(name, requiredSkills, "");
    }

    public String getName() {
        return name;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public String getDescription() {
        return description;
    }

    public static List<CareerPath> getPredefinedCareers() {
        List<CareerPath> careers = new ArrayList<>();

        careers.add(new CareerPath(
            "Software Developer",
            Arrays.asList("Java", "Python", "Git", "SQL", "Problem Solving",
                          "OOP", "JavaScript", "Data Structures"),
            "Designs and builds computer programs to meet specific user or business needs. "
            + "Writes, tests, and maintains code to ensure software runs efficiently."
        ));

        careers.add(new CareerPath(
            "Data Analyst",
            Arrays.asList("SQL", "Excel", "Python", "Statistics",
                          "Data Visualisation", "R", "Problem Solving", "Communication"),
            "Collects, cleans, and interprets data to help organisations make informed decisions. "
            + "Identifies patterns and presents insights using analytical techniques."
        ));

        careers.add(new CareerPath(
            "IT Support",
            Arrays.asList("Networking", "Hardware", "Windows", "Linux",
                          "Troubleshooting", "Communication", "Customer Service"),
            "Assists users by diagnosing and resolving technical issues with hardware, "
            + "software, and networks. Ensures systems run smoothly."
        ));

        careers.add(new CareerPath(
            "Data Scientist",
            Arrays.asList("Python", "Machine Learning", "Statistics", "SQL",
                          "R", "Data Visualisation", "Mathematics", "Problem Solving"),
            "Uses statistical methods, programming, and machine learning to analyse "
            + "large datasets and uncover meaningful insights."
        ));

        careers.add(new CareerPath(
            "QA Engineer",
            Arrays.asList("Testing", "Java", "Python", "Selenium",
                          "Problem Solving", "Attention to Detail", "SQL", "Git"),
            "Tests software to ensure it meets quality standards and is free of defects. "
            + "Designs test cases and works with developers to improve reliability."
        ));

        return careers;
    }

    @Override
    public String toString() {
        return name;
    }
}