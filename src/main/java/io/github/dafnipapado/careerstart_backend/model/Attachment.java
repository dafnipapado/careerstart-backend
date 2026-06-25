package io.github.dafnipapado.careerstart_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column
    private String filename;

    @Column(name = "saved_name", unique = true, nullable = false)
    private String savedName;

    @Column(nullable = false)
    private String filepath;

    @Column(name = "content_type")
    private String contentType;

    @Column
    private String extension;

    @ManyToOne
    @JoinColumn(name = "personal_info_id", nullable = false)
    private PersonalInfo personalInfo;

    @PrePersist
    public void uuidInitialize() {
        this.uuid = UUID.randomUUID();
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Attachment that)) return false;
        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
