-- Seed Doctors
INSERT INTO doctors (id, name, specialization) VALUES (1, 'Dr. Sarah Johnson', 'Cardiology');
INSERT INTO doctors (id, name, specialization) VALUES (2, 'Dr. Michael Chen', 'Dermatology');
INSERT INTO doctors (id, name, specialization) VALUES (3, 'Dr. Emily Williams', 'Orthopedics');
INSERT INTO doctors (id, name, specialization) VALUES (4, 'Dr. James Brown', 'General Medicine');

-- Seed Slots for Dr. Sarah Johnson (Cardiology) - Today + next 7 days
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, CURRENT_DATE, '09:00', '09:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, CURRENT_DATE, '09:30', '10:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, CURRENT_DATE, '10:00', '10:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, CURRENT_DATE, '10:30', '11:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, CURRENT_DATE, '11:00', '11:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, CURRENT_DATE, '14:00', '14:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, CURRENT_DATE, '14:30', '15:00', true);

INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, DATEADD('DAY', 1, CURRENT_DATE), '09:00', '09:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, DATEADD('DAY', 1, CURRENT_DATE), '09:30', '10:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, DATEADD('DAY', 1, CURRENT_DATE), '10:00', '10:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, DATEADD('DAY', 1, CURRENT_DATE), '10:30', '11:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, DATEADD('DAY', 1, CURRENT_DATE), '14:00', '14:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (1, DATEADD('DAY', 1, CURRENT_DATE), '14:30', '15:00', true);

-- Seed Slots for Dr. Michael Chen (Dermatology)
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (2, CURRENT_DATE, '10:00', '10:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (2, CURRENT_DATE, '10:30', '11:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (2, CURRENT_DATE, '11:00', '11:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (2, CURRENT_DATE, '11:30', '12:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (2, CURRENT_DATE, '15:00', '15:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (2, CURRENT_DATE, '15:30', '16:00', true);

INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (2, DATEADD('DAY', 1, CURRENT_DATE), '10:00', '10:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (2, DATEADD('DAY', 1, CURRENT_DATE), '10:30', '11:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (2, DATEADD('DAY', 1, CURRENT_DATE), '11:00', '11:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (2, DATEADD('DAY', 1, CURRENT_DATE), '15:00', '15:30', true);

-- Seed Slots for Dr. Emily Williams (Orthopedics)
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (3, CURRENT_DATE, '08:00', '08:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (3, CURRENT_DATE, '08:30', '09:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (3, CURRENT_DATE, '09:00', '09:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (3, CURRENT_DATE, '13:00', '13:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (3, CURRENT_DATE, '13:30', '14:00', true);

INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (3, DATEADD('DAY', 1, CURRENT_DATE), '08:00', '08:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (3, DATEADD('DAY', 1, CURRENT_DATE), '08:30', '09:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (3, DATEADD('DAY', 1, CURRENT_DATE), '09:00', '09:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (3, DATEADD('DAY', 1, CURRENT_DATE), '13:00', '13:30', true);

-- Seed Slots for Dr. James Brown (General Medicine)
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, CURRENT_DATE, '09:00', '09:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, CURRENT_DATE, '09:30', '10:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, CURRENT_DATE, '10:00', '10:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, CURRENT_DATE, '10:30', '11:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, CURRENT_DATE, '11:00', '11:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, CURRENT_DATE, '14:00', '14:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, CURRENT_DATE, '14:30', '15:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, CURRENT_DATE, '15:00', '15:30', true);

INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, DATEADD('DAY', 1, CURRENT_DATE), '09:00', '09:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, DATEADD('DAY', 1, CURRENT_DATE), '09:30', '10:00', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, DATEADD('DAY', 1, CURRENT_DATE), '10:00', '10:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, DATEADD('DAY', 1, CURRENT_DATE), '14:00', '14:30', true);
INSERT INTO slots (doctor_id, date, start_time, end_time, is_available) VALUES (4, DATEADD('DAY', 1, CURRENT_DATE), '14:30', '15:00', true);
