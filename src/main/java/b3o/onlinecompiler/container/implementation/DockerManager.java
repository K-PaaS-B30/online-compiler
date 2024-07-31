package b3o.onlinecompiler.container.implementation;

import b3o.onlinecompiler.entity.ContainerContext;
import b3o.onlinecompiler.entity.ContainerStatus;
import b3o.onlinecompiler.container.abstraction.ContainerManager;
import b3o.onlinecompiler.container.callback.LogCaptureCallback;
import b3o.onlinecompiler.entity.Language;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;


@Component
public class DockerManager implements ContainerManager {
    @Autowired
    private DockerClient dockerClient;

    @Value("${docker.file.path}")
    private String DOCKER_FILE_PATH;

    private ContainerContext createContainer() {
        String imageId = dockerClient.buildImageCmd()
                .withDockerfile(new File(DOCKER_FILE_PATH))
                .exec(new BuildImageResultCallback())
                .awaitImageId();

        String containerId = dockerClient.createContainerCmd(imageId)
                .exec()
                .getId();

        return ContainerContext.create(imageId, containerId);
    }

    @Override
    public ContainerContext run(File sourceFile, Language language) {
        ContainerContext context = createContainer();

        dockerClient.copyArchiveToContainerCmd(context.getContainerId())
                .withHostResource(sourceFile.getAbsolutePath())
                .exec();

        dockerClient.startContainerCmd(context.getContainerId())
                .exec();

        context.setStatus(ContainerStatus.CONTAINER_BUILD);
        exec(context, language.generateBuildCommand(sourceFile.getName()));

        context.setStatus(ContainerStatus.CONTAINER_EXECUTE);
        exec(context, language.generateExecuteCommand(sourceFile.getName()));

        return context;
    }

    private ContainerContext exec(ContainerContext context, String[] commands){
        context.setStartTime(System.nanoTime());

        String execId = dockerClient.execCreateCmd(context.getContainerId())
                .withCmd(commands)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec()
                .getId();

        try {
            dockerClient.execStartCmd(execId)
                    .exec(new LogCaptureCallback(context))
                    .awaitCompletion();
        } catch (InterruptedException e){
            context.setStatus(ContainerStatus.CONTAINER_FAILURE);
        } finally {
            context.setEndTime(System.nanoTime());
        }

        return context;
    }

    @Override
    public ContainerContext stop(ContainerContext context) {
        dockerClient.stopContainerCmd(context.getContainerId())
                .exec();

        context.setStatus(ContainerStatus.CONTAINER_STOP);

        return context;
    }

    @Override
    public ContainerContext remove(ContainerContext context) {
        dockerClient.removeContainerCmd(context.getContainerId())
                .exec();

        dockerClient.removeImageCmd(context.getImageId())
                .exec();

        context.setStatus(ContainerStatus.CONTAINER_REMOVE);

        return context;
    }
}
