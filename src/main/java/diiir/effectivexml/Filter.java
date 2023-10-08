package diiir.effectivexml;

import java.util.Map;

/**
 * This filter need to add some custom logic, for xml objects filtration
 */
public interface Filter {

    /**
     *
     * @param row object from xml
     * @return true if this object need to be observed, otherwise false
     */
    boolean isAccepted(Map<String, Object> row);

}
