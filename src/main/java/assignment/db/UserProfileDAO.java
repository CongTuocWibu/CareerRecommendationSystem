package assignment.db;

import assignment.UserProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserProfileDAO {

    private final Connection connection;

    public UserProfileDAO() {
        this.connection = DBManager.getInstance().getConnection();
    }

    public int insert(UserProfile profile) {
        String sql = "INSERT INTO USER_PROFILE "
            + "(education_level, skills, interests, working_style, career_goal, strengths) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps =
                 connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, profile.getEducationLevel());
            ps.setString(2, listToString(profile.getSkills()));
            ps.setString(3, listToString(profile.getInterests()));
            ps.setString(4, profile.getWorkingStyle());
            ps.setString(5, profile.getCareerGoal());
            ps.setString(6, listToString(profile.getStrengths()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    System.out.println("User profile saved with id " + id);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error inserting user profile: " + e.getMessage());
        }
        return -1;
    }


    public UserProfile findById(int id) {
        String sql = "SELECT * FROM USER_PROFILE WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding user profile: " + e.getMessage());
        }
        return null;
    }


    public List<UserProfile> findAll() {
        List<UserProfile> profiles = new ArrayList<>();
        String sql = "SELECT * FROM USER_PROFILE ORDER BY id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                profiles.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error loading user profiles: " + e.getMessage());
        }
        return profiles;
    }

    public int findLatestId() {
        String sql = "SELECT MAX(id) AS max_id FROM USER_PROFILE";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int id = rs.getInt("max_id");
                return rs.wasNull() ? -1 : id;
            }
        } catch (SQLException e) {
            System.out.println("Error finding latest profile id: " + e.getMessage());
        }
        return -1;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM USER_PROFILE WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting user profile: " + e.getMessage());
        }
        return false;
    }

    private UserProfile mapRow(ResultSet rs) throws SQLException {
        String education = rs.getString("education_level");
        List<String> skills = stringToList(rs.getString("skills"));
        List<String> interests = stringToList(rs.getString("interests"));
        String workingStyle = rs.getString("working_style");
        String careerGoal = rs.getString("career_goal");
        List<String> strengths = stringToList(rs.getString("strengths"));

        return new UserProfile(education, skills, interests,
                               workingStyle, careerGoal, strengths);
    }


    private String listToString(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return String.join(",", list);
    }


    private List<String> stringToList(String s) {
        List<String> result = new ArrayList<>();
        if (s == null || s.trim().isEmpty()) return result;
        for (String item : s.split(",")) {
            String trimmed = item.trim();
            if (!trimmed.isEmpty()) result.add(trimmed);
        }
        return result;
    }
}