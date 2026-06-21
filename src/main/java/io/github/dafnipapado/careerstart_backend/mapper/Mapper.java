package io.github.dafnipapado.careerstart_backend.mapper;

import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.model.Employer;
import io.github.dafnipapado.careerstart_backend.model.PersonalInfo;
import io.github.dafnipapado.careerstart_backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public Employer mapToEmployerEntity(EmployerInsertDTO employerInsertDTO) {
        Employer employer = new Employer(null, null, employerInsertDTO.brandName(), employerInsertDTO.vat(), employerInsertDTO.website(), null, null, null, null);
        User user = new User();
        user.setUsername(employerInsertDTO.userInsertDTO().username());
        employer.setUser(user);
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setEmail(employerInsertDTO.personalInfoInsertDTO().email());
        personalInfo.setTelephoneNumber(employerInsertDTO.personalInfoInsertDTO().telephoneNumber());
        personalInfo.setAddress(employerInsertDTO.personalInfoInsertDTO().address());
        employer.setPersonalInfo(personalInfo);
        return employer;
    }

    public EmployerReadOnlyDTO mapToEmployerReadOnlyDTO(Employer employer) {
        return new EmployerReadOnlyDTO(employer.getUuid().toString(), employer.getBrandName(), employer.getUser().getUsername());
    }

}
