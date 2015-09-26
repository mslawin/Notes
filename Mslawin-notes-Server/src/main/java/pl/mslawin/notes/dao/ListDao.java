package pl.mslawin.notes.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.mslawin.notes.domain.notes.TasksList;

@Repository
public interface ListDao extends CrudRepository<TasksList, Long> {

    TasksList findById(Long id);
}