package diiir.effectivexml;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import static diiir.effectivexml.Utils.getFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StreamParserTest {


    @Test
    public void testArrayItems() throws Exception {

        var observer = new ObserverMock();
        var filter = mock(Filter.class);
        when(filter.isAccepted(any())).thenReturn(true);
        var parser = new StreamParser(observer, filter);

        try (var is = new FileInputStream(getFile("food-menu.xml"))){
            var count = parser.parse(is, "food");
            Assertions.assertEquals(3, count);
        }

        Assertions.assertEquals("Belgian Waffles", ((Map<String, Object>)observer.getFirstObj().get("name")).get("#val"));
        Assertions.assertEquals("650", ((Map<String, Object>)observer.getFirstObj().get("calories")).get("#val"));
        Assertions.assertEquals("EUR", ((Map<String, Object>)observer.getFirstObj().get("price")).get("@share"));
    }


    @Test
    public void testFullXmlParse() throws Exception {

        var observer = new ObserverMock();
        var filter = mock(Filter.class);
        when(filter.isAccepted(any())).thenReturn(true);
        var parser = new StreamParser(observer, filter);

        try (var is = new FileInputStream(getFile("food-menu.xml"))){
            var count = parser.parse(is, "breakfast_menu");
            Assertions.assertEquals(1, count);
        }

        var foodList = ((List<Map<String, Object>>)observer.getFirstObj().get("food"));

        Assertions.assertEquals(3, foodList.size());
        Assertions.assertEquals("Berry-Berry Belgian Waffles", ((Map<String, Object>)foodList.get(2).get("name")).get("#val"));
        Assertions.assertEquals("900", ((Map<String, Object>)foodList.get(2).get("calories")).get("#val"));
    }


}
