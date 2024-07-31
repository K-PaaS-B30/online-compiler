package b3o.onlinecompiler.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.ByteArrayOutputStream;


@Builder
@Getter
@ToString
public class ContainerContext {
    private String imageId;
    private String containerId;
    private ByteArrayOutputStream containerStdout;
    private ByteArrayOutputStream containerStderr;
    private ContainerStatus status;
    private long startTime;
    private long endTime;

    public static ContainerContext create(String imageId, String containerId, long startTime, long endTime){
        return ContainerContext.builder()
                .imageId(imageId)
                .containerId(containerId)
                .containerStdout(new ByteArrayOutputStream())
                .containerStderr(new ByteArrayOutputStream())
                .status(ContainerStatus.CREATE)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    public void start(){
        this.startTime = System.nanoTime();
    }

    public void end(){
        this.endTime = System.nanoTime();
    }

    public void nextStatus(){
        this.status = this.status.nextStatus();
    }

    public void failure(){
        this.status = this.getStatus().failure();
    }
}
