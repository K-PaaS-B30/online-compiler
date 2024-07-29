package b3o.onlinecompiler.container;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Frame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class DockerManager implements ContainerManagement {
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
    public String run(File sourceFile, File dockerFile) {
        String containerId = createContainer(dockerFile);

        dockerClient.startContainerCmd(containerId).exec();

        try {
            dockerClient.waitContainerCmd(containerId)
                    .exec(new WaitContainerResultCallback())
                    .awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StringBuilder containerOutput = new StringBuilder();

        // 실행이 완료된 컨테이너의 로그를 가져오는 코드
        try {
            dockerClient.logContainerCmd(containerId)
                    .withStdOut(true)
                    .withStdErr(true)
                    .exec(new ResultCallback.Adapter<Frame>() {
                        @Override
                        public void onNext(Frame frame) {
                            String output = new String(frame.getPayload());
                            System.out.println(output);
                            containerOutput.append(output);
                        }
                    }).awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return containerOutput.toString();
    }

    private void stop(String containerId) {

    }

    private void kill(String containerId) {

    }
}
