package siakng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import siakng.model.Course;

@Repository
public interface SiakNgRepository extends JpaRepository<Course, Long>{

}
