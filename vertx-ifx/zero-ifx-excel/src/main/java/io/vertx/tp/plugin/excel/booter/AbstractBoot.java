package io.vertx.tp.plugin.excel.booter;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.plugin.excel.atom.ExConnect;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractBoot implements ExBoot {
    private transient final ConcurrentMap<String, ExConnect> connects =
        new ConcurrentHashMap<>();
    private transient final List<String> files = new ArrayList<>();

    public AbstractBoot(final String module) {
        final String root = "plugin/" + module + "/oob/";
        final String excelYaml = root + "initialize.yml";
        final JsonArray data = Ut.ioYaml(excelYaml);
        final List<ExConnect> connectList = Ut.deserialize(data, new TypeReference<List<ExConnect>>() {
        });
        connectList.stream().filter(Objects::nonNull)
            .filter(connect -> Objects.nonNull(connect.getTable()))
            .forEach(connect -> this.connects.put(connect.getTable(), connect));
        final JsonArray fileData = Ut.ioJArray(root + "initialize.json");
        Ut.itJArray(fileData, String.class, (item, index) -> this.files.add(item));
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    @Override
    public ConcurrentMap<String, ExConnect> configure() {
        return this.connects;
    }

    @Override
    public List<String> oob() {
        return this.files;
    }
}
