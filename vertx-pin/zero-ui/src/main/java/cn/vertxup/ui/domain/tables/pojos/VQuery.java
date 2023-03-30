/*
 * This file is generated by jOOQ.
 */
package cn.vertxup.ui.domain.tables.pojos;


import cn.vertxup.ui.domain.tables.interfaces.IVQuery;
import io.github.jklingsporn.vertx.jooq.shared.internal.VertxPojo;
/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VQuery implements VertxPojo, IVQuery {

    private static final long serialVersionUID = 1L;

    private String key;
    private String projection;
    private String pager;
    private String sorter;
    private String criteria;

    public VQuery() {}

    public VQuery(IVQuery value) {
        this.key = value.getKey();
        this.projection = value.getProjection();
        this.pager = value.getPager();
        this.sorter = value.getSorter();
        this.criteria = value.getCriteria();
    }

    public VQuery(
        String key,
        String projection,
        String pager,
        String sorter,
        String criteria
    ) {
        this.key = key;
        this.projection = projection;
        this.pager = pager;
        this.sorter = sorter;
        this.criteria = criteria;
    }

        public VQuery(io.vertx.core.json.JsonObject json) {
                this();
                fromJson(json);
        }

    /**
     * Getter for <code>DB_ETERNAL.V_QUERY.KEY</code>. 「key」- 选项主键
     */
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Setter for <code>DB_ETERNAL.V_QUERY.KEY</code>. 「key」- 选项主键
     */
    @Override
    public VQuery setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.V_QUERY.PROJECTION</code>. 「projection」-
     * query/projection:[], 默认列过滤项
     */
    @Override
    public String getProjection() {
        return this.projection;
    }

    /**
     * Setter for <code>DB_ETERNAL.V_QUERY.PROJECTION</code>. 「projection」-
     * query/projection:[], 默认列过滤项
     */
    @Override
    public VQuery setProjection(String projection) {
        this.projection = projection;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.V_QUERY.PAGER</code>. 「pager」-
     * query/pager:{}, 分页选项
     */
    @Override
    public String getPager() {
        return this.pager;
    }

    /**
     * Setter for <code>DB_ETERNAL.V_QUERY.PAGER</code>. 「pager」-
     * query/pager:{}, 分页选项
     */
    @Override
    public VQuery setPager(String pager) {
        this.pager = pager;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.V_QUERY.SORTER</code>. 「sorter」-
     * query/sorter:[], 排序选项
     */
    @Override
    public String getSorter() {
        return this.sorter;
    }

    /**
     * Setter for <code>DB_ETERNAL.V_QUERY.SORTER</code>. 「sorter」-
     * query/sorter:[], 排序选项
     */
    @Override
    public VQuery setSorter(String sorter) {
        this.sorter = sorter;
        return this;
    }

    /**
     * Getter for <code>DB_ETERNAL.V_QUERY.CRITERIA</code>. 「criteria」-
     * query/criteria:{}, 查询条件选项
     */
    @Override
    public String getCriteria() {
        return this.criteria;
    }

    /**
     * Setter for <code>DB_ETERNAL.V_QUERY.CRITERIA</code>. 「criteria」-
     * query/criteria:{}, 查询条件选项
     */
    @Override
    public VQuery setCriteria(String criteria) {
        this.criteria = criteria;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final VQuery other = (VQuery) obj;
        if (this.key == null) {
            if (other.key != null)
                return false;
        }
        else if (!this.key.equals(other.key))
            return false;
        if (this.projection == null) {
            if (other.projection != null)
                return false;
        }
        else if (!this.projection.equals(other.projection))
            return false;
        if (this.pager == null) {
            if (other.pager != null)
                return false;
        }
        else if (!this.pager.equals(other.pager))
            return false;
        if (this.sorter == null) {
            if (other.sorter != null)
                return false;
        }
        else if (!this.sorter.equals(other.sorter))
            return false;
        if (this.criteria == null) {
            if (other.criteria != null)
                return false;
        }
        else if (!this.criteria.equals(other.criteria))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
        result = prime * result + ((this.projection == null) ? 0 : this.projection.hashCode());
        result = prime * result + ((this.pager == null) ? 0 : this.pager.hashCode());
        result = prime * result + ((this.sorter == null) ? 0 : this.sorter.hashCode());
        result = prime * result + ((this.criteria == null) ? 0 : this.criteria.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("VQuery (");

        sb.append(key);
        sb.append(", ").append(projection);
        sb.append(", ").append(pager);
        sb.append(", ").append(sorter);
        sb.append(", ").append(criteria);

        sb.append(")");
        return sb.toString();
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    @Override
    public void from(IVQuery from) {
        setKey(from.getKey());
        setProjection(from.getProjection());
        setPager(from.getPager());
        setSorter(from.getSorter());
        setCriteria(from.getCriteria());
    }

    @Override
    public <E extends IVQuery> E into(E into) {
        into.from(this);
        return into;
    }
}
