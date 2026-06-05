package assignment.db;

import assignment.RecommendationResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecommendationResultDAO {

    private static final String DELIM = "||";
    private static final String DELIM_REGEX = "\\|\\|";

    private final Connection connection;

    public RecommendationResultDAO() {
        this.connection = DBManager.getInstance().getConnection();
    }


    public int insert(RecommendationResult result, int profileId) {
        String sql = "INSERT INTO RECOMMENDATION_RESULT "
            + "(profile_id, career_name, score, rank_position, explanations, user_reflection) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps =
                 connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, profileId);
            ps.setString(2, result.getCareerName());
            ps.setInt(3, result.getScore());
            ps.setInt(4, result.getRank());
            ps.setString(5, listToString(result.getExplanations()));
            ps.setString(6, result.getUserReflection());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting result: " + e.getMessage());
        }
        return -1;
    }


    public void insertAll(List<RecommendationResult> results, int profileId) {
        for (RecommendationResult r : results) {
            insert(r, profileId);
        }
        System.out.println("Saved " + results.size() + " results for profile " + profileId);
    }


    public List<RecommendationResult> findByProfileId(int profileId) {
        List<RecommendationResult> results = new ArrayList<>();
        String sql = "SELECT * FROM RECOMMENDATION_RESULT "
            + "WHERE profile_id = ? ORDER BY rank_position";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, profileId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error loading results: " + e.getMessage());
        }
        return results;
    }


    public List<RecommendationResult> findAll() {
        List<RecommendationResult> results = new ArrayList<>();
        String sql = "SELECT * FROM RECOMMENDATION_RESULT ORDER BY profile_id, rank_position";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                results.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error loading all results: " + e.getMessage());
        }
        return results;
    }


    public boolean updateReflection(int resultId, String reflection) {
        String sql = "UPDATE RECOMMENDATION_RESULT SET user_reflection = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, reflection);
            ps.setInt(2, resultId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating reflection: " + e.getMessage());
        }
        return false;
    }


    public boolean deleteByProfileId(int profileId) {
        String sql = "DELETE FROM RECOMMENDATION_RESULT WHERE profile_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, profileId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting results: " + e.getMessage());
        }
        return false;
    }

    private RecommendationResult mapRow(ResultSet rs) throws SQLException {
        String careerName = rs.getString("career_name");
        int score = rs.getInt("score");
        int rank = rs.getInt("rank_position");
        List<String> explanations = stringToList(rs.getString("explanations"));
        String reflection = rs.getString("user_reflection");

        RecommendationResult result =
            new RecommendationResult(careerName, score, explanations);
        result.setRank(rank);
        result.setUserReflection(reflection);
        return result;
    }

    private String listToString(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return String.join(DELIM, list);
    }

    private List<String> stringToList(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.trim().isEmpty()) return result;
        for (String item : s.split(DELIM_REGEX)) {
            if (!item.trim().isEmpty()) result.add(item.trim());
        }
        return result;
    }
}