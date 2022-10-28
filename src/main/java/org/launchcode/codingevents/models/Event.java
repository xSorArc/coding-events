package org.launchcode.codingevents.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity                     // Flags class to be persistent
public class Event {

    @Id                     // Tells SpringBoot that this is a PRIMARY KEY
    @GeneratedValue         // Tells the database to generate the values
    private int id;

    @NotBlank(message = "Name is required.")  // Prevents the program moving forward without an entry to name
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters.")  // Restricts size of the string
    private String name;

    @Size(max = 500, message = "Description too long!")
    private String description;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid Email. Try Again.") // Tells the program to expect email syntax (Ex. fake@email.com)
    private String contactEmail;

//    @NotNull              // BONUS
//    @NotBlank(message = "Location cannot be left blank.")
//    private String location;
//
//    @AssertTrue(message = "Registration is required.")
//    private boolean registration = true;
//
//    @Positive(message = "At least one person in attendance is required.")
//    private int attendanceNum;

    private EventType type;

    public Event() {}
    public Event(String name, String description, String contactEmail, EventType type) {
        this.name = name;
        this.description = description;
        this.contactEmail = contactEmail;
        this.type = type;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public int getId() { return id; }

    public String getContactEmail() { return contactEmail; }

    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

//    public String getLocation() { return location; }                              // BONUS
//
//    public void setLocation(String location) { this.location = location; }
//
//    public boolean getRegistration() { return registration; }
//
//    public void setRegistration(boolean registration) { this.registration = registration; }
//
//    public int getAttendanceNum() { return attendanceNum; }
//
//    public void setAttendanceNum(int attendanceNum) { this.attendanceNum = attendanceNum; }

    public EventType getType() { return type; }

    public void setType(EventType type) { this.type = type; }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
