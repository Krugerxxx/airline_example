package app.repositories;

import app.entities.search.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchResultRepository  extends JpaRepository<SearchResult, Long> {
}
