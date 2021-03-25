package simulator.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {
    private List<Builder<T>> _builders;
    private List<JSONObject> _factoryElements;

    public BuilderBasedFactory(List<Builder<T>> builders) {
        _builders = new ArrayList<>(builders);

        List<JSONObject> info = new ArrayList<>();
        for (Builder<T> builder : builders) {
            info.add(builder.getBuilderInfo());
        }
        _factoryElements = Collections.unmodifiableList(info);
    }

    @Override
    public T createInstance(JSONObject info) {
        if (info == null) {
            throw new IllegalArgumentException("Invalid value for createInstance: null");
        }

        for (Builder<T> builder : _builders) {
            T o = builder.createInstance(info);
            if (o != null)
                return o; // if we find a correct builder, we return and never arrive to the exception
        }

        throw new IllegalArgumentException("Invalid value for createInstance: " + info.toString());
    }

    @Override
    public List<JSONObject> getInfo() {
        return _factoryElements;
    }
}
