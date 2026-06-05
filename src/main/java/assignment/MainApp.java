package assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Main console application entry point.
 *
 * Upgraded from A1:
 * - User enters their own profile (no more hard-coded data)
 * - Input validation — cannot generate recommendation with empty profile
 * - Evaluates ALL 5 career paths and shows a ranked list
 * - Can view detailed explanation for any ranked career
 * - Reflection is saved per-career if user wishes
 * - Save/Load now handles list of results
 */
public class MainApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Set up rules (Strategy pattern — rules are injected into engine)
        List<EvaluationRule> rules = new ArrayList<>();
        rules.add(new SkillRule());
        rules.add(new InterestRule());
        rules.add(new EducationRule());

        RecommendationEngine engine = new RecommendationEngine(rules);
        ExplanationGenerator explanationGenerator = new ExplanationGenerator();
        FileHandler file = new FileHandler();

        UserProfile user = null;
        List<RecommendationResult> results = null;

        boolean run = true;

        while (run) {
            System.out.println("\n==== Career Recommendation System ====");
            System.out.println("1. Enter user profile");
            System.out.println("2. Generate recommendations");
            System.out.println("3. View ranked summary");
            System.out.println("4. View detailed explanation");
            System.out.println("5. Add reflection");
            System.out.println("6. Save results");
            System.out.println("7. Load results");
            System.out.println("0. Exit");
            System.out.print("Choice: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {

                case 1:
                    user = inputUserProfile(scanner);
                    System.out.println("Profile saved. You can now generate recommendations.");
                    break;

                case 2:
                    if (user == null || !user.isValid()) {
                        System.out.println("Please enter a valid user profile first (option 1).");
                    } else {
                        results = engine.generate(user);
                        System.out.println("Recommendations generated for " + results.size() + " career paths.");
                        explanationGenerator.printRankedSummary(results);
                    }
                    break;

                case 3:
                    if (results == null) {
                        System.out.println("Please generate recommendations first (option 2).");
                    } else {
                        explanationGenerator.printRankedSummary(results);
                    }
                    break;

                case 4:
                    if (results == null) {
                        System.out.println("Please generate recommendations first (option 2).");
                    } else {
                        viewDetailedExplanation(scanner, results, explanationGenerator);
                    }
                    break;

                case 5:
                    if (results == null) {
                        System.out.println("Please generate recommendations first (option 2).");
                    } else {
                        addReflection(scanner, results);
                    }
                    break;

                case 6:
                    if (results == null) {
                        System.out.println("Nothing to save.");
                    } else {
                        file.saveAll(results);
                    }
                    break;

                case 7:
                    List<RecommendationResult> loaded = file.loadAll();
                    if (loaded.isEmpty()) {
                        System.out.println("No saved results found.");
                    } else {
                        results = loaded;
                        System.out.println("Loaded " + results.size() + " results.");
                        explanationGenerator.printRankedSummary(results);
                    }
                    break;

                case 0:
                    run = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please enter 0-7.");
                    break;
            }
        }

        scanner.close();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    /**
     * Guides the user through entering their profile with validation.
     */
    private static UserProfile inputUserProfile(Scanner scanner) {
        System.out.println("\n--- Enter Your Profile ---");

        // Education level
        String education = "";
        while (education.isEmpty()) {
            System.out.print("Education level (e.g. Bachelor, Master, Diploma):  ");
            education = scanner.nextLine().trim();
            if (education.isEmpty()) {
                System.out.println("Education level cannot be empty.");
            }
        }

        // Skills
        List<String> skills = new ArrayList<>();
        while (skills.isEmpty()) {
            System.out.print("Your skills (comma-separated, e.g. Java, SQL, Python):  ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Please enter at least one skill.");
            } else {
                for (String s : input.split(",")) {
                    String trimmed = s.trim();
                    if (!trimmed.isEmpty()) skills.add(trimmed);
                }
            }
        }

        // Interests
        List<String> interests = new ArrayList<>();
        while (interests.isEmpty()) {
            System.out.println("Interests -- enter career names you are interested in.");
            System.out.println("Options: Software Developer, Data Analyst, IT Support, Data Scientist, QA Engineer");
            System.out.print("Your interests (comma-separated):  ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Please enter at least one interest.");
            } else {
                for (String s : input.split(",")) {
                    String trimmed = s.trim();
                    if (!trimmed.isEmpty()) interests.add(trimmed);
                }
            }
        }

        // Optional fields
        System.out.print("Preferred working style (e.g. collaborative, independent) [optional]:  ");
        String workingStyle = scanner.nextLine().trim();

        System.out.print("Career goal (e.g. become a lead developer) [optional]:  ");
        String careerGoal = scanner.nextLine().trim();

        System.out.print("Personal strengths (comma-separated, e.g. analytical, creative) [optional]:  ");
        List<String> strengths = new ArrayList<>();
        String strengthInput = scanner.nextLine().trim();
        if (!strengthInput.isEmpty()) {
            for (String s : strengthInput.split(",")) {
                String trimmed = s.trim();
                if (!trimmed.isEmpty()) strengths.add(trimmed);
            }
        }

        return new UserProfile(education, skills, interests, workingStyle, careerGoal, strengths);
    }

    /**
     * Lets user pick a career by rank to view its detailed explanation.
     */
    private static void viewDetailedExplanation(Scanner scanner,
                                                 List<RecommendationResult> results,
                                                 ExplanationGenerator gen) {
        System.out.println("\nEnter rank number to view details (1-" + results.size() + "), or 0 to view all:  ");
        int rankChoice = -1;
        try {
            rankChoice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (rankChoice == 0) {
            gen.printAllExplanations(results);
        } else if (rankChoice >= 1 && rankChoice <= results.size()) {
            // Find the result with that rank
            for (RecommendationResult r : results) {
                if (r.getRank() == rankChoice) {
                    gen.printDetailedExplanation(r);
                    return;
                }
            }
        } else {
            System.out.println("Invalid rank number.");
        }
    }

    /**
     * Lets user add a reflection to one or all career results.
     */
    private static void addReflection(Scanner scanner, List<RecommendationResult> results) {
        System.out.println("\nDo you agree with the top recommendation? (yes/no)");
        String agree = scanner.nextLine().trim();

        if (agree.equalsIgnoreCase("no")) {
            System.out.println("Which rank do you want to add a reflection to? (1-" + results.size() + "): ");
            int rankChoice = -1;
            try {
                rankChoice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                return;
            }

            for (RecommendationResult r : results) {
                if (r.getRank() == rankChoice) {
                    System.out.println("Please describe your expectation for " + r.getCareerName() + ":");
                    String reflection = scanner.nextLine().trim();
                    r.setUserReflection(reflection);
                    System.out.println("Reflection recorded for " + r.getCareerName() + ".");
                    return;
                }
            }
            System.out.println("Rank not found.");
        } else if (agree.equalsIgnoreCase("yes")) {
            System.out.println("Thank you for your feedback!");
        } else {
            System.out.println("Please enter 'yes' or 'no'.");
        }
    }
}