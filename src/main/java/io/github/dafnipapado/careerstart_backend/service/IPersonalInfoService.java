package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.model.PersonalInfo;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;

public interface IPersonalInfoService {
    Region getRegionById(Long regionId) throws EntityNotFoundException;
    PersonalInfo getPersonalInfoByEmail(String email) throws EntityNotFoundException;
}
