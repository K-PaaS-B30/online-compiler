package b3o.onlinecompiler.container.abstraction;

import b3o.onlinecompiler.entity.Language;

import java.io.File;


public interface CodeConverter {
    public File textToSourceFile(String text, Language language);
}
