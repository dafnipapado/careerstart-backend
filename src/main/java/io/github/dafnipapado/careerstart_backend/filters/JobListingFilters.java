package io.github.dafnipapado.careerstart_backend.filters;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JobListingFilters extends PaginationGenericFilters {
    private UUID uuid;
    private String title;
    private Long regionId;
    private Long professionalFieldId;
    private LocalDate createdAt;
    private boolean deleted;
    private UUID employerUuid;
    private String employerBrandName;
}
