package io.vertx.mod.workflow.atom.runtime;

import cn.vertxup.workflow.cv.em.PassWay;
import io.vertx.mod.workflow.error._409InValidTaskApiException;
import org.camunda.bpm.engine.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The call back of camunda workflow engine and it's related to gateway result.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WTask {

    /*
     * This tasks is matrix for 2 level
     * 1) taskKey = TaskMap
     * 2) TaskMap = taskId: Task
     *
     * It's based on gateway type and checking for task specification
     * 1. Standard -- size = 1 ( size = 1 )
     * 2. Fork     -- size = N ( size = 1 )
     * 3. Multi    -- size = 1 ( size = N )
     * 4. Grid     -- size = N ( size = N )
     */
    private final ConcurrentMap<String, ConcurrentMap<String, Task>> tasks = new ConcurrentHashMap<>();
    private final PassWay type;

    public WTask(final PassWay type) {
        Objects.requireNonNull(type);
        this.type = type;
    }

    public WTask add(final Task task) {
        if (Objects.nonNull(task)) {
            // Key 1: task key
            final String taskKey = task.getTaskDefinitionKey();
            final ConcurrentMap<String, Task> taskRef;
            if (this.tasks.containsKey(taskKey)) {
                taskRef = this.tasks.get(taskKey);
            } else {
                taskRef = new ConcurrentHashMap<>();
                this.tasks.put(taskKey, taskRef);
            }
            // Key 2: task id
            taskRef.put(task.getId(), task);
        }
        return this;
    }

    // ----------------- Mode for Different Usage -------------------

    public Task standard() {
        this.ensure(PassWay.Standard);
        if (this.tasks.isEmpty()) {
            return null;
        }
        final ConcurrentMap<String, Task> item = this.tasks.values().iterator().next();
        if (Objects.isNull(item) || item.isEmpty()) {
            return null;
        }
        return item.values().iterator().next();
    }

    public List<Task> multi() {
        this.ensure(PassWay.Multi);
        if (this.tasks.isEmpty()) {
            return new ArrayList<>();
        }
        final ConcurrentMap<String, Task> item = this.tasks.values().iterator().next();
        if (Objects.isNull(item) || item.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(item.values());
    }

    public ConcurrentMap<String, Task> fork() {
        this.ensure(PassWay.Fork);
        if (this.tasks.isEmpty()) {
            return new ConcurrentHashMap<>();
        }
        final ConcurrentMap<String, Task> taskMap = new ConcurrentHashMap<>();
        this.tasks.forEach((taskKey, valueMap) -> {
            final Task task = valueMap.values().iterator().next();
            taskMap.put(taskKey, task);
        });
        return taskMap;
    }

    public ConcurrentMap<String, List<Task>> grid() {
        this.ensure(PassWay.Grid);
        if (this.tasks.isEmpty()) {
            return new ConcurrentHashMap<>();
        }
        final ConcurrentMap<String, List<Task>> taskMap = new ConcurrentHashMap<>();
        this.tasks.forEach((taskKey, valueMap) -> {
            final List<Task> valueList = new ArrayList<>(valueMap.values());
            if (!valueList.isEmpty()) {
                taskMap.put(taskKey, valueList);
            }
        });
        return taskMap;
    }

    private void ensure(final PassWay expected) {
        if (expected != this.type) {
            throw new _409InValidTaskApiException(this.getClass(), this.type, expected);
        }
    }

    public PassWay vague() {
        return this.type;
    }

    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }
}
