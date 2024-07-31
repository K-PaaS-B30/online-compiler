package b3o.onlinecompiler.dto.response;

import b3o.onlinecompiler.entity.ContainerContext;
import lombok.Builder;
import lombok.Getter;

import java.text.DecimalFormat;

@Getter
@Builder
public class CompileResponseDto {
    private String status;
    private String durationTime;
    private String stdout;
    private String stderr;

    public static CompileResponseDto fromContext(ContainerContext context){
        double second = ((double) context.getStartTime() - (double) context.getEndTime()) / 1_000_000_000.0;
        DecimalFormat df = new DecimalFormat("#.####");

        return CompileResponseDto.builder()
                .status(context.getStatus().name())
                .durationTime(df.format(Math.abs(second)))
                .stdout(context.getContainerStdout().toString())
                .stderr(context.getContainerStderr().toString())
                .build();
    }
}
