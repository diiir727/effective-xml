package diiir.effectivexml;

import diiir.effectivexml.observer.ListObserver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

public class ListObserverTest {


    @Test
    public void testResults() throws Exception {
        var observer = new ListObserver();
        var parser = new StreamParser(observer);

        try (var is = new FileInputStream(Utils.getFile("food-menu.xml"))){
            parser.parse(is, "food");
        }

        List<Map<String, Object>> results = observer.getResults();

        Assertions.assertEquals(3, results.size());
        Assertions.assertEquals("Belgian Waffles", ((Map<String, Object>)results.get(0).get("name")).get("#val"));
        Assertions.assertEquals("EUR", ((Map<String, Object>)results.get(0).get("price")).get("@share"));
    }



}
