package b3o.onlinecompiler.container.implementation;

import b3o.onlinecompiler.entity.ContainerContext;
import b3o.onlinecompiler.container.abstraction.ContainerManager;
import b3o.onlinecompiler.container.callback.LogCaptureCallback;
import b3o.onlinecompiler.entity.Language;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.io.*;

@Log4j2
@Component
public class DockerManager implements ContainerManager {
    @Autowired
    private DockerClient dockerClient;

    @Value("${docker.file.path}")
    private String DOCKER_FILE_PATH;

    @Override
    public ContainerContext run(File sourceFile, Language language) {
        ContainerContext context = createContainer();

        dockerClient.copyArchiveToContainerCmd(context.getContainerId())
                .withHostResource(sourceFile.getAbsolutePath())
                .exec();

        log.info("Move source file [" + sourceFile.getName() + "] to Container id [" + context.getContainerId() + "]");

        dockerClient.startContainerCmd(context.getContainerId())
                .exec();

        log.info("Container id [" + context.getContainerId() + "] is start");

        exec(context, language.generateBuildCommand(sourceFile.getName()));
        exec(context, language.generateExecuteCommand(sourceFile.getName()));

        return context;
    }

    @Async
    @Override
    public void stop(ContainerContext context) {
        context.start();

        dockerClient.stopContainerCmd(context.getContainerId())
                .exec();

        try {
            dockerClient.waitContainerCmd(context.getContainerId())
                    .exec(new WaitContainerResultCallback())
                    .awaitCompletion();
        } catch (InterruptedException e) {
            log.error("container stop exception");
            throw new RuntimeException("Container stop exception");
        }

        context.end();
        context.nextStatus();

        log.info("Container id [" + context.getContainerId() + "] is stop");

        removeContainer(context);
    }

    private ContainerContext createContainer() {
        long startTime = System.nanoTime();

        String imageId = dockerClient.buildImageCmd()
                .withDockerfile(new File(DOCKER_FILE_PATH))
                .exec(new BuildImageResultCallback())
                .awaitImageId();

        String containerId = dockerClient.createContainerCmd(imageId)
                .exec()
                .getId();

        long endTime = System.nanoTime();

        log.info("Image build success, image id [" + imageId + "]");

        log.info("Container create success, container id [" + containerId + "]");

        return ContainerContext.create(imageId, containerId, startTime, endTime);
    }

    private ContainerContext exec(ContainerContext context, String[] commands){
        context.start();

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
            context.failure();
        } finally {
            context.end();
            context.nextStatus();

            log.info("Execute command : [" + commands[0] + "] [" + commands[1] + "]");
            log.info("Duration time : " + Math.abs((context.getEndTime() - context.getStartTime()) / 1_000_000_000.0));
            log.info("[Container stdout]\n" + context.getContainerStdout());
            log.info("[Container stderr]\n" + context.getContainerStderr());
        }

        return context;
    }

    private void removeContainer(ContainerContext context) {
        context.start();

        dockerClient.removeContainerCmd(context.getContainerId())
                .exec();

        dockerClient.removeImageCmd(context.getImageId())
                .exec();

        context.end();
        context.nextStatus();

        log.info("Container id : [" + context.getContainerId() + "] is remove");
        log.info("Image id : [" + context.getImageId() + "] is remove");
    }
}
