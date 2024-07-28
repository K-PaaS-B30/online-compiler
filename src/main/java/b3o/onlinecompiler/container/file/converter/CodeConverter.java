package b3o.onlinecompiler.container.file.converter;

import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

@Component
public class CodeConverter {
    private static final String storePath = System.getenv("FILE_STORE");

    public static File generate(String text, String fileExtension) {
        String fileName = "_" + UUID.randomUUID().toString();
        fileName = fileName.replaceAll("-", "");
        text = modifyClassName(text, fileName);
        File sourceFile = new File(storePath + "/" + fileName + fileExtension);

        try {
            FileWriter fw = new FileWriter(sourceFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.close();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sourceFile;
    }

    private static String modifyClassName(String text, String newClassName) {
        StringBuffer sb = new StringBuffer();
        int classNamePosition = text.indexOf("class") + 6;

        sb.append(text.substring(0, classNamePosition));
        sb.append(newClassName);

        int startBracePosition = text.indexOf(" {");
        int endBracePosition = text.lastIndexOf("}");

        sb.append(text.substring(startBracePosition, endBracePosition));
        sb.append("}");

        return sb.toString();
    }
}
