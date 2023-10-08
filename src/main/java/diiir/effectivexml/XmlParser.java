package diiir.effectivexml;

import java.io.InputStream;


public interface XmlParser {

    /**
     *
     * @param xml xml file
     * @param tag xml tag name
     * @return count of object with specified tag name
     */
    long parse(InputStream xml, String tag);

}
