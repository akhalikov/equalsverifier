package nl.jqno.equalsverifier.internal.prefabvalues.factories;

import nl.jqno.equalsverifier.internal.prefabvalues.JavaApiPrefabValues;
import nl.jqno.equalsverifier.internal.prefabvalues.PrefabValues;
import nl.jqno.equalsverifier.internal.prefabvalues.Tuple;
import nl.jqno.equalsverifier.internal.prefabvalues.TypeTag;
import nl.jqno.equalsverifier.testhelpers.types.Point;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("rawtypes")
public class ReflectiveCollectionCopyFactoryTest {
    private PrefabValues prefabValues;
    private final LinkedHashSet<TypeTag> typeStack = new LinkedHashSet<>();

    @Before
    public void setUp() {
        prefabValues = new PrefabValues();
        JavaApiPrefabValues.addTo(prefabValues);
    }

    @Test
    public void createInstancesWithCorrectSingleGenericParameter() {
        TypeTag tag = new TypeTag(GenericContainer.class, new TypeTag(String.class));
        TypeTag listTag = new TypeTag(List.class, new TypeTag(String.class));

        PrefabValueFactory<GenericContainer> factory = new ReflectiveCollectionCopyFactory<>(
                GenericContainer.class.getName(), List.class, List.class, GenericContainerFactory.class.getName(), "createGenericContainer");
        Tuple<GenericContainer> tuple = factory.createValues(tag, prefabValues, typeStack);

        assertEquals(prefabValues.giveRed(listTag), tuple.getRed().t);
        assertEquals(prefabValues.giveBlack(listTag), tuple.getBlack().t);
        assertEquals(String.class, tuple.getRed().t.get(0).getClass());
    }

    @Test
    public void createInstancesWithCorrectMultipleGenericParameter() {
        TypeTag tag = new TypeTag(GenericMultiContainer.class, new TypeTag(String.class), new TypeTag(Point.class));
        TypeTag mapTag = new TypeTag(Map.class, new TypeTag(String.class), new TypeTag(Point.class));

        PrefabValueFactory<GenericMultiContainer> factory = new ReflectiveCollectionCopyFactory<>(
                GenericMultiContainer.class.getName(), Map.class, Map.class, GenericContainerFactory.class.getName(), "createGenericMultiContainer");
        Tuple<GenericMultiContainer> tuple = factory.createValues(tag, prefabValues, typeStack);

        assertEquals(prefabValues.giveRed(mapTag), tuple.getRed().t);
        assertEquals(prefabValues.giveBlack(mapTag), tuple.getBlack().t);

        Map.Entry next = (Map.Entry)tuple.getRed().t.entrySet().iterator().next();
        assertEquals(String.class, next.getKey().getClass());
        assertEquals(Point.class, next.getValue().getClass());
    }

    private static final class GenericContainer<T> {
        private final List<T> t;

        public GenericContainer(List<T> t) {
            this.t = t;
        }
    }

    private static final class GenericMultiContainer<K, V> {
        private final Map<K, V> t;

        public GenericMultiContainer(Map<K, V> t) {
            this.t = t;
        }
    }

    private static final class GenericContainerFactory {
        @SuppressWarnings("unused")
        public static <T> GenericContainer<T> createGenericContainer(List<T> t) {
            return new GenericContainer<>(t);
        }

        @SuppressWarnings("unused")
        public static <K, V> GenericMultiContainer<K, V> createGenericMultiContainer(Map<K, V> t) {
            return new GenericMultiContainer<>(t);
        }
    }
}
