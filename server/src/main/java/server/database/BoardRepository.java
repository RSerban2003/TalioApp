package server.database;

import commons.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Quote, Long> {}
