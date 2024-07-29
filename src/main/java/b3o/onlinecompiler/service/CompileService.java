package b3o.onlinecompiler.service;

import b3o.onlinecompiler.container.DockerManager;
import b3o.onlinecompiler.container.file.converter.CodeConverter;
import b3o.onlinecompiler.container.file.generator.DockerFileGenerator;
import b3o.onlinecompiler.dto.request.CompileRequestDto;
import b3o.onlinecompiler.dto.response.CompileResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class CompileService {
    @Autowired
    DockerManager dockerManager;

    public CompileResponseDto compile(CompileRequestDto compileRequestDto) throws IOException {
        String fileExtension = compileRequestDto.getLanguage().getExtension();
        String text = compileRequestDto.getSourceCode();

        File sourceFile = CodeConverter.generate(text, fileExtension);
        File dockerFile = DockerFileGenerator.create(sourceFile.getName());

        String output = dockerManager.run(sourceFile, dockerFile);

        return CompileResponseDto.builder().output(output).build();
    }
}
