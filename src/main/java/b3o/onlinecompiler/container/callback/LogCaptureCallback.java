package b3o.onlinecompiler.container.callback;

import b3o.onlinecompiler.entity.ContainerContext;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class LogCaptureCallback extends ExecStartResultCallback {
    ContainerContext context;

    @Override
    public void onNext(Frame frame) {
        try {
            if (frame.getStreamType() == StreamType.STDOUT) {
                writeStdout(frame.getPayload());

            } else if (frame.getStreamType() == StreamType.STDERR) {
                writeStderr(frame.getPayload());
            }
        } catch (Exception e) {
            context.failure();
        }

        super.onNext(frame);
    }

    private void writeStdout(byte[] payload) throws IOException {
        context.getContainerStdout().write(payload);
    }

    private void writeStderr(byte[] payload) throws IOException {
        context.getContainerStderr().write(payload);
    }
}
