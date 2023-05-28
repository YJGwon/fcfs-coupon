package com.coupop.fcfscoupon.domain.history.testconfig;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoDatabaseCleaner {

    private final MongoTemplate mongoTemplate;

    public MongoDatabaseCleaner(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void clean() {
        for (String collectionName : mongoTemplate.getCollectionNames()) {
            mongoTemplate.dropCollection(collectionName);
        }
    }
}
