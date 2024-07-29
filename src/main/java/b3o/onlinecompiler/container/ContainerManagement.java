package b3o.onlinecompiler.container;

import java.io.File;

public interface ContainerManagement {
    public String run(File sourceFile, File dockerFile);
}
