package repositories;

import models.Gate;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/*
CRUD operations
 */
public class GateRepository implements IGateRepository {

    private Map<Long, Gate> gates = new TreeMap<>();
    public Optional<Gate> findGateById(Long gateId) {

        if (gates.containsKey(gateId)) {
            return Optional.of(gates.get(gateId));
        }
        return Optional.empty();
    }
}
