package server.database;

import commons.NestedTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NestedTaskRepository extends JpaRepository<NestedTask, Long> {}