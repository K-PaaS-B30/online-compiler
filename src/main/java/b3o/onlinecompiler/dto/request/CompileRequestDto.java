package b3o.onlinecompiler.dto.request;

import b3o.onlinecompiler.entity.Language;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompileRequestDto {
    private Language language;
    private String sourceCode;

    @JsonCreator
    public CompileRequestDto(@JsonProperty("language") Language language,
                             @JsonProperty("sourceCode") String sourceCode) {

        this.language = language;
        this.sourceCode = sourceCode;
    }
}
