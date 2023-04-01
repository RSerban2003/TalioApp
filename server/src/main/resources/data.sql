DELETE FROM nestedtask;
DELETE FROM task;
DELETE FROM tasklist;
DELETE FROM board;
INSERT INTO board (id, title) VALUES (100, 'board 0');
INSERT INTO board (id, title) VALUES (101, 'board 1');
INSERT INTO board (id, title) VALUES (102, 'board 2');
INSERT INTO board (id, title) VALUES (103, 'board 3');
INSERT INTO board (id, title) VALUES (104, 'board 4');
INSERT INTO Tasklist (id, name, board_id) VALUES (100, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (101, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (102, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (103, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (104, 'aaaa', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (105, 'aaaa', 100);

INSERT INTO Task (id, name, description, tasklist_id, index) VALUES (100, 'thisisatask', '', 100,0);
INSERT INTO Task (id, name, description, tasklist_id, index) VALUES (101, 'thisisataskaswell', '', 100,1);

INSERT INTO NestedTask (id, name, isComplete, task_id, index) VALUES (100, 'nestedTest', FALSE, 100, 0);
INSERT INTO NestedTask (id, name, isComplete, task_id, index) VALUES (101, 'nestedTest', FALSE, 101, 0);