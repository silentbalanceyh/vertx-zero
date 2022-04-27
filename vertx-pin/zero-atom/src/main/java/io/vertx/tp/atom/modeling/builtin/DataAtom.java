package io.vertx.tp.atom.modeling.builtin;

import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.experiment.mixture.HModel;
import io.vertx.up.experiment.shape.AbstractHAtom;
import io.vertx.up.experiment.shape.atom.HAtomMetadata;
import io.vertx.up.experiment.shape.atom.HAtomReference;
import io.vertx.up.uca.cache.Cc;

import java.util.Set;

/**
 * 内部使用的元数据分析工具，提供
 * 当前 DataRecord的专用 辅助工具，核心元数据处理工厂
 */
public class DataAtom extends AbstractHAtom {
    private static final Cc<Integer, AtomMarker> CC_MARKER = Cc.open();
    private transient final AtomMarker marker;

    public DataAtom(final Model model, final String appName) {
        super(model, appName);

        final Integer modelCode = model.hashCode();
        this.marker = CC_MARKER.pick(() -> new AtomMarker(model), modelCode);
    }


    @Override
    protected <T extends HModel> HAtomMetadata newMetadata(final T model) {
        return new AtomMetadata((Model) model);
    }

    @Override
    protected <T extends HModel> HAtomReference newReference(final T model) {
        return new AtomReference((Model) model, this.appName);
    }

    @Override
    public DataAtom atom(final String identifier) {
        return Ao.toAtom(this.appName, identifier);
    }

    @Override
    public String sigma() {
        return ((AtomMetadata) this.metadata).sigma();
    }

    @Override
    public String language() {
        return ((AtomMetadata) this.metadata).language();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Model model() {
        return (Model) super.model();
    }

    // ------------ 属性检查的特殊功能，收集相关属性 ----------
    /*
     * 模型本身打开Track属性
     */
    @Override
    public Boolean trackable() {
        return this.marker.trackable();
    }

    /*
     * 解决空指针问题，
     * isTrack
     * isConfirm
     * isSyncIn
     * isSyncOut
     *
     * 关于这四个属性需要详细说明
     * 1. Track：是否生成变更历史, 001
     * 2. Confirm：是否生成待确认, 002
     * 3. SyncIn：同步拉取, 003
     * 4. SyncOut：同步推送, 004
     */
    @Override
    public Set<String> falseTrack() {
        return this.marker.track(Boolean.FALSE);
    }

    @Override
    public Set<String> trueTrack() {
        return this.marker.track(Boolean.TRUE);
    }

    @Override
    public Set<String> falseIn() {
        return this.marker.in(Boolean.FALSE);
    }

    /*
     * 集成过程中引入
     */
    @Override
    public Set<String> trueIn() {
        return this.marker.in(Boolean.TRUE);
    }

    @Override
    public Set<String> falseOut() {
        return this.marker.out(Boolean.FALSE);
    }

    @Override
    public Set<String> trueOut() {
        return this.marker.out(Boolean.TRUE);
    }

    @Override
    public Set<String> falseConfirm() {
        return this.marker.confirm(Boolean.FALSE);
    }

    @Override
    public Set<String> trueConfirm() {
        return this.marker.confirm(Boolean.TRUE);
    }
}
