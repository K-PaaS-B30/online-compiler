package b3o.onlinecompiler.container.callback;

import b3o.onlinecompiler.entity.ContainerContext;
import b3o.onlinecompiler.entity.ContainerStatus;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LogCaptureCallback extends ExecStartResultCallback {
    ContainerContext context;

    @Override
    public void onNext(Frame frame) {
        try {
            if (frame.getStreamType() == StreamType.STDOUT) {
                context.getContainerStdout().write(frame.getPayload());

            } else if (frame.getStreamType() == StreamType.STDERR) {
                context.setStatus(ContainerStatus.CONTAINER_FAILURE);
                context.getContainerStderr().write(frame.getPayload());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onNext(frame);
    }
}
