package nl.jqno.equalsverifier.integration.operational;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Func.Func1;
import nl.jqno.equalsverifier.Func.Func2;
import nl.jqno.equalsverifier.testhelpers.types.TypeHelper;
import nl.jqno.equalsverifier.testhelpers.types.TypeHelper.DoubleGenericContainer;
import nl.jqno.equalsverifier.testhelpers.types.TypeHelper.SingleGenericContainer;
import nl.jqno.equalsverifier.testhelpers.types.TypeHelper.SingleGenericContainerContainer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.core.IsInstanceOf.instanceOf;

@SuppressWarnings("unchecked")
public class WithGenericPrefabValuesTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void sanityCheck() {
        EqualsVerifier.forClass(SingleGenericContainer.class)
                .withPrefabValues(SingleGenericContainer.class, new SingleGenericContainer<>(1), new SingleGenericContainer<>(2))
                .verify();

        EqualsVerifier.forClass(DoubleGenericContainer.class)
                .withPrefabValues(DoubleGenericContainer.class, new DoubleGenericContainer<>(1, 1), new DoubleGenericContainer<>(2, 2))
                .verify();
    }

    @Test
    public void throw_whenRegularPrefabValuesOfWrongTypeAreUsed_given1GenericParameter() {
        thrown.expectCause(instanceOf(ClassCastException.class));

        EqualsVerifier.forClass(SingleGenericContainerContainer.class)
                .withPrefabValues(SingleGenericContainer.class, new SingleGenericContainer<>(1), new SingleGenericContainer<>(2))
                .verify();
    }

    @Test
    public void succeed_whenPrefabValuesMatchGenericParameterInClassUnderTest_given1GenericParameter() {
        EqualsVerifier.forClass(SingleGenericContainerContainer.class)
                .withGenericPrefabValues(SingleGenericContainer.class, SingleGenericContainer::new)
                .verify();
    }

    @Test
    public void throw_whenTypeIsNull_given1GenericParameter() {
        thrown.expect(NullPointerException.class);

        EqualsVerifier.forClass(SingleGenericContainerContainer.class)
                .withGenericPrefabValues(null, SingleGenericContainer::new);
    }

    @Test
    public void throw_whenFactoryIsNull_given1GenericParameter() {
        thrown.expect(NullPointerException.class);

        EqualsVerifier.forClass(SingleGenericContainerContainer.class)
                .withGenericPrefabValues(SingleGenericContainer.class, (Func1)null);
    }

    @Test
    public void throw_whenFactoryHas2Parameters_given1GenericParameter() {
        thrown.expect(IllegalArgumentException.class);

        EqualsVerifier.forClass(SingleGenericContainerContainer.class)
                .withGenericPrefabValues(SingleGenericContainer.class, (Func2)(a, b) -> new SingleGenericContainer<>(a));
    }

    @Test
    public void throw_whenRegularPrefabValuesOfWrongTypeAreUsed_given2GenericParameters() {
        thrown.expectCause(instanceOf(ClassCastException.class));

        EqualsVerifier.forClass(TypeHelper.DoubleGenericContainerContainer.class)
                .withPrefabValues(DoubleGenericContainer.class, new DoubleGenericContainer<>(1, 1), new DoubleGenericContainer<>(2, 2))
                .verify();
    }

    @Test
    public void succeed_whenPrefabValuesMatchGenericParametersInClassUnderTest_given2GenericParameters() {
        EqualsVerifier.forClass(TypeHelper.DoubleGenericContainerContainer.class)
                .withGenericPrefabValues(DoubleGenericContainer.class, DoubleGenericContainer::new)
                .verify();
    }

    @Test
    public void throw_whenTypeIsNull_given2GenericParameters() {
        thrown.expect(NullPointerException.class);

        EqualsVerifier.forClass(TypeHelper.DoubleGenericContainerContainer.class)
                .withGenericPrefabValues(null, DoubleGenericContainer::new);
    }

    @Test
    public void throw_whenFactoryIsNull_given2GenericParameters() {
        thrown.expect(NullPointerException.class);

        EqualsVerifier.forClass(TypeHelper.DoubleGenericContainerContainer.class)
                .withGenericPrefabValues(DoubleGenericContainer.class, (Func2)null);
    }

    @Test
    public void throw_whenFactoryHas1Parameter_given2GenericParameters() {
        thrown.expect(IllegalArgumentException.class);

        EqualsVerifier.forClass(TypeHelper.DoubleGenericContainerContainer.class)
                .withGenericPrefabValues(DoubleGenericContainer.class, (Func1)(a -> new DoubleGenericContainer<>(a, a)));
    }
}
