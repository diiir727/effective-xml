package diiir.effectivexml;

import java.io.File;
import java.io.IOException;

public class Utils {

    public static File getFile(String path) throws IOException {
        ClassLoader classLoader = Utils.class.getClassLoader();
        var resource = classLoader.getResource(path);
        if (resource != null) {
            return new File(resource.getFile());
        }
        throw new IOException("can't find resource: " + path);
    }

}
