package io.github.dafnipapado.careerstart_backend.service;

import io.github.dafnipapado.careerstart_backend.core.exception.EntityNotFoundException;
import io.github.dafnipapado.careerstart_backend.model.PersonalInfo;
import io.github.dafnipapado.careerstart_backend.model.static_data.Region;
import io.github.dafnipapado.careerstart_backend.repository.PersonalInfoRepository;
import io.github.dafnipapado.careerstart_backend.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalInfoServiceImpl implements IPersonalInfoService {

    private final RegionRepository regionRepository;
    private final PersonalInfoRepository personalInfoRepository;

    @Override
    public Region getRegionById(Long regionId) throws EntityNotFoundException {
        return regionRepository.findById(regionId)
                .orElseThrow(() -> new EntityNotFoundException("Region", "Region with id = '" + regionId + "' not found."));
    }

    @Override
    public PersonalInfo getPersonalInfoByEmail(String email) throws EntityNotFoundException {
        return personalInfoRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("PersonalInfo", "Personal Info with email = '" + email + "' not found."));
    }
}
