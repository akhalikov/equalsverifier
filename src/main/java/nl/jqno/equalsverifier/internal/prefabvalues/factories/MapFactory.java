/*
 * Copyright 2015-2018 Jan Ouwens
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.jqno.equalsverifier.internal.prefabvalues.factories;

import nl.jqno.equalsverifier.internal.prefabvalues.PrefabValues;
import nl.jqno.equalsverifier.internal.prefabvalues.Tuple;
import nl.jqno.equalsverifier.internal.prefabvalues.TypeTag;

import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Implementation of {@link PrefabValueFactory} that specializes in creating
 * implementations of {@link Map}, taking generics into account.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class MapFactory<T extends Map> extends AbstractReflectiveGenericFactory<T> {
    public abstract T createEmpty();

    @Override
    public Tuple<T> createValues(TypeTag tag, PrefabValues prefabValues, LinkedHashSet<TypeTag> typeStack) {
        LinkedHashSet<TypeTag> clone = cloneWith(typeStack, tag);
        TypeTag keyTag = determineAndCacheActualTypeTag(0, tag, prefabValues, clone);
        TypeTag valueTag = determineAndCacheActualTypeTag(1, tag, prefabValues, clone);

        // Use red for key and black for value in the Red map to avoid having identical keys and values.
        // But don't do it in the Black map, or they may cancel each other out again.

        Object redKey = prefabValues.giveRed(keyTag);
        Object blackKey = prefabValues.giveBlack(keyTag);
        Object blackValue = prefabValues.giveBlack(valueTag);

        T red = createEmpty();
        red.put(redKey, blackValue);

        T black = createEmpty();
        if (!redKey.equals(blackKey)) { // This happens with single-element enums
            black.put(prefabValues.giveBlack(keyTag), blackValue);
        }

        T redCopy = createEmpty();
        redCopy.put(redKey, blackValue);

        return new Tuple<>(red, black, redCopy);
    }
}
