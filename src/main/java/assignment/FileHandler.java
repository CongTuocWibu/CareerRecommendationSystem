package assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    private static final String FILE_NAME = "result.txt";
    private static final String SEPARATOR = "---";

    public void saveAll(List<RecommendationResult> results) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (RecommendationResult result : results) {
                writer.println("CAREER:" + result.getCareerName());
                writer.println("SCORE:" + result.getScore());
                writer.println("RANK:" + result.getRank());
                for (String explanation : result.getExplanations()) {
                    writer.println("EXPLANATION:" + explanation);
                }
                String reflection = result.getUserReflection();
                if (reflection != null && !reflection.isEmpty()) {
                    writer.println("REFLECTION:" + reflection);
                }
                writer.println(SEPARATOR);
            }
            System.out.println("Results saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving results: " + e.getMessage());
        }
    }

    public void save(RecommendationResult result, String userReflection) {
        result.setUserReflection(userReflection);
        List<RecommendationResult> list = new ArrayList<>();
        list.add(result);
        saveAll(list);
    }


    public List<RecommendationResult> loadAll() {
        List<RecommendationResult> results = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String careerName = "";
            int score = 0;
            int rank = 0;
            String reflection = "";
            List<String> explanations = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(SEPARATOR)) {
                    
                    RecommendationResult result =
                        new RecommendationResult(careerName, score, explanations);
                    result.setRank(rank);
                    result.setUserReflection(reflection);
                    results.add(result);


                    careerName = "";
                    score = 0;
                    rank = 0;
                    reflection = "";
                    explanations = new ArrayList<>();

                } else if (line.startsWith("CAREER:")) {
                    careerName = line.substring(7);
                } else if (line.startsWith("SCORE:")) {
                    score = Integer.parseInt(line.substring(6).trim());
                } else if (line.startsWith("RANK:")) {
                    rank = Integer.parseInt(line.substring(5).trim());
                } else if (line.startsWith("EXPLANATION:")) {
                    explanations.add(line.substring(12));
                } else if (line.startsWith("REFLECTION:")) {
                    reflection = line.substring(11);
                }
            }

            System.out.println("Results loaded successfully from " + FILE_NAME);

        } catch (FileNotFoundException e) {
            System.out.println("No saved file found.");
        } catch (IOException e) {
            System.out.println("Error loading results: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error reading saved file — data may be corrupted.");
        }

        return results;
    }

    public RecommendationResult load() {
        List<RecommendationResult> results = loadAll();
        if (results.isEmpty()) return null;
        return results.get(0);
    }
}