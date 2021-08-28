package cn.vertxup.ui.service.column;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {
    ConcurrentMap<String, UiValve> VALVE_MAP
        = new ConcurrentHashMap<>();
}
