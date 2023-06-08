package io.horizon.eon;

/**
 * 路径专用常量
 *
 * @author lang : 2023/4/24
 */
public interface VPath {

    /**
     * 文件扩展名
     */
    interface SUFFIX {

        String ZIP = "zip";

        String YAML = "yaml";

        String YML = "yml";

        String CLASS = "class";

        String JSON = "json";

        String EXCEL_2003 = "xls";

        String EXCEL_2007 = "xlsx";

        String JAR_DIVIDER = "jar!/";

        String BPMN = "bpmn";

        String BPMN_FORM = "form";

        String IMG_PNG = "png";

        String IMG_JPG = "jpg";

        String IMG_JPEG = "jpeg";
    }

    /**
     * 路径中的协议常量
     */
    interface PROTOCOL {

        String FILE = "file";

        String HTTP = "http";

        String HTTPS = "https";

        String JAR = "jar";
    }

    interface SERVER {
        
        String EXPORT = "file-exported";
        String INTERNAL_UP = "up";

        String INTERNAL_RULE = INTERNAL_UP + "/rules/{0}.yml";
        String INTERNAL_FILE = INTERNAL_UP + "/config/";
        String INTERNAL_PACKAGE = INTERNAL_FILE + "vertx-package-filter.json";
        // Aeon系统专用
        String INTERNAL_AEON = "aeon/contained/";
    }
}
