package fh.sem.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceManager {
    public enum Location {JAR, APPDIR}

    private static ResourceManager singleton;
    private ResourceManager() {}

    public static ResourceManager instance() {
        if(singleton == null)
            singleton = new ResourceManager();

        return singleton;
    }

    public InputStream get(String path, Location location) {
        InputStream resource = null;

        if(location == Location.JAR)
            resource = getClass().getResourceAsStream(path);

        if(location == Location.APPDIR) try {
            resource = new FileInputStream(new File(path));
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return resource;
    }

    public InputStream get(String path) {
        InputStream resource = get(path, Location.APPDIR);
        return resource == null ? get(path, Location.JAR) : resource;
    }
}