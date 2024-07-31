package b3o.onlinecompiler.service;

import b3o.onlinecompiler.container.abstraction.CodeConverter;
import b3o.onlinecompiler.container.abstraction.ContainerManager;
import b3o.onlinecompiler.dto.request.CompileRequestDto;
import b3o.onlinecompiler.dto.response.CompileResponseDto;
import b3o.onlinecompiler.entity.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class CompileService {
    @Autowired
    ContainerManager containerManager;

    @Autowired
    CodeConverter sourceFileGenerator;

    public CompileResponseDto compile(CompileRequestDto compileRequestDto) {
        Language language = compileRequestDto.getLanguage();
        String text = compileRequestDto.getSourceCode();

        File sourceFile = sourceFileGenerator.textToSourceFile(text, language);

        return CompileResponseDto.fromContext(containerManager.run(sourceFile, compileRequestDto.getLanguage()));
    }
}
