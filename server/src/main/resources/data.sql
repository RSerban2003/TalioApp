DELETE FROM task;
DELETE FROM tasklist;
DELETE FROM board;
INSERT INTO board (id, title) VALUES (100, 'blabla');
INSERT INTO Tasklist (id, name, board_id) VALUES (100, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (101, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (102, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (103, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (104, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (105, 'aaaa', 100);

INSERT INTO Task (id, name, description, tasklist_id,0) VALUES (100, 'thisisatask', '', 100);
INSERT INTO Task (id, name, description, tasklist_id,1) VALUES (101, 'thisisataskaswell', '', 100);