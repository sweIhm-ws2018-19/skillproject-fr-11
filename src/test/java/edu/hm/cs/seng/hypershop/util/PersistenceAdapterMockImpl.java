package edu.hm.cs.seng.hypershop.util;

import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.exception.PersistenceException;
import com.amazon.ask.model.RequestEnvelope;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PersistenceAdapterMockImpl implements PersistenceAdapter {

    private Map<String, Object> map = new HashMap<>();

    @Override
    public Optional<Map<String, Object>> getAttributes(RequestEnvelope requestEnvelope) throws PersistenceException {
        return Optional.of(map);
    }

    @Override
    public void saveAttributes(RequestEnvelope requestEnvelope, Map<String, Object> map) throws PersistenceException {
        this.map = map;
    }
}
