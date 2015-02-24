package ie.ucc.cs1.fyp.Model.Managers;

/**
 * Created by kpmmmurphy on 10/01/15.
 */
public class SensorManager {
    protected int collection_priority;
    protected int collection_rate;

    public int getCollection_rate() {
        return collection_rate;
    }

    public void setCollection_rate(int collection_rate) {
        this.collection_rate = collection_rate;
    }

    public int getCollection_priority() {
        return collection_priority;
    }

    public void setCollection_priority(int collection_priority) {
        this.collection_priority = collection_priority;
    }
}
