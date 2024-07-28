package b3o.onlinecompiler.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class DockerManager implements ContainerManager {
    @Autowired
    DockerClient dockerClient;

    private String createContainer(File dockerFile) {
        String imageId = dockerClient.buildImageCmd()
                .withDockerfile(dockerFile)
                .exec(new BuildImageResultCallback())
                .awaitImageId();

        return dockerClient.createContainerCmd(imageId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec()
                .getId();
    }


    @Override
    public String start(File sourceFile, File dockerFile) {
        String containerId = createContainer(dockerFile);

        dockerClient.startContainerCmd(containerId).exec();

        StringBuilder containerOutput = new StringBuilder();
        /*
        try {
            dockerClient.logContainerCmd(containerId)
                    .withStdOut(true)
                    .withStdErr(true)
                    .exec(new ExecStartResultCallback(){
                        @Override
                        public void onNext(Frame frame){
                            containerOutput.append(new String(frame.getPayload()));
                        }
                    }).awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException("Log error!");
        }
        */
        return containerOutput.toString();
    }

    @Override
    public void stop(String containerId) {

    }

    @Override
    public void kill(String containerId) {

    }
}
