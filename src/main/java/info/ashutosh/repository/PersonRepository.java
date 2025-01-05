package info.ashutosh.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import info.ashutosh.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

	Optional<Person> findByName(String username);

	Optional<Person> findByEmail(String email);

}
