package b3o.onlinecompiler.entity;

public enum ContainerStatus {
    CREATE,
    BUILD,
    EXECUTE,
    STOP,
    REMOVE,
    FAILURE;

    private static final ContainerStatus[] statuses = ContainerStatus.values();

    public ContainerStatus nextStatus(){
        int currentIndex = this.ordinal();
        int nextIndex = currentIndex + 1;

        if (nextIndex < statuses.length && statuses[nextIndex] != FAILURE) {
            return statuses[nextIndex];
        }

        return this;
    }

    public ContainerStatus failure(){
        return FAILURE;
    }
}
