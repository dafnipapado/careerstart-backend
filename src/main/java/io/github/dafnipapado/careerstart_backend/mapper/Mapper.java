package io.github.dafnipapado.careerstart_backend.mapper;

import io.github.dafnipapado.careerstart_backend.dto.attachment.AttachmentUploadDTO;
import io.github.dafnipapado.careerstart_backend.dto.cv.CvInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.cv.CvReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerDetailsReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.employer.EmployerSummaryReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingDetailsReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_listing.JobListingSummaryReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerDetailsReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerInsertDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.job_seeker.JobSeekerSummaryReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.dto.personalInfo.PersonalInfoDetailsReadOnlyDTO;
import io.github.dafnipapado.careerstart_backend.model.*;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public Employer mapToEmployerEntity(EmployerInsertDTO employerInsertDTO) {
        Employer employer = new Employer(null, null, employerInsertDTO.brandName(), employerInsertDTO.vat(), employerInsertDTO.website(), employerInsertDTO.profile(), null, null, null, null);
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

    public EmployerSummaryReadOnlyDTO mapToEmployerSummaryReadOnlyDTO(Employer employer) {
        return new EmployerSummaryReadOnlyDTO(
                employer.getUuid().toString(),
                employer.getBrandName(),
                employer.getWebsite(),
                employer.getProfessionalField().getName(),
                employer.getPersonalInfo().getRegion().getName()
        );
    }

    public EmployerDetailsReadOnlyDTO mapToEmployerDetailsReadOnlyDTO(Employer employer) {
        return new EmployerDetailsReadOnlyDTO(
                employer.getUuid().toString(),
                employer.getBrandName(),
                employer.getVat(),
                employer.getWebsite(),
                employer.getProfile(),
                employer.getProfessionalField().getName(),
                employer.getProfessionalField().getId(),
                new PersonalInfoDetailsReadOnlyDTO(
                    employer.getPersonalInfo().getEmail(),
                    employer.getPersonalInfo().getTelephoneNumber(),
                    employer.getPersonalInfo().getAddress(),
                    employer.getPersonalInfo().getRegion().getName(),
                    employer.getPersonalInfo().getRegion().getId()
                ),
                employer.isDeleted()
        );
    }

    public Attachment mapToAttachmentEntity(AttachmentUploadDTO attachmentUploadDTO) {
        return new Attachment(
            null,
            attachmentUploadDTO.uuid(),
            attachmentUploadDTO.filename(),
            attachmentUploadDTO.savedName(),
            attachmentUploadDTO.filepath(),
            attachmentUploadDTO.contentType(),
            attachmentUploadDTO.extension(),
            null
        );
    }

    public JobSeeker mapToJobSeekerEntity(JobSeekerInsertDTO jobSeekerInsertDTO) {
        JobSeeker jobSeeker = new JobSeeker(null, null, jobSeekerInsertDTO.firstname(), jobSeekerInsertDTO.lastname(), null, null, null, null);
        User user = new User();
        user.setUsername(jobSeekerInsertDTO.userInsertDTO().username());
        jobSeeker.setUser(user);
        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setEmail(jobSeekerInsertDTO.personalInfoInsertDTO().email());
        personalInfo.setTelephoneNumber(jobSeekerInsertDTO.personalInfoInsertDTO().telephoneNumber());
        personalInfo.setAddress(jobSeekerInsertDTO.personalInfoInsertDTO().address());
        jobSeeker.setPersonalInfo(personalInfo);
        return jobSeeker;
    }

    public JobSeekerReadOnlyDTO mapToJobSeekerReadOnlyDTO(JobSeeker jobSeeker) {
        return new JobSeekerReadOnlyDTO(jobSeeker.getUuid().toString(), jobSeeker.getFirstname(), jobSeeker.getLastname());
    }

    public JobSeekerSummaryReadOnlyDTO mapToJobSeekerSummaryReadOnlyDTO(JobSeeker jobSeeker) {
        return new JobSeekerSummaryReadOnlyDTO(jobSeeker.getUuid().toString(), jobSeeker.getFirstname(), jobSeeker.getLastname(), jobSeeker.getPersonalInfo().getEmail());
    }

    public JobSeekerDetailsReadOnlyDTO mapToJobSeekerDetailsReadOnlyDTO(JobSeeker jobSeeker) {
        return new JobSeekerDetailsReadOnlyDTO(
                jobSeeker.getUuid().toString(),
                jobSeeker.getFirstname(),
                jobSeeker.getLastname(),
                new PersonalInfoDetailsReadOnlyDTO(
                        jobSeeker.getPersonalInfo().getEmail(),
                        jobSeeker.getPersonalInfo().getTelephoneNumber(),
                        jobSeeker.getPersonalInfo().getAddress(),
                        jobSeeker.getPersonalInfo().getRegion().getName(),
                        jobSeeker.getPersonalInfo().getRegion().getId()
                )
        );
    }

    public JobListing mapToJobListingEntity(JobListingInsertDTO jobListingInsertDTO) {
         return new JobListing(null, null, jobListingInsertDTO.title(), jobListingInsertDTO.description(), null, null, null, null);
    }

    public JobListingReadOnlyDTO mapToJobListingReadOnlyDTO(JobListing jobListing) {
        return new JobListingReadOnlyDTO(jobListing.getUuid().toString(), jobListing.getTitle(), jobListing.getEmployer().getBrandName());
    }

    public JobListingSummaryReadOnlyDTO mapToJobListingSummaryReadOnlyDTO(JobListing jobListing) {
        return new JobListingSummaryReadOnlyDTO(
                jobListing.getUuid().toString(),
                jobListing.getTitle(),
                jobListing.getRegion().getName(),
                jobListing.getProfessionalField().getName(),
                jobListing.getCreatedAt().toString(),
                jobListing.getEmployer().getUuid().toString(),
                jobListing.getEmployer().getBrandName()
        );
    }

    public JobListingDetailsReadOnlyDTO mapToJobListingDetailsReadOnlyDTO(JobListing jobListing) {
        Employer employer = jobListing.getEmployer();
        return new JobListingDetailsReadOnlyDTO(
            jobListing.getUuid().toString(),
            jobListing.getTitle(),
            jobListing.getDescription(),
            jobListing.getProfessionalField().getId(),
            jobListing.getProfessionalField().getName(),
            jobListing.getRegion().getId(),
            jobListing.getRegion().getName(),
            jobListing.getCreatedAt().toString(),
            mapToEmployerSummaryReadOnlyDTO(employer)
        );
    }

    public JobSeekerCv mapToJobSeekerCvEntity(CvInsertDTO cvInsertDTO) {
        return new JobSeekerCv(
            null,
            null,
            cvInsertDTO.profession(),
            cvInsertDTO.bio(),
            cvInsertDTO.education(),
            cvInsertDTO.experience(),
            cvInsertDTO.certificates(),
            cvInsertDTO.languages(),
            cvInsertDTO.skills(),
            null
        );
    }

    public CvReadOnlyDTO mapToCvReadOnlyDTO(JobSeekerCv jobSeekerCv) {
        return new CvReadOnlyDTO(
                jobSeekerCv.getUuid().toString(),
                jobSeekerCv.getProfession(),
                jobSeekerCv.getBio(),
                jobSeekerCv.getEducation(),
                jobSeekerCv.getExperience(),
                jobSeekerCv.getCertificates(),
                jobSeekerCv.getLanguages(),
                jobSeekerCv.getSkills()
        );
    }

}
