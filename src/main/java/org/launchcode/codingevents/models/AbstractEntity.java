package org.launchcode.codingevents.models;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass       // Ensures that the id values will be stored in the event and event_category tables
public abstract class AbstractEntity {

    @Id                 // Tells SpringBoot that this is a PRIMARY KEY
    @GeneratedValue     // Tells the database to generate the values
    private int id;

    public int getId() { return id; }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity entity = (AbstractEntity) o;
        return id == entity.id;
    }

    public int hashCode() { return Objects.hash(id); }
}
