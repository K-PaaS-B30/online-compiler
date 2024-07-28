package b3o.onlinecompiler.container;

import java.io.File;
import java.io.FileNotFoundException;

public interface ContainerManager {
    public String start(File sourceFile, File dockerFile) throws FileNotFoundException;
    public void stop(String containerId);
    public void kill(String containerId);
}
