package jpa;


import javax.persistence.*;


//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@NamedQueries({
        @NamedQuery(name = "LogIn", query = "SELECT u FROM User_ u WHERE u.email = :email AND u.password = :password"),
        @NamedQuery(name = "getUser", query="SELECT u FROM User_ u WHERE u.id = :id"),
        @NamedQuery(name = "getUsers", query="SELECT u FROM User_ u"),
        @NamedQuery(name = "countUsers", query="SELECT COUNT(u) FROM User_ u")
})
@DiscriminatorColumn(name = "ROLE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("USER")
public class User_ {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique=true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;


    public User_() {
    }

    public User_(String email, String password, String firstName, String lastName, String address) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    @Transient
    public String getDecriminatorValue() {
        try {
            return this.getClass().getAnnotation(DiscriminatorValue.class).value();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "no";
        }
    }

    public String getRole() {
        return getDecriminatorValue();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", role=" + getDecriminatorValue() +
                '}';
    }
}
