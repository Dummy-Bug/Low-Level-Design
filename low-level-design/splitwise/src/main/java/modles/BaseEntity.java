package modles;

import jakarta.persistence.*;

import java.time.Instant;

/*
Following should not be package-private.
- JPA proxies
- Lombok getters
- Subclass access
Base entity fields are protected so domain entities can access
identity while preventing misuse by services.

*/
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, updatable = false)
    protected Instant createdAt;

    @Column(nullable = false)
    protected Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    /*
    Hibernate Relies on equals/hashCode for dirty checking
    This avoids catastrophic bugs when comparing transient objects.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // TODO -> explore hashCode and equals for hibernate and ORM as
    // it looks crucial and chatGpt was stressing on them a lot.

    /*
    For JPA entities, hashCode must be stable across the entity lifecycle.
    Since IDs are assigned on persist, using ID in hashCode can corrupt hash-based collections.
    Using getClass().hashCode() ensures stability while equality is based on non-null IDs.
     */

    /*
    - equals answers: are these the same database row?
    -hashCode answers: which bucket do I go to, consistently?
     */
}
