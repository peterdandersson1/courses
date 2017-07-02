package jpa;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(name = "getAdmins", query="SELECT u FROM Admin u"),
})
@DiscriminatorValue("ADMIN")
public class Admin extends User_ {


    public Admin(String email, String password, String firstName, String lastName, String address) {
        super(email, password, firstName, lastName, address);
    }

    public Admin() {
    }

    @Override
    public String toString() {
        return "Admin {" +
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
