package b3o.onlinecompiler.dto.response;

import b3o.onlinecompiler.entity.ContainerContext;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompileResponseDto {
    private String status;
    private long durationTime;
    private String stdout;
    private String stderr;

    public static CompileResponseDto fromContext(ContainerContext context){
        return CompileResponseDto.builder()
                .status(context.getStatus().name())
                .durationTime(context.getEndTime() - context.getStartTime())
                .stdout(context.getContainerStdout().toString())
                .stderr(context.getContainerStderr().toString())
                .build();
    }
}
