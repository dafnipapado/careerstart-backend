package io.github.dafnipapado.careerstart_backend.model.static_data;

import io.github.dafnipapado.careerstart_backend.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Setter(AccessLevel.NONE)
    @ManyToMany(mappedBy = "roles")
    private Set<Capability> capabilities = new HashSet<>();

    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();

    public void addUser(User user){
        users.add(user);
        user.setRole(this);
    }

    public void removeUser(User user){
        users.remove(user);
        user.setRole(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Role role)) return false;
        return getName().equals(role.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
