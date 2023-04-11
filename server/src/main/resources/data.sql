DELETE FROM nestedtask;
DELETE FROM tag;
DELETE FROM task;
DELETE FROM tasklist;
DELETE FROM board;
INSERT INTO board (id, title) VALUES (100, 'Board 1');
INSERT INTO board (id, title) VALUES (101, 'Board 2');
INSERT INTO board (id, title) VALUES (102, 'Board 3');
INSERT INTO board (id, title) VALUES (103, 'Board 4');
INSERT INTO board (id, title) VALUES (104, 'Board 5');
INSERT INTO Tasklist (id, name, board_id) VALUES (100, 'TaskList 1', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (101, 'TaskList 2', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (102, 'TaskList 3', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (103, 'TaskList 4', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (104, 'TaskList 5', 100);
INSERT INTO Tasklist (id, name, board_id) VALUES (105, 'TaskList 6', 100);

INSERT INTO Task (id, name, description, tasklist_id, index) VALUES (100, 'Task 1', '', 100,0);
INSERT INTO Task (id, name, description, tasklist_id, index) VALUES (101, 'Task 2', 'This is a description.', 100,1);

INSERT INTO NestedTask (id, name, isComplete, task_id, index) VALUES (100, 'SubTask 1', FALSE, 100, 0);
INSERT INTO NestedTask (id, name, isComplete, task_id, index) VALUES (101, 'SubTask 2', TRUE, 100, 1);
INSERT INTO NestedTask (id, name, isComplete, task_id, index) VALUES (102, 'Subtask 3', FALSE, 101, 0);

INSERT INTO Tag (id, name, board_id) VALUES (100, 'Important', 100);
INSERT INTO Tag (id, name, board_id) VALUES (101, 'Not Important', 100);