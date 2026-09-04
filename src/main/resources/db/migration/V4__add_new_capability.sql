INSERT INTO capabilities (id, name, description) VALUES
(25, 'WITHDRAW_FROM_JOB_LISTING', 'Withdraw from a job listing');

INSERT INTO roles_capabilities (role_id, capability_id)
SELECT r.id, c.id
FROM roles r
CROSS JOIN capabilities c
WHERE r.name IN (
    'ADMIN',
    'JOB_SEEKER')
  AND c.name = 'WITHDRAW_FROM_JOB_LISTING';