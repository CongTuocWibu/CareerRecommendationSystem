package assignment.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;


public class DBManager {


    private static final String DB_URL = "jdbc:derby:careerDB;create=true";

    private static DBManager instance;

    private Connection connection;

    private DBManager() {
        connect();
        createTablesIfNotExist();
    }

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    private void connect() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Database connected successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("Derby driver not found. Check that derby.jar is on the classpath.");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            System.out.println("Error checking connection: " + e.getMessage());
        }
        return connection;
    }

    private void createTablesIfNotExist() {
        createTable("USER_PROFILE",
            "CREATE TABLE USER_PROFILE ("
            + "  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
            + "  education_level VARCHAR(100), "
            + "  skills VARCHAR(500), "
            + "  interests VARCHAR(500), "
            + "  working_style VARCHAR(200), "
            + "  career_goal VARCHAR(300), "
            + "  strengths VARCHAR(500)"
            + ")"
        );

        createTable("RECOMMENDATION_RESULT",
            "CREATE TABLE RECOMMENDATION_RESULT ("
            + "  id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, "
            + "  profile_id INT, "
            + "  career_name VARCHAR(100), "
            + "  score INT, "
            + "  rank_position INT, "
            + "  explanations VARCHAR(2000), "
            + "  user_reflection VARCHAR(1000)"
            + ")"
        );
    }


    private void createTable(String tableName, String createSql) {
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet rs = meta.getTables(null, null, tableName.toUpperCase(), null);
            boolean exists = rs.next();
            rs.close();

            if (!exists) {
                try (Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate(createSql);
                    System.out.println("Table '" + tableName + "' created.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating table '" + tableName + "': " + e.getMessage());
        }
    }


    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            if ("XJ015".equals(e.getSQLState()) || "08006".equals(e.getSQLState())) {
                System.out.println("Database shut down normally.");
            } else {
                System.out.println("Error during shutdown: " + e.getMessage());
            }
        }
        instance = null;
    }
}
