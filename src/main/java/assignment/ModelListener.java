package assignment;

import java.util.List;


public interface ModelListener {


    void onResultsChanged(List<RecommendationResult> results);
}
