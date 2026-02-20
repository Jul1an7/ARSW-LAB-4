package edu.eci.arsw.blueprints.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.eci.arsw.blueprints.persistence.entity.BlueprintEntity;

import java.util.List;

public interface BlueprintRepository extends JpaRepository<BlueprintEntity, Long> {

    List<BlueprintEntity> findByAuthor(String author);

    BlueprintEntity findByAuthorAndName(String author, String name);
}