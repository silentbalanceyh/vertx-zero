package io.vertx.tp.plugin.shell;

import io.horizon.eon.em.Environment;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.tp.plugin.shell.atom.CommandAtom;
import io.vertx.tp.plugin.shell.atom.CommandInput;
import io.vertx.tp.plugin.shell.cv.em.TermStatus;
import io.vertx.tp.plugin.shell.refine.Sl;
import io.horizon.uca.log.Annal;
import io.vertx.up.runtime.ZeroSerializer;
import io.vertx.up.util.Ut;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractCommander implements Commander {
    protected transient CommandAtom atom;
    protected transient Vertx vertxRef;
    protected transient Environment environment = Environment.Production;

    @Override
    public Commander bind(final Environment environment) {
        if (Objects.nonNull(environment)) {
            this.environment = environment;
        }
        return this;
    }

    @Override
    public Commander bind(final CommandAtom atom) {
        this.atom = atom;
        return this;
    }

    @Override
    public Commander bind(final Vertx vertx) {
        this.vertxRef = vertx;
        return this;
    }

    @Override
    public Future<TermStatus> executeAsync(final CommandInput args) {
        return Future.succeededFuture(this.execute(args));
    }

    @Override
    public TermStatus execute(final CommandInput args) {
        return TermStatus.SUCCESS;
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    /*
     * Help method to read arguments from input
     */
    protected String inString(final CommandInput input, final String name) {
        return this.inValue(input, name, String.class);
    }

    protected Integer inInteger(final CommandInput input, final String name) {
        return this.inValue(input, name, Integer.class);
    }

    protected Boolean inBoolean(final CommandInput input, final String name) {
        return this.inValue(input, name, Boolean.class);
    }

    protected String inFolder(final CommandInput input, final String name, final Function<String, String> processor) {
        String folder = this.inValue(input, name, String.class);
        if (Objects.nonNull(processor)) {
            folder = processor.apply(folder);
        }
        return this.inFolder(folder);
    }

    protected String inFolder(final CommandInput input, final String name) {
        return this.inFolder(input, name, null);
    }

    protected String inFolder(final String folderName) {
        final File file = new File(folderName);
        if (!file.exists()) {
            final boolean created = file.mkdirs();
            Sl.output("Folder does not exist, created: {0}", created);
        }
        return file.getAbsolutePath();
    }

    /*
     * Read json object / array from input file
     */
    @SuppressWarnings("unchecked")
    protected <T> T inJson(final CommandInput input, final String name) {
        final String filename = this.inValue(input, name, String.class);
        final String content = Ut.ioString(filename);
        if (Ut.isJArray(content)) {
            return (T) Ut.toJArray(content);
        } else if (Ut.isJObject(content)) {
            return (T) Ut.toJObject(content);
        } else {
            return (T) content;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T inValue(final CommandInput input, final String name, final Class<T> clazzT) {
        /* Input Argument Map */
        final ConcurrentMap<String, String> inputMap = input.get();
        final String literal = inputMap.get(name);
        if (Ut.isNil(literal)) {
            return this.atom.getDefault(name);
        } else {
            return (T) ZeroSerializer.getValue(clazzT, literal);
        }
    }
}