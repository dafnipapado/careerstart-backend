INSERT INTO roles (id, name) VALUES
(1, 'ADMIN'),
(2, 'EMPLOYER'),
(3, 'JOB_SEEKER');

INSERT INTO capabilities (id, name, description) VALUES
(1, 'INSERT_EMPLOYER', 'Create an employer'),
(2, 'UPDATE_EMPLOYER', 'Update an employer'),
(3, 'DELETE_EMPLOYER', 'Delete an employer'),
(4, 'VIEW_EMPLOYER', 'View an employer'),
(5, 'VIEW_EMPLOYER_OWN_DASHBOARD', 'View the personal dashboard of an employer'),
(6, 'VIEW_EMPLOYERS', 'View all employers'),
(7, 'INSERT_JOB_SEEKER', 'Create a job seeker'),
(8, 'UPDATE_JOB_SEEKER', 'Update a job seeker'),
(9, 'DELETE_JOB_SEEKER', 'Delete a job seeker'),
(10, 'VIEW_JOB_SEEKER', 'View a job seeker'),
(11, 'VIEW_JOB_SEEKER_OWN_DASHBOARD', 'View the personal dashboard of a job seeker'),
(12, 'VIEW_JOB_SEEKERS', 'View all job seekers'),
(13, 'INSERT_JOB_LISTING', 'Create a job listing'),
(14, 'UPDATE_JOB_LISTING', 'Update a job listing'),
(15, 'DELETE_JOB_LISTING', 'Delete a job listing'),
(16, 'VIEW_JOB_LISTING', 'View a job listing'),
(17, 'VIEW_JOB_LISTINGS', 'View a list of all job listings'),
(18, 'APPLY_TO_JOB_LISTING', 'Apply to a job listing'),
(19, 'UPLOAD_CV', 'Upload a CV'),
(20, 'UPDATE_CV', 'Update a CV'),
(21, 'DELETE_CV', 'Delete a CV'),
(22, 'UPLOAD_ATTACHMENT', 'Upload an attachment'),
(23, 'UPDATE_ATTACHMENT', 'Update an attachment'),
(24, 'DELETE_ATTACHMENT', 'Delete an attachment');

INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
CROSS JOIN capabilities c
WHERE r.name = 'ADMIN';

INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
CROSS JOIN capabilities c
WHERE r.name = 'EMPLOYER'
  AND c.name IN (
    'INSERT_EMPLOYER',
    'UPDATE_EMPLOYER',
    'DELETE_EMPLOYER',
    'VIEW_EMPLOYER',
    'VIEW_EMPLOYER_OWN_DASHBOARD',
    'VIEW_EMPLOYERS',
    'VIEW_JOB_SEEKER',
    'VIEW_JOB_SEEKERS',
    'INSERT_JOB_LISTING',
    'UPDATE_JOB_LISTING',
    'DELETE_JOB_LISTING',
    'VIEW_JOB_LISTING',
    'VIEW_JOB_LISTINGS',
    'UPLOAD_ATTACHMENT',
    'UPDATE_ATTACHMENT',
    'DELETE_ATTACHMENT'
);

INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
CROSS JOIN capabilities c
WHERE r.name = 'JOB_SEEKER'
  AND c.name IN (
    'INSERT_JOB_SEEKER',
    'UPDATE_JOB_SEEKER',
    'DELETE_JOB_SEEKER',
    'VIEW_JOB_SEEKER',
    'VIEW_JOB_SEEKER_OWN_DASHBOARD',
    'VIEW_JOB_SEEKERS',
    'VIEW_EMPLOYER',
    'VIEW_EMPLOYERS',
    'VIEW_JOB_LISTING',
    'VIEW_JOB_LISTINGS',
    'APPLY_TO_JOB_LISTING',
    'UPLOAD_CV',
    'UPDATE_CV',
    'DELETE_CV',
    'UPLOAD_ATTACHMENT',
    'UPDATE_ATTACHMENT',
    'DELETE_ATTACHMENT'
);

INSERT INTO regions (id, name) VALUES
(1, 'Attica'),
(2, 'Central Greece'),
(3, 'Central Macedonia'),
(4, 'Crete'),
(5, 'Eastern Macedonia and Thrace'),
(6, 'Epirus'),
(7, 'Ionian Islands'),
(8, 'North Aegean'),
(9, 'Peloponnese'),
(10, 'South Aegean'),
(11, 'Thessaly'),
(12, 'Western Greece'),
(13, 'Western Macedonia');

INSERT INTO professional_fields (id, name) VALUES
(1, 'Accounting'),
(2, 'Agriculture'),
(3, 'Arts/Designing'),
(4, 'Business Administration'),
(5, 'Construction'),
(6, 'Consulting'),
(7, 'Customer Support'),
(8, 'Education'),
(9, 'Engineering'),
(10, 'Finance'),
(11, 'Hospitality'),
(12, 'Human Resources'),
(13, 'IT'),
(14, 'Legal'),
(15, 'Logistics/Warehouse'),
(16, 'Manufacturing/Production'),
(17, 'Marketing/Advertising'),
(18, 'Medicine/Healthcare'),
(19, 'Office & Administrative Support'),
(20, 'Real Estate'),
(21, 'Retail'),
(22, 'Science/Research'),
(23, 'Tourism')
