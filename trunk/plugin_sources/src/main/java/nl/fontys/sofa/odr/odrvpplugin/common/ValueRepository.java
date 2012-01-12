/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sofa.odr.odrvpplugin.common;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eigo
 */
public final class ValueRepository {

    private Map<String, Object> values;

    private static final class Holder {

        private static final ValueRepository INSTANCE = new ValueRepository();

        private Holder() {
        }
    }

    private ValueRepository() {
        values = new HashMap<String, Object>();
    }

    /**
     * returns an instance of the valueRepository
     * @return instance of valueRepository
     */
    public static ValueRepository getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * sets a new value to the valueRepository
     * @param valueName identifier (should be declared in VPStrings)
     * @param value object to store in repository
     */
    public void setValue(String valueName, Object value) {
        values.put(valueName, value);
    }

    /**
     * gets a value from the repository by identifier
     * @param valueName identifier (mostly in VPStrings)
     * @return requested object
     */
    public Object getValue(String valueName) {
        return values.get(valueName);
    }

    /**
     * removes an object from the valuerepository
     * @param valueName object's identifier to remove object by identifier
     */
    public void removeValue(String valueName) {
        values.remove(valueName);
    }
}
