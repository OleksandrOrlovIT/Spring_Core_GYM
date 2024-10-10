INSERT INTO trainingType (trainingtypename)
VALUES
    ('Strength Training'),
    ('Cardio'),
    ('Flexibility'),
    ('Endurance'),
    ('Balance Training')
    ON CONFLICT DO NOTHING;