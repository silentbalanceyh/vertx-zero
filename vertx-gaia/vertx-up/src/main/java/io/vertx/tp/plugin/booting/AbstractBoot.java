package io.vertx.tp.plugin.booting;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractBoot implements KBoot {
    private transient final ConcurrentMap<String, KConnect> connects =
        new ConcurrentHashMap<>();
    private transient final ConcurrentMap<String, JsonObject> modules =
        new ConcurrentHashMap<>();
    private transient final ConcurrentMap<String, JsonArray> columns =
        new ConcurrentHashMap<>();
    private transient final List<String> files = new ArrayList<>();
    private transient final String root;

    public AbstractBoot(final String module) {
        this.root = "plugin/" + module + "/oob/";
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    @Override
    public ConcurrentMap<String, KConnect> configure() {
        if (this.connects.isEmpty()) {
            final String excelYaml = this.root + "initialize.yml";
            final JsonArray data = Ut.ioYaml(excelYaml);
            final List<KConnect> connectList = Ut.deserialize(data, new TypeReference<List<KConnect>>() {
            });
            connectList.stream().filter(Objects::nonNull)
                .filter(connect -> Objects.nonNull(connect.getTable()))
                .forEach(connect -> this.connects.put(connect.getTable(), connect));
            final JsonArray fileData = Ut.ioJArray(this.root + "initialize.json");
            Ut.itJArray(fileData, String.class, (item, index) -> this.files.add(item));
        }
        return this.connects;
    }

    @Override
    public List<String> oob() {
        return this.files;
    }

    @Override
    public List<String> oob(final String prefix) {
        return this.files.stream()
            .filter(item -> item.contains(prefix))
            .collect(Collectors.toList());
    }

    @Override
    public ConcurrentMap<String, JsonObject> module() {
        if (this.modules.isEmpty()) {
            final Set<String> modules = this.moduleSet();
            modules.forEach(item -> {
                final String moduleJson = this.root + "module/crud/" + item + ".json";
                final JsonObject module = Ut.ioJObject(moduleJson);
                if (Ut.notNil(module)) {
                    this.modules.put(item, module);
                }
            });
        }
        return this.modules;
    }

    @Override
    public ConcurrentMap<String, JsonArray> column() {
        if (this.columns.isEmpty()) {
            final Set<String> modules = this.moduleSet();
            modules.forEach(item -> {
                final String moduleJson = this.root + "module/ui/" + item + ".json";
                final JsonArray module = Ut.ioJArray(moduleJson);
                if (Ut.notNil(module)) {
                    this.columns.put(item, module);
                }
            });
        }
        return this.columns;
    }

    private Set<String> moduleSet() {
        final String moduleJson = this.root + "module.json";
        final JsonArray module = Ut.ioJArray(moduleJson);
        return Ut.toSet(module);
    }
}
