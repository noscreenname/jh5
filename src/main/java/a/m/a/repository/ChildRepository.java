package a.m.a.repository;

import a.m.a.domain.Child;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Child entity.
 */
public interface ChildRepository extends JpaRepository<Child,Long> {

}
