package nl.jqno.equalsverifier.internal.util;

import nl.jqno.equalsverifier.internal.reflection.FieldIterable;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final class FieldNameExtractor {

    private FieldNameExtractor() {}

    static <T> Set<String> extractFieldNames(Class<T> type) {
        Set<String> actualFieldNames = new HashSet<>();
        for (Field f : FieldIterable.of(type)) {
            String name = f.getName();
            actualFieldNames.add(name);
        }

        return Collections.unmodifiableSet(actualFieldNames);
    }

}
