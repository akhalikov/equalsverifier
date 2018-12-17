package nl.jqno.equalsverifier.integration.extra_features;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import nl.jqno.equalsverifier.testhelpers.ExpectedExceptionTestBase;
import nl.jqno.equalsverifier.testhelpers.annotations.javax.persistence.Entity;
import nl.jqno.equalsverifier.testhelpers.annotations.javax.persistence.Id;
import nl.jqno.equalsverifier.testhelpers.annotations.org.hibernate.annotations.NaturalId;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unused")
public class JpaIdTest extends ExpectedExceptionTestBase {

    @Test
    public void succeed_whenIdFieldIsNotUsed_givenIdIsAnnotatedWithId() {
        EqualsVerifier.forClass(JpaIdBusinessKeyPerson.class)
                .verify();
        EqualsVerifier.forClass(JpaIdBusinessKeyPersonReorderedFields.class)
                .verify();
    }

    @Test
    public void succeed_whenOnlyIdFieldIsUsed_givenIdIsAnnotatedWithIdAndSurrogateKeyWarningIsSuppressed() {
        EqualsVerifier.forClass(JpaIdSurrogateKeyPerson.class)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
        EqualsVerifier.forClass(JpaIdSurrogateKeyPersonReorderedFields.class)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

    @Test
    public void fail_whenOnlyIdFieldIsUsed_givenIdIsAnnotatedWithId() {
        expectFailure("Significant fields", "id is marked @Id", "equals should not use it", "Suppress Warning.SURROGATE_KEY if");
        EqualsVerifier.forClass(JpaIdSurrogateKeyPerson.class)
                .verify();
    }

    @Test
    public void fail_whenOnlyIdFieldIsUsed_givenIdIsAnnotatedWithId2() {
        expectFailure("Significant fields", "equals does not use socialSecurity", "Suppress Warning.SURROGATE_KEY if");
        EqualsVerifier.forClass(JpaIdSurrogateKeyPersonReorderedFields.class)
                .verify();
    }

    @Test
    public void fail_whenIdFieldIsNotUsed_givenIdIsAnnotatedWithIdAndSurrogateKeyWarningIsSuppressed() {
        expectFailure("Significant fields", "id is marked @Id", "Warning.SURROGATE_KEY", "equals does not use");
        EqualsVerifier.forClass(JpaIdBusinessKeyPerson.class)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

    @Test
    public void fail_whenIdFieldIsNotUsed_givenIdIsAnnotatedWithIdAndSurrogateKeyWarningIsSuppressed2() {
        expectFailure("Significant fields", "equals should not use socialSecurity",
                "Warning.SURROGATE_KEY is suppressed and it is not marked as @Id", "but it does");
        EqualsVerifier.forClass(JpaIdBusinessKeyPersonReorderedFields.class)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

    @Test
    public void succeed_whenOnlySocialSecurityIsUsed_givenSocialSecurityIsAnnotatedWithNaturalId() {
        EqualsVerifier.forClass(NaturalIdBusinessKeyPerson.class)
                .verify();
    }

    @Test
    public void succeed_whenOnlySocialSecurityIsUsed_givenSocialSecurityIsAnnotatedWithNaturalIdAndNothingIsAnnotatedWithJpaId() {
        EqualsVerifier.forClass(NaturalIdWithoutJpaIdBusinessKeyPerson.class)
                .verify();
    }

    @Test
    public void fail_whenIdFieldIsTheOnlyFieldUsed() {
        expectFailure("Precondition: you can't use withOnlyTheseFields on a field marked @Id.", "Suppress Warning.SURROGATE_KEY if");
        EqualsVerifier.forClass(JpaIdBusinessKeyPerson.class)
                .withOnlyTheseFields("id")
                .verify();
    }

    @Test
    public void fail_whenOnlySocialSecurityIsUsed_givenSocialSecurityIsAnnotatedWithNaturalIdButSurrogateKeyWarningIsSuppressed() {
        expectFailure("Precondition: you can't suppress Warning.SURROGATE_KEY when fields are marked @NaturalId.");
        EqualsVerifier.forClass(NaturalIdBusinessKeyPerson.class)
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

    @Test
    public void fail_whenWithOnlyTheseFieldsIsUsed_givenWarningSurrogateKeyIsSuppressed() {
        expectFailure("Precondition: you can't use withOnlyTheseFields when Warning.SURROGATE_KEY is suppressed.");
        EqualsVerifier.forClass(JpaIdSurrogateKeyPerson.class)
                .withOnlyTheseFields("socialSecurity")
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

    @Test
    public void fail_whenFieldsAreIgnored_givenWarningSurrogateKeyIsSuppressed() {
        expectFailure("Precondition: you can't use withIgnoredFields when Warning.SURROGATE_KEY is suppressed.");
        EqualsVerifier.forClass(JpaIdSurrogateKeyPerson.class)
                .withIgnoredFields("socialSecurity")
                .suppress(Warning.SURROGATE_KEY)
                .verify();
    }

    @Test
    public void fail_whenWithOnlyTheseFieldsIsUsed_givenFieldsAreMarkedWithNaturalId() {
        expectFailure("Precondition: you can't use withOnlyTheseFields when fields are marked with @NaturalId.");
        EqualsVerifier.forClass(NaturalIdBusinessKeyPerson.class)
                .withOnlyTheseFields("socialSecurity")
                .verify();
    }

    @Test
    public void fail_whenFieldsAreIgnored_givenFieldsAreMarkedWithNaturalId() {
        expectFailure("Precondition: you can't use withIgnoredFields when fields are marked with @NaturalId.");
        EqualsVerifier.forClass(NaturalIdBusinessKeyPerson.class)
                .withIgnoredFields("socialSecurity")
                .verify();
    }

    @Test
    public void succeed_whenIdIsPartOfAProperJpaEntity() {
        EqualsVerifier.forClass(JpaIdBusinessKeyPersonEntity.class)
                .verify();
    }

    @Test
    public void succeed_whenNaturalIdIsPartOfAProperJpaEntity() {
        EqualsVerifier.forClass(NaturalIdBusinessKeyPersonEntity.class)
                .verify();
    }

    @Test
    public void fail_whenAnIdAnnotationFromAnotherPackageIsUsed() {
        expectFailure("Significant fields");
        EqualsVerifier.forClass(NonJpaIdBusinessKeyPerson.class)
                .verify();
    }

    @Test
    public void fail_whenANaturalIdAnnotationFromAnotherPackageIsUsed() {
        expectFailure("Significant fields");
        EqualsVerifier.forClass(NonHibernateNaturalIdBusinessKeyPerson.class)
                .verify();
    }

    static final class JpaIdBusinessKeyPerson {
        @Id
        private final UUID id;
        private final String socialSecurity;
        private final String name;
        private final LocalDate birthdate;

        public JpaIdBusinessKeyPerson(UUID id, String socialSecurity, String name, LocalDate birthdate) {
            this.id = id;
            this.socialSecurity = socialSecurity;
            this.name = name;
            this.birthdate = birthdate;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof JpaIdBusinessKeyPerson)) {
                return false;
            }
            JpaIdBusinessKeyPerson other = (JpaIdBusinessKeyPerson)obj;
            return Objects.equals(socialSecurity, other.socialSecurity) &&
                Objects.equals(name, other.name) &&
                Objects.equals(birthdate, other.birthdate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(socialSecurity, name, birthdate);
        }
    }

    static final class JpaIdBusinessKeyPersonReorderedFields {
        private final String socialSecurity;
        private final String name;
        private final LocalDate birthdate;
        @Id
        private final UUID id;

        public JpaIdBusinessKeyPersonReorderedFields(UUID id, String socialSecurity, String name, LocalDate birthdate) {
            this.id = id;
            this.socialSecurity = socialSecurity;
            this.name = name;
            this.birthdate = birthdate;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof JpaIdBusinessKeyPersonReorderedFields)) {
                return false;
            }
            JpaIdBusinessKeyPersonReorderedFields other = (JpaIdBusinessKeyPersonReorderedFields)obj;
            return Objects.equals(socialSecurity, other.socialSecurity) &&
                Objects.equals(name, other.name) &&
                Objects.equals(birthdate, other.birthdate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(socialSecurity, name, birthdate);
        }
    }

    static final class JpaIdSurrogateKeyPerson {
        @Id
        private final UUID id;
        private final String socialSecurity;
        private final String name;
        private final LocalDate birthdate;

        public JpaIdSurrogateKeyPerson(UUID id, String socialSecurity, String name, LocalDate birthdate) {
            this.id = id;
            this.socialSecurity = socialSecurity;
            this.name = name;
            this.birthdate = birthdate;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof JpaIdSurrogateKeyPerson)) {
                return false;
            }
            JpaIdSurrogateKeyPerson other = (JpaIdSurrogateKeyPerson)obj;
            return Objects.equals(id, other.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    static final class JpaIdSurrogateKeyPersonReorderedFields {
        private final String socialSecurity;
        private final String name;
        private final LocalDate birthdate;
        @Id
        private final UUID id;

        public JpaIdSurrogateKeyPersonReorderedFields(UUID id, String socialSecurity, String name, LocalDate birthdate) {
            this.id = id;
            this.socialSecurity = socialSecurity;
            this.name = name;
            this.birthdate = birthdate;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof JpaIdSurrogateKeyPersonReorderedFields)) {
                return false;
            }
            JpaIdSurrogateKeyPersonReorderedFields other = (JpaIdSurrogateKeyPersonReorderedFields)obj;
            return Objects.equals(id, other.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    static final class NaturalIdBusinessKeyPerson {
        @Id
        private final UUID id;
        @NaturalId
        private final String socialSecurity;
        private final String name;
        private final LocalDate birthdate;

        public NaturalIdBusinessKeyPerson(UUID id, String socialSecurity, String name, LocalDate birthdate) {
            this.id = id;
            this.socialSecurity = socialSecurity;
            this.name = name;
            this.birthdate = birthdate;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof NaturalIdBusinessKeyPerson)) {
                return false;
            }
            NaturalIdBusinessKeyPerson other = (NaturalIdBusinessKeyPerson)obj;
            return Objects.equals(socialSecurity, other.socialSecurity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(socialSecurity);
        }
    }

    static final class NaturalIdWithoutJpaIdBusinessKeyPerson {
        private final UUID id;
        @NaturalId
        private final String socialSecurity;
        private final String name;
        private final LocalDate birthdate;

        public NaturalIdWithoutJpaIdBusinessKeyPerson(UUID id, String socialSecurity, String name, LocalDate birthdate) {
            this.id = id;
            this.socialSecurity = socialSecurity;
            this.name = name;
            this.birthdate = birthdate;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof NaturalIdWithoutJpaIdBusinessKeyPerson)) {
                return false;
            }
            NaturalIdWithoutJpaIdBusinessKeyPerson other = (NaturalIdWithoutJpaIdBusinessKeyPerson)obj;
            return Objects.equals(socialSecurity, other.socialSecurity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(socialSecurity);
        }
    }

    @Entity
    static final class JpaIdBusinessKeyPersonEntity {
        @Id
        private UUID id;
        private String socialSecurity;
        private String name;
        private LocalDate birthdate;

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof JpaIdBusinessKeyPersonEntity)) {
                return false;
            }
            JpaIdBusinessKeyPersonEntity other = (JpaIdBusinessKeyPersonEntity)obj;
            return Objects.equals(socialSecurity, other.socialSecurity) &&
                Objects.equals(name, other.name) &&
                Objects.equals(birthdate, other.birthdate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(socialSecurity, name, birthdate);
        }
    }

    @Entity
    static final class NaturalIdBusinessKeyPersonEntity {
        @Id
        private UUID id;
        @NaturalId
        private String socialSecurity;
        private String name;
        private LocalDate birthdate;

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof NaturalIdBusinessKeyPersonEntity)) {
                return false;
            }
            NaturalIdBusinessKeyPersonEntity other = (NaturalIdBusinessKeyPersonEntity)obj;
            return Objects.equals(socialSecurity, other.socialSecurity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(socialSecurity);
        }
    }

    static final class NonJpaIdBusinessKeyPerson {
        @nl.jqno.equalsverifier.testhelpers.annotations.Id
        private final UUID id;
        private final String socialSecurity;
        private final String name;
        private final LocalDate birthdate;

        public NonJpaIdBusinessKeyPerson(UUID id, String socialSecurity, String name, LocalDate birthdate) {
            this.id = id;
            this.socialSecurity = socialSecurity;
            this.name = name;
            this.birthdate = birthdate;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof NonJpaIdBusinessKeyPerson)) {
                return false;
            }
            NonJpaIdBusinessKeyPerson other = (NonJpaIdBusinessKeyPerson)obj;
            return Objects.equals(socialSecurity, other.socialSecurity) &&
                Objects.equals(name, other.name) &&
                Objects.equals(birthdate, other.birthdate);
        }

        @Override
        public int hashCode() {
            return Objects.hash(socialSecurity, name, birthdate);
        }
    }

    static final class NonHibernateNaturalIdBusinessKeyPerson {
        @Id
        private final UUID id;
        @nl.jqno.equalsverifier.testhelpers.annotations.NaturalId
        private final String socialSecurity;
        private final String name;
        private final LocalDate birthdate;

        public NonHibernateNaturalIdBusinessKeyPerson(UUID id, String socialSecurity, String name, LocalDate birthdate) {
            this.id = id;
            this.socialSecurity = socialSecurity;
            this.name = name;
            this.birthdate = birthdate;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof NonHibernateNaturalIdBusinessKeyPerson)) {
                return false;
            }
            NonHibernateNaturalIdBusinessKeyPerson other = (NonHibernateNaturalIdBusinessKeyPerson)obj;
            return Objects.equals(socialSecurity, other.socialSecurity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(socialSecurity);
        }
    }
}
