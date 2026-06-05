package assignment;

import assignment.db.RecommendationResultDAO;
import assignment.db.UserProfileDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Database tests for RecommendationResultDAO.
 *
 * A throwaway parent profile is created in setUp so that the results have a
 * profile_id to link to, then everything is removed again in tearDown.
 */
public class RecommendationResultDAOTest {

    private UserProfileDAO profileDao;
    private RecommendationResultDAO resultDao;
    private int profileId = -1;

    @Before
    public void setUp() {
        profileDao = new UserProfileDAO();
        resultDao = new RecommendationResultDAO();

        UserProfile parent = new UserProfile(
                "Bachelor", Arrays.asList("Java"), Arrays.asList("Software Developer"));
        profileId = profileDao.insert(parent);
    }

    @After
    public void tearDown() {
        if (profileId != -1) {
            resultDao.deleteByProfileId(profileId);
            profileDao.delete(profileId);
            profileId = -1;
        }
    }

    private List<RecommendationResult> twoRankedResults() {
        List<RecommendationResult> list = new ArrayList<>();

        RecommendationResult first = new RecommendationResult(
                "Software Developer", 75, Arrays.asList("strong match"));
        first.setRank(1);

        RecommendationResult second = new RecommendationResult(
                "QA Engineer", 50, Arrays.asList("partial match"));
        second.setRank(2);

        list.add(first);
        list.add(second);
        return list;
    }

    @Test
    public void insertAllThenFindByProfileIdReturnsAllRows() {
        resultDao.insertAll(twoRankedResults(), profileId);

        List<RecommendationResult> loaded = resultDao.findByProfileId(profileId);
        assertEquals(2, loaded.size());
    }

    @Test
    public void resultsAreReturnedOrderedByRank() {
        resultDao.insertAll(twoRankedResults(), profileId);

        List<RecommendationResult> loaded = resultDao.findByProfileId(profileId);
        assertEquals(1, loaded.get(0).getRank());
        assertEquals("Software Developer", loaded.get(0).getCareerName());
        assertEquals(2, loaded.get(1).getRank());
    }

    @Test
    public void scoreIsPersistedCorrectly() {
        resultDao.insertAll(twoRankedResults(), profileId);

        List<RecommendationResult> loaded = resultDao.findByProfileId(profileId);
        assertEquals(75, loaded.get(0).getScore());
    }
}
