package b3o.onlinecompiler.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompileResponseDto {
    private String output;
}
