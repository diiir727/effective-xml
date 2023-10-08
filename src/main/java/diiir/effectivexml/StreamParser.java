package diiir.effectivexml;

import diiir.effectivexml.observer.Observer;
import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Memory efficient xml parser, based on XMLStreamReader.
 * Parse all object with specified tag name, every found object convert to Map<String,Object>
 * and send to Observer one by one.
 */
@Slf4j
public class StreamParser implements XmlParser {

    private final Observer observer;
    private Filter filter;

    public StreamParser(Observer observer) {
        this.observer = observer;
    }

    public StreamParser(Observer observer, Filter filter) {
        this.observer = observer;
        this.filter = filter;
    }

    /**
     * Parse all objects with the specified tag name.
     * @param xml xml file
     * @param tag xml tag name
     * @return count of found objects
     */
    @Override
    public long parse(InputStream xml, String tag) {
        try {
            AtomicLong counter = new AtomicLong();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            var reader = factory.createXMLStreamReader(xml);
            parse(reader, tag, counter);
            return counter.get();
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private void parse(XMLStreamReader reader, String tag, AtomicLong counter) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (XMLStreamConstants.START_ELEMENT == event && tag.equals(reader.getLocalName())) {
                var el = getData(reader);
                if (!reader.isEndElement()) {
                    parseElement(reader, el, tag);
                }
                if (filter == null || filter.isAccepted(el)) {
                    counter.incrementAndGet();
                    observer.observe(el);
                }
            }
        }
    }

    private void parseElement(XMLStreamReader reader, Map<String, Object> el, String tag) throws XMLStreamException {
        var lastTagName = "";
        List<Map<String, Object>> innerArray = new ArrayList<>();
        while (reader.hasNext()) {
            int event = reader.getEventType();
            if (XMLStreamConstants.END_ELEMENT == event && tag.equals(reader.getLocalName())) {
                break;
            }
            if (XMLStreamConstants.START_ELEMENT == event) {
                var localName = reader.getLocalName();
                var elData = getData(reader);
                parseElement(reader, elData, localName);
                if ((!lastTagName.isEmpty() && !lastTagName.equals(localName))) {
                    el.put(lastTagName, innerArray.size() == 1 ? innerArray.get(0) : new ArrayList<>(innerArray));
                    innerArray.clear();
                }
                innerArray.add(elData);
                lastTagName = localName;
            }
            reader.next();
        }

        if (!innerArray.isEmpty()) {
            el.put(lastTagName, innerArray.size() == 1 ? innerArray.get(0) : new ArrayList<>(innerArray));
        }

    }

    private Map<String, Object> getData(XMLStreamReader reader) {
        var el = new HashMap<String, Object>();
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            el.put("@" + reader.getAttributeName(i).getLocalPart(), reader.getAttributeValue(i));
        }
        try {
            var elVal = reader.getElementText();
            if (elVal != null && !elVal.isEmpty()) {
                el.put("#val", elVal);
            }
        } catch (Exception e) {
            log.trace("Can't get element value", e);
        }
        return el;
    }

}
