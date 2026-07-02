package io.github.dafnipapado.careerstart_backend.filters;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class EmployerFilters extends PaginationGenericFilters{
    private UUID uuid;
    private String vat;
    private String brandName;
    private String professionalField;
    private String region;
    private boolean deleted;
}
