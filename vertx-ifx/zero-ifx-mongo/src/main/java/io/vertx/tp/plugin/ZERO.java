package io.vertx.tp.plugin;

interface Info {

    String MONGO_FILTER = "( Mongo -> findOne ) collection = {0}, filter = {1}, result = {2}.";
    String MONGO_INSERT = "( Mongo -> insert ) collection = {0}, data = {1}.";
    String MONGO_UPDATE = "( Mongo -> findOneAndReplace ) collection = {0}, filter = {1}, result = {2}.";
    String MONGO_DELETE = "( Mongo -> removeDocument ) Effected: {2} Rows. collection = {0}, filter = {1}.";
    String MONGO_FIND = "( Mongo -> findWithOptions ) collection = {0}, filter = {1}, options = {2}, result = {3}.";
    String MERGE_INFO = "Merged {0} and {1}, sourceKey = {2}, targetKey = {3}";

    String FILTER_INFO = "Mongo collection = {0}, filter: {1}";

    String UPDATE_INFO = "Mongo collection = {0}, filter: {1}, data: {2}";

    String UPDATE_QUERY = "Mongo collection = {0}, query by: {1}, data: {2}";

    String UPDATE_FLOW = "Mongo update flow = {0}, filter: {1}, latest: {2}";

    String UPDATE_RESULT = "Mongo update result = {0}.";
}
