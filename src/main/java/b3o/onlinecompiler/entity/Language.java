package b3o.onlinecompiler.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Language {
    JAVA("javac", "java", ".java", "");

    private final String buildCommand;
    private final String executeCommand;
    private final String sourceExtension;
    private final String executableExtension;

    public String[] generateBuildCommand(String fileName){
        return new String[]{ this.buildCommand, fileName };
    }

    public String[] generateExecuteCommand(String fileName) {
        fileName = fileName.replace(this.sourceExtension, this.executableExtension);
        return new String[]{ this.executeCommand, fileName };
    }
}
