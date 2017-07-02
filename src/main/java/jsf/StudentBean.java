package jsf;

import ejb.AdminService;
import ejb.StudentService;
import ejb.UserService;
import jpa.Student;
import jpa.User_;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import java.util.List;
import java.util.Map;

@ManagedBean
@RequestScoped
public class StudentBean {

    //@EJB
    //UserService userService;

    @EJB
    StudentService studentService;

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String role;

    // private User_ user;
    private UIComponent row;

    public UIComponent getRow() {
        return row;
    }

    public void setRow(UIComponent row) {
        this.row = row;
    }

    public String createStudent() {
        Student s = new Student(email, password, firstName, lastName, address);
        s.setId(id);
        studentService.saveStudent(s);
        /*try {
            if (getId()==null) {
                studentService.createStudent(email, password);
            }
            else {
                updateStudent(getId());
            }
        } catch (EJBException e) {
            // Do nothing
            // Pop-up message - email already exists.
        }*/
        setEmail("");
        setPassword("");
        setFirstName("");
        setLastName("");
        setAddress("");
        setId(null);
        return "admin/students";
    }

    public String removeUser(Long id) {
        studentService.removeStudent(id);
        return "admin/students";
    }


    public String updateStudent(Long id) {
        studentService.updateStudent(getId(), getEmail(), getPassword());
        return "admin/students";
    }

    public String editStudent(Long id) {
        Student student = studentService.getStudent(id);
        setId(id);
        setEmail(student.getEmail());
        setPassword(student.getPassword());
        setFirstName(student.getFirstName());
        setLastName(student.getLastName());
        setAddress(student.getAddress());
        return "admin";
    }

    public List<Student> getAllStudents(){
        return studentService.getStudents();
    }

    public String getSubmitButtonLabel() {
        if (getId()==null)
            return "Add";
        else
            return "Update";
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean getHasLeftCourse(User_ user,  Long cid) {
        Map<String, Object> attr = getRow().getAttributes();
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        //System.out.println(getRow());
        return studentService.hasLeftCourse((Student) user, cid);
        //return true;
/*        System.out.println(course);
        return studentService.hasLeftCourse((Student) user, course.getId());*/
    }
}
