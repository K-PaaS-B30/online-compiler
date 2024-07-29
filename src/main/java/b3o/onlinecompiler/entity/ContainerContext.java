package b3o.onlinecompiler.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;

@Builder
@Setter
@Getter
public class ContainerContext {
    private String imageId;
    private String containerId;
    private ByteArrayOutputStream containerStdout;
    private ByteArrayOutputStream containerStderr;
    private ContainerStatus status;
    private long startTime;
    private long endTime;

    public static ContainerContext create(String imageId, String containerId){
        return ContainerContext.builder()
                .imageId(imageId)
                .containerId(containerId)
                .containerStdout(new ByteArrayOutputStream())
                .containerStderr(new ByteArrayOutputStream())
                .status(ContainerStatus.CONTAINER_CREATE)
                .startTime(0)
                .endTime(0)
                .build();
    }
}
