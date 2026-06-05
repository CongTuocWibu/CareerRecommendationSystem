package assignment;

import assignment.db.RecommendationResultDAO;
import assignment.db.UserProfileDAO;

import java.util.ArrayList;
import java.util.List;


public class RecommendationController {

    private final RecommendationEngine engine;

 
    private UserProfileDAO profileDao;
    private RecommendationResultDAO resultDao;

    private UserProfile currentProfile;
    private List<RecommendationResult> currentResults = new ArrayList<>();

    private final List<ModelListener> listeners = new ArrayList<>();

    public RecommendationController() {
        
        this.engine = new RecommendationEngine(RuleFactory.createDefaultRules());
    }

    public void addListener(ModelListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(ModelListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (ModelListener listener : listeners) {
            listener.onResultsChanged(currentResults);
        }
    }

    private UserProfileDAO profiles() {
        if (profileDao == null) {
            profileDao = new UserProfileDAO();
        }
        return profileDao;
    }

    private RecommendationResultDAO resultsDao() {
        if (resultDao == null) {
            resultDao = new RecommendationResultDAO();
        }
        return resultDao;
    }


    public List<RecommendationResult> generate(UserProfile profile) {
        this.currentProfile = profile;
        this.currentResults = engine.generate(profile);
        notifyListeners();   
        return currentResults;
    }


    public int saveToDatabase() {
        if (currentProfile == null || currentResults.isEmpty()) {
            return -1;
        }
        int profileId = profiles().insert(currentProfile);
        if (profileId != -1) {
            resultsDao().insertAll(currentResults, profileId);
        }
        return profileId;
    }


    public List<RecommendationResult> loadLatestFromDatabase() {
        int latestId = profiles().findLatestId();
        if (latestId == -1) {
            currentResults = new ArrayList<>();
            notifyListeners();
            return currentResults;
        }
        currentProfile = profiles().findById(latestId);
        currentResults = resultsDao().findByProfileId(latestId);
        notifyListeners();   
        return currentResults;
    }


    public UserProfile getCurrentProfile() {
        return currentProfile;
    }

    public List<RecommendationResult> getCurrentResults() {
        return currentResults;
    }

    public boolean hasResults() {
        return currentResults != null && !currentResults.isEmpty();
    }

    public List<CareerPath> getAvailableCareers() {
        return CareerPath.getPredefinedCareers();
    }
}