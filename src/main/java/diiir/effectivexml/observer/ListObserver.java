package diiir.effectivexml.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListObserver implements Observer {

    private final List<Map<String, Object>> results = new ArrayList<>();

    @Override
    public void observe(Map<String, Object> freshObj) {
        results.add(freshObj);
    }

    public List<Map<String, Object>> getResults() {
        return results;
    }
}
