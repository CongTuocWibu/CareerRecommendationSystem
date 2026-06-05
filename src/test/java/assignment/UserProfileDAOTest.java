package assignment;

import assignment.db.UserProfileDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Database tests for UserProfileDAO.
 *
 * These run against the embedded Derby database (created automatically by
 * DBManager). Each test inserts its own row and removes it again in tearDown,
 * so the tests are independent and repeatable.
 */
public class UserProfileDAOTest {

    private UserProfileDAO dao;
    private int insertedId = -1;

    @Before
    public void setUp() {
        dao = new UserProfileDAO();
    }

    @After
    public void tearDown() {
        // Clean up whatever this test inserted.
        if (insertedId != -1) {
            dao.delete(insertedId);
            insertedId = -1;
        }
    }

    private UserProfile sampleProfile() {
        return new UserProfile(
                "Bachelor",
                Arrays.asList("Java", "SQL"),
                Arrays.asList("Software Developer"),
                "collaborative",
                "become a senior developer",
                Arrays.asList("analytical"));
    }

    @Test
    public void insertReturnsGeneratedId() {
        insertedId = dao.insert(sampleProfile());
        assertTrue("insert should return a valid generated id", insertedId > 0);
    }

    @Test
    public void findByIdReturnsTheSameData() {
        UserProfile profile = sampleProfile();
        insertedId = dao.insert(profile);

        UserProfile loaded = dao.findById(insertedId);
        assertNotNull("profile should be found by id", loaded);
        assertEquals(profile.getEducationLevel(), loaded.getEducationLevel());
        assertEquals(profile.getSkills(), loaded.getSkills());
        assertEquals(profile.getInterests(), loaded.getInterests());
        assertEquals(profile.getWorkingStyle(), loaded.getWorkingStyle());
    }

    @Test
    public void findLatestIdReflectsInsertedRow() {
        insertedId = dao.insert(sampleProfile());
        assertEquals(insertedId, dao.findLatestId());
    }
}
