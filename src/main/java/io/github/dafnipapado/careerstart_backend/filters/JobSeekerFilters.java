package io.github.dafnipapado.careerstart_backend.filters;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class JobSeekerFilters extends PaginationGenericFilters{
    private UUID uuid;
    private String firstname;
    private String lastname;
    private String region;
    private boolean deleted;
}
