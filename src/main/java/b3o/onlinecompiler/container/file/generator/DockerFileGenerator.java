package b3o.onlinecompiler.container.file.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class DockerFileGenerator {
    public static File create(String sourceFileName) throws IOException {
        String dockerFileName = UUID.randomUUID().toString();
        File dockerFile = new File(System.getenv("FILE_STORE") + "/" + dockerFileName);

        FileWriter fw = new FileWriter(dockerFile);

        String classFileName = sourceFileName.replace(".java", "");

        fw.write("FROM openjdk:17-alpine\n");
        fw.write("WORKDIR /\n");
        fw.write("USER root\n");
        fw.write("COPY " + sourceFileName + " /\n");
        fw.write("RUN javac " + sourceFileName + "\n");
        fw.write("CMD java " + classFileName);

        fw.close();

        return dockerFile;
    }

    public static void main(String[] args) throws IOException {
        create(UUID.randomUUID().toString());
    }
}
