package io.github.dafnipapado.careerstart_backend.filters;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;
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
    private String region;
    private String professionalField;
    private LocalDate createdAt;
    private UUID employerUuid;
    private String employerBrandName;
}
