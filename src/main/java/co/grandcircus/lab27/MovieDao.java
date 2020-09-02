package co.grandcircus.lab27;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovieDao extends JpaRepository<Movie, Long> {

	List<Movie> findByTitleContainsIgnoreCase(String title);

	List<Movie> findByCategoryContainsIgnoreCase(String category);

	@Query("select distinct category from Movie order by 1")
	List<String> getDistinctCategory();
}
