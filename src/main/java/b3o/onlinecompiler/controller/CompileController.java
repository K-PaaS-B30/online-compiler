package b3o.onlinecompiler.controller;

import b3o.onlinecompiler.dto.request.CompileRequestDto;
import b3o.onlinecompiler.dto.response.CompileResponseDto;
import b3o.onlinecompiler.service.CompileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class CompileController {
    @Autowired
    private CompileService compileService;

    @PostMapping("/compile")
    public CompileResponseDto compile(@RequestBody CompileRequestDto compileRequestDto) throws IOException {
        return compileService.compile(compileRequestDto);
    }
}
