package b3o.onlinecompiler.container.abstraction;

import b3o.onlinecompiler.entity.ContainerContext;
import b3o.onlinecompiler.entity.Language;

import java.io.File;

public interface ContainerManager {
    ContainerContext run(File sourceFile, Language language);
    void stop(ContainerContext context);
}
