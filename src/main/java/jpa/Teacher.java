package jpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(name="selectAll",query="SELECT t FROM Teacher t"),
        @NamedQuery(name = "getTeacher", query = "SELECT t FROM Teacher t WHERE t.id = :id")
})
@DiscriminatorValue("TEACHER")
public class Teacher extends User_ {

    public List<Course> getCourses() {
        return (courses == null) ? (courses = new ArrayList<Course>()) : courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @OneToMany(fetch = FetchType.EAGER)
    private List<Course> courses;

    public Teacher(String email, String password, String firstName, String lastName, String address) {
        super(email, password, firstName, lastName, address);
    }

    public Teacher() {}

    @Override
    public String toString() {
        return "Teacher {" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", role=" + getDecriminatorValue() +
                '}';
    }

}
