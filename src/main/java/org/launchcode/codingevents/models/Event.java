package org.launchcode.codingevents.models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity // Flags class to be persistent
public class Event extends AbstractEntity{

    @NotBlank(message = "Name is required.")  // Prevents the program moving forward without an entry to name
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters.")  // Restricts size of the string
    private String name;

    @Size(max = 500, message = "Description too long!")
    private String description;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid Email. Try Again.") // Tells the program to expect email syntax (Ex. fake@email.com)
    private String contactEmail;

    @ManyToOne
    @NotNull(message = "Category is required.")
    private EventCategory eventCategory;

    public Event() {}
    public Event(String name, String description, String contactEmail, EventCategory eventCategory) {
        this.name = name;
        this.description = description;
        this.contactEmail = contactEmail;
        this.eventCategory = eventCategory;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getContactEmail() { return contactEmail; }

    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public EventCategory getEventCategory() { return eventCategory; }

    public void setEventCategory(EventCategory eventCategory) { this.eventCategory = eventCategory; }

    @Override
    public String toString() {
        return name;
    }
}
