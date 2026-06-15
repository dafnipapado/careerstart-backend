package io.github.dafnipapado.careerstart_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ", nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "TIMESTAMPTZ")
    private Instant updatedAt;

    @Column(nullable = true)
    private boolean deleted;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMPTZ", nullable = true)
    private Instant deletedAt;

    public void softDelete() {
        this.deleted = true;
        this.deletedAt = Instant.now();
    }
}
