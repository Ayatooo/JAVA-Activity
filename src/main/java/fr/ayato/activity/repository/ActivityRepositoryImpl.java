package fr.ayato.activity.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import fr.ayato.activity.model.ActivityDTO;
import jdk.dynalink.linker.LinkerServices;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Objects;

import static fr.ayato.activity.mapper.ActivityMapper.activityToDocument;
import static fr.ayato.activity.mapper.ActivityMapper.documentToActivity;

public class ActivityRepositoryImpl implements ActivityRepository {
    MongoCollection<Document> collection;

    public ActivityRepositoryImpl(MongoCollection<Document> collection) {
        this.collection = collection;
    }

        @Override
        public String save(ActivityDTO activityDTO) {
            InsertOneResult result = this.collection.insertOne(activityToDocument(activityDTO));
            return Objects.requireNonNull(result.getInsertedId()).toString();
        }

        @Override
        public MongoCollection<Document> getAll() {
            return this.collection;
        }

        @Override
        public ActivityDTO getOne(String id) {
            return documentToActivity(Objects.requireNonNull(this.collection.find(new Document("_id", new ObjectId(id))).first()));
        }

        @Override
        public DeleteResult deleteOne(String id) {
            return this.collection.deleteOne(new Document("_id", new ObjectId(id)));
        }
}
