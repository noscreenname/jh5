package a.m.a.repository;

import a.m.a.domain.Parent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Parent entity.
 */
public interface ParentRepository extends JpaRepository<Parent,Long> {

}
