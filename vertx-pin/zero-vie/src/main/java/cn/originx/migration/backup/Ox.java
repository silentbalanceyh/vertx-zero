package cn.originx.migration.backup;

import java.util.HashSet;
import java.util.Set;

interface Pool {
    /*
     * 结构
     * migration/xxx/
     * - backup
     * - backup/sql            两个整库的基本数据
     * - backup/data/org       组织架构数据
     * - backup/data/user      账号/员工数据
     * - backup/data/system    字典 / 系统配置数据
     * - backup/data/history   历史记录核心数据
     * - report/ddl            DDL的比对结果，可修正 DB_ORIGIN_X 数据库
     * - report/numbers        编号修正报表
     * - report/types          类型报表
     * - report/cis            非法配置项数据备份目录
     * - report/tickets        确认单清洗目录
     */
    Set<String> FOLDERS = new HashSet<String>() {
        {
            this.add("backup");
            this.add("backup/sql");
            this.add("backup/data/org");
            this.add("backup/data/user");
            this.add("backup/data/system");
            this.add("backup/data/history");
            this.add("report/ddl");
            this.add("report/types");
            this.add("report/numbers");
            this.add("report/cis");
            this.add("report/tickets");
        }
    };
}
