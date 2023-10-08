package diiir.effectivexml;

import diiir.effectivexml.observer.Observer;
import lombok.Getter;

import java.util.Map;

@Getter
public class ObserverMock implements Observer {


    private int count;
    private Map<String, Object> firstObj;

    @Override
    public void observe(Map<String, Object> obj) {
        count++;
        firstObj = firstObj == null ? obj : firstObj;
    }
}
