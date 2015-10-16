package pl.mslawin.notes.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.mslawin.notes.domain.notes.TasksList;

@Repository
public interface ListDao extends CrudRepository<TasksList, Long> {

    TasksList findById(Long id);

    List<TasksList> findByOwner(String email);
}