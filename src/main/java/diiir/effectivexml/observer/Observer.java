package diiir.effectivexml.observer;

import java.util.Map;

public interface Observer {


    /**
     *
     * @param freshObj obj from xml, that already converted to Map.
     *                 the nesting hierarchy is preserved as in the original.
     *                 The value of tag can be found by <B>#val</B> key.
     *                 Properties of xml objects can be found by <B>@nameOfProperty</B>
     */
    void observe(Map<String, Object> freshObj);

}
