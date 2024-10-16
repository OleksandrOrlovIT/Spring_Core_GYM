INSERT INTO trainingtype (trainingtypename) VALUES ('testTrainingType1');
SET @training_type_id_1 = (SELECT ID FROM trainingtype ORDER BY ID DESC LIMIT 1);

INSERT INTO gym_user (firstname, lastname, username, password, isactive)
VALUES ('Test', 'Trainer', 'testtrainer', 'password', true);

SET @trainer_id = (SELECT ID FROM gym_user ORDER BY ID DESC LIMIT 1);

INSERT INTO gym_user (firstname, lastname, username, password, isactive)
VALUES ('Test', 'Trainee', 'testtrainee', 'password', true);

SET @trainee_id = (SELECT ID FROM gym_user ORDER BY ID DESC LIMIT 1);

INSERT INTO trainer (id, training_type_id)
VALUES (@trainer_id, @training_type_id_1);

INSERT INTO trainee (id, address, dateofbirth)
VALUES (@trainee_id, '123 Main St', '1990-01-01');

INSERT INTO trainer_trainee (trainer_id, trainee_id)
VALUES (@trainer_id, @trainee_id);

INSERT INTO training (trainingname, trainingdate, trainingduration, trainee_id, trainer_id, training_type_id)
VALUES ('Training 1', '2024-10-01', 60, @trainee_id, @trainer_id, @training_type_id_1),
       ('Training 2', '2024-10-02', 60, @trainee_id, @trainer_id, @training_type_id_1);
