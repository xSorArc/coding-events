package org.launchcode.codingevents.data;

import org.launchcode.codingevents.models.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository                           // Class that's going to store Objects in the database
public interface EventRepository extends CrudRepository<Event, Integer>{
                                      // CrudRepository<Objects, PRIMARY KEY type of the objects>
}
