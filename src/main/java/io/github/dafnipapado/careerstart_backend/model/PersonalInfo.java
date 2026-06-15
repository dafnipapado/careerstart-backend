package io.github.dafnipapado.careerstart_backend.model;

import io.github.dafnipapado.careerstart_backend.model.static_data.Region;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "personal_info")
public class PersonalInfo extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "telephone_number")
    private String telephoneNumber;

    @Column
    private String address;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @OneToMany(mappedBy = "personalInfo")
    private Set<Attachment> attachments = new HashSet<>();

    @PrePersist
    public void uuidInitialize(){
        this.uuid = UUID.randomUUID();
    }

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
        attachment.setPersonalInfo(this);
    }

    public void removeAttachment(Attachment attachment) {
        attachments.remove(attachment);
        attachment.setPersonalInfo(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof PersonalInfo that)) return false;
        return getUuid().equals(that.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
