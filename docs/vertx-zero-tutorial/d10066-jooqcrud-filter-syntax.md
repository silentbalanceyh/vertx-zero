# Jooq/CRUD, Filter Syntax

In previous tutorial, you could see that there are two apis in `Ux.Jooq` as following:

* `<T> Future<List<T>> fetchAndAsync(JsonObject andFilters)`
* `<T> Future<List<T>> fetchOrAsync(JsonObject orFilters)`
* `<T> Future<T> fetchOneAndAsync(JsonObject andFilters)`

Above three apis used `andFilters` and `orFilters`, actually these filters support different condition that mapped to
SQL database.

Demo projects:

* **Standalone - 6093** : `up-thea`

## 1. Basic Syntax

When you write filter with following java code:

```java
        final String type = Ux.getString(envelop);
        final String code = Ux.getString1(envelop);
        final JsonObject filters = new JsonObject();
        filters.put("S_TYPE", type).put("S_CODE", code);
```

Here are the basic syntax, you can put `column` name and `value` into JsonObject and above filters will generate
following SQL:

```sql
S_TYPE = ? AND S_CODE = ?
```

Here the connector is `AND`, because we called `fetchAndAsync` api, if you use the same filter to call `fetchOrAsync`
api, the generated SQL will be:

```sql
S_TYPE = ? OR S_CODE = ?
```

## 2. Advanced Syntax

Our filter `column` support suffix syntax to do different query, the basic column syntax is:

```shell
NAME,OP
```

It means that you can use following syntax:

```java
JsonObject filter1 = new JsonObject().put("S_TYPE,=", type);
JsonObject filter2 = new JsonObject().put("S_TYPE", type);
```

Above two statement is equal, here `S_TYPE,=` is the syntax of zero system filter.

### 2.1. equal, NAME,=

The first syntax is `=` operator in SQL, because it's frequently used and you can ignore `,=` in this statement.

```java
JsonObject filter1 = new JsonObject().put("S_TYPE,=", type);
JsonObject filter2 = new JsonObject().put("S_TYPE", type);
```

To

```java
S_TYPE = ?
```

### 2.2. not equal, NAME,&lt;&gt;

```java
JsonObject filter = new JsonObject().put("S_TYPE,<>", type);
```

To

```sql
S_TYPE <> ?
```

### 2.3. less than, NAME,&lt;

```java
JsonObject filter = new JsonObject().put("S_TYPE,<", type);
```

To

```sql
S_TYPE < ?
```

### 2.4. less than and equal, NAME, &lt;=

```java
JsonObject filter = new JsonObject().put("S_TYPE,<=", type);
```

To

```sql
S_TYPE <= ?
```

### 2.5. greater than, NAME, &gt;

```java
JsonObject filter = new JsonObject().put("S_TYPE,>", type);
```

To

```sql
S_TYPE > ?
```

### 2.6. greater than and equal, NAME, &gt;=

```java
JsonObject filter = new JsonObject().put("S_TYPE,>=", type);
```

To

```sql
S_TYPE >= ?
```

### 2.7. not null, NAME,!n

```java
JsonObject filter = new JsonObject().put("S_TYPE,!n", type);
```

To

```sql
S_TYPE IS NOT NULL
```

### 2.8, null, NAME,n

```java
JsonObject filter = new JsonObject().put("S_TYPE,n", type);
```

To

```sql
S_TYPE IS NULL
```

### 2.9, true, NAME,t

```java
JsonObject filter = new JsonObject().put("S_TYPE,t", type);
```

To

```sql
S_TYPE = TRUE
```

### 2.10, false, NAME,f

```java
JsonObject filter = new JsonObject().put("S_TYPE,f", type);
```

To

```sql
S_TYPE = FALSE
```

### 2.11, in, NAME,i

```java
JsonArray type = new JsonArray().add("type1");    // IN should use JsonArray as parameters
JsonObject filter = new JsonObject().put("S_TYPE,i", type);
```

To

```sql
S_TYPE IN (?,?)
```

### 2.12, not in, NAME,!i

```java
JsonArray type = new JsonArray().add("type1");    // IN should use JsonArray as parameters
JsonObject filter = new JsonObject().put("S_TYPE,!i", type);
```

To

```sql
S_TYPE NOT IN (?,?)
```

### 2.13, start with, NAME,s

```java
JsonObject filter = new JsonObject().put("S_TYPE,s", type);
```

To

```sql
S_TYPE LIKE 'type%'
```

### 2.14, end with, NAME, e

```java
JsonObject filter = new JsonObject().put("S_TYPE,e", type);
```

To

```sql
S_TYPE LIKE '%type'
```

### 2.15, contain, NAME,c

```java
JsonObject filter = new JsonObject().put("S_TYPE,c", type);
```

To

```sql
S_TYPE LIKE '%type%'
```

## 3. Summary

For above filters, now it's used into `andFilters` and `orFilters` only, in future plan we'll put into advanced usage.
It's common usage and you may meet different situations in your real project, but if you use the filter syntax, you can
consider the code logical only and do not think how to write the SQL statement. It's also why we recommend to use Jooq
instead of other client here.

