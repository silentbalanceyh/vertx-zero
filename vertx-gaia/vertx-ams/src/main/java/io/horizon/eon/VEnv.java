package io.horizon.eon;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2023/4/24
 */
public interface VEnv {

    interface APP {
        // Modeler Namespace of DEFAULT
        String NS = "cn.originx.{0}";
    }

    interface PROP {
        String OS_NAME = "os.name";
    }

    interface SPEC {
        // Java语言规范
        ConcurrentMap<Class<?>, Class<?>> TYPES = new ConcurrentHashMap<Class<?>, Class<?>>() {
            {
                this.put(Integer.class, int.class);
                this.put(Long.class, long.class);
                this.put(Short.class, short.class);
                this.put(Boolean.class, boolean.class);
                this.put(Character.class, char.class);
                this.put(Double.class, double.class);
                this.put(Float.class, float.class);
                this.put(Byte.class, byte.class);
            }
        };
    }
}
