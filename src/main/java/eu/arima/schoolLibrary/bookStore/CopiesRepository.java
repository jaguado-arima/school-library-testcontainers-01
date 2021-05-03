package eu.arima.schoolLibrary.bookStore;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CopiesRepository extends CrudRepository<Copy, Long> {

}
