# Jooq API

## 1. 标准部分

### 1.1. 写

#### 1.1.1. 添加

* 单增加：`insert(T) / insertAsync(T)`
* 批量增加：`insert(List<T>) / insertAsync(List<T>)`

#### 1.1.2. 更新

* 单更新：`update(T) / updateAsync(T)`
* 批量更新：`update(List<T>) / updateAsync(List<T>)`
* ID更新：`update(id, T) / updateAsync(id, T)`

#### 1.1.3. 删除

* ID删除：`deleteById(ID) / deleteByIdAsync(ID)`
* ID集合删除：`deleteById(ID...) / deleteByIdAsync(ID...)`
* 单删除：`delete(T) / deleteAsync(T)`
* 【集合】条件删除：`delete(JsonObject) / deleteAsync(JsonObject)`
* 【集合】POJO模式：`delete(JsonObject,String) / deleteAsync(JsonObject, String)`

#### 1.1.4. 合并（添加/更新）

* 单ID合并：`upsert(String,T) / upsertAsync(String, T)`
* 条件合并：`upsert(JsonObject,T) / upsertAsync(JsonObject,T)`
* 【集合】合并：`upsert(JsonObject,List<T>,BiPredicate) / upsertAsync(JsonObject, List<T>, BiPredicate)`

### 1.2. 读

#### 1.2.1. 基本集合

* 读取所有：`fetchAll() / fetchAllAsync()`
* 条件读取：`fetch(JsonObject) / fetchAsync(JsonObject)`
* 【变化】单维度读取：`fetch(String,Object) / fetchAsync(String,Object)`
* 【变化】AND条件读取：`fetchAnd(JsonObject) / fetchAndAsync(JsonObject)`
* 【变化】Or条件读取：`fetchOr(JsonObject) / fetchOrAsync(JsonObject)`
* 【变化】单维度集合读取：`fetchIn(String,JsonArray) / fetchInAsync(String, JsonArray)`

#### 1.2.2. 读取单条

* ID读取：`fetchById(Object) / fetchByIdAsync(Object)`
* 单字段读取：`fetchOne(String, Object) / fetchOneAsync(String, Object)`
* 条件读取：`fetchOne(JsonObject) / fetchOneAsync(JsonObject)`

#### 1.2.3. 检查

* ID检查：`existById(Object) / existByIdAsync(Object)`
* 条件检查：`exist(JsonObject) / existAsync(JsonObject)`

#### 1.2.4. 搜索

* 快速搜索POJO模式：`search(JsonObject,String) / searchAsync(JsonObject,String)`
* 快速搜索：`search(JsonObject) / searchAsync(JsonObject)`

### 1.3. 聚集

#### 1.3.1. 计数

* 条件计数：`count(JsonObject) / countAsync(JsonObject)`
* 条件计数POJO模式：`count(JsonObject, String) / countAsync(JsonObject, String)`
* 条件维度计数：`countBy(JsonObject,String) / countByAsync(JsonObject, String)`

#### 1.3.2. 分组

* 条件分组：`groupAsync(JsonObject, ...String)`
* 条件分组POJO模式：`groupAsync(String, JsonObject, ...String)`

## 2. Ox Engine部分

### 2.1. 写

#### 2.1.1. 添加

* 单增：`insert(Record) / insertAsync(Record)`
* 批量增加：`insert(Record...) / insertAsync(Record...)`

#### 2.1.2. 更新

* 单更新：`update(Record) / updateAsync(Record)`
* 批量更新：`update(Record...) / updateAsync(Record...)`

#### 2.1.3. 删除

* 单删除：`delete(Record) / deleteAsync(Record)`
* 批量删除：`delete(Record...) / deleteAsync(Record...)`

### 2.2. 读

#### 2.2.1. 基本集合

* 条件读取：`fetch(JsonObject) / fetchAsync(JsonObject)`
* 读取所有：`fetchAll() / fetchAllAsync()`

#### 2.2.2. 读取单条

* ID读取：`fetchById(ID) / fetchByIdAsync(ID)`
* ID集合读取：`fetchByIds(ID...) / fetchByIdsAsync(ID...)`
* 条件读取：`fetchOne(Criteria) / fetchOneAsync(Criteria)`

#### 2.2.3. 检查

* 存在检查：`exist(Criteria) / existAsync(Criteria)`
* 丢失检查：`miss(Criteria) / missAsync(Criteria)`

#### 2.2.4. 搜索

* 标准模式：`search(JsonObject) / searchAsync(JsonObject)`