package io.github.dafnipapado.careerstart_backend.filters;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public abstract class PaginationGenericFilters {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_SORT_COLUMN = "id";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.ASC;

    private int page;
    private int pageSize;
    private String sortBy;
    private Sort.Direction sortDirection;

    public int getPage() {
        return Math.max(page, 0);
    }

    public int getPageSize() {
        return pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public String getSortBy() {
        return StringUtils.isBlank(sortBy) ? DEFAULT_SORT_COLUMN : sortBy;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection == null ? DEFAULT_SORT_DIRECTION : sortDirection;
    }

    public Sort getSort() {
        return Sort.by(getSortDirection(), getSortBy());
    }

    public Pageable getPageable() {
        return PageRequest.of(getPage(), getPageSize(), getSort());
    }
}
