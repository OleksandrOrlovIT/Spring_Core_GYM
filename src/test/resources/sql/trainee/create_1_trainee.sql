INSERT INTO gym_user (firstname, lastname, username, password, isactive)
VALUES ('Test', 'Trainee', 'testtrainee', 'password', true);

SET @trainee_id = (SELECT ID FROM gym_user ORDER BY ID DESC LIMIT 1);

INSERT INTO trainee (id, address, dateofbirth)
VALUES (@trainee_id, '123 Main St', '1990-01-01');