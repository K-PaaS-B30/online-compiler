package b3o.onlinecompiler.entity;

public enum Language {
    C(".c"),
    CPP(".cpp"),
    JAVA(".java"),
    Python(".py");

    private final String extension;

    Language(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return this.extension;
    }
}
