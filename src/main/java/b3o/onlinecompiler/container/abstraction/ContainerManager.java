package b3o.onlinecompiler.container.abstraction;

import b3o.onlinecompiler.entity.ContainerContext;
import b3o.onlinecompiler.entity.Language;

import java.io.File;

public interface ContainerManager {
    public ContainerContext run(File sourceFile, Language language);
    public ContainerContext stop(ContainerContext context);
    public ContainerContext remove(ContainerContext context);
}
