DELETE FROM task WHERE id=100;
DELETE FROM task WHERE id=101;
DELETE FROM tasklist WHERE id=100;
DELETE FROM board WHERE id=100;
INSERT INTO board (id, title) VALUES (100, 'blabla');
INSERT INTO Tasklist (id, name, board_id) VALUES (100, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (101, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (102, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (103, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (104, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (105, 'aaaa', 100);

INSERT INTO Task (id, name, description, tasklist_id) VALUES (100, 'thisisatask', '', 100);
INSERT INTO Task (id, name, description, tasklist_id) VALUES (101, 'thisisataskaswell', '', 100);