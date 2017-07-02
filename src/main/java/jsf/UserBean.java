package jsf;

import ejb.AdminService;
import ejb.StudentService;
import ejb.TeacherService;
import ejb.UserService;
import jpa.Course;
import jpa.Student;
import jpa.Teacher;
import jpa.User_;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static lib.Helpers.notification;

@ManagedBean
@SessionScoped
public class UserBean {

    @EJB
    UserService userService;

    @EJB
    AdminService adminService;

    @EJB
    StudentService studentService;

    @EJB
    TeacherService teacherService;

    private String email;
    private String password;
    private String role;
    private boolean newDB = false;

    private User_ user;

    public String logIn(){
        if((!userService.checkForUser()) && email.equals("admin") && password.equals("admin")){
            newDB = true;
            email = "";
            password = "";
            return "/add-user";
        }
        user = userService.logIn(email, password);
        setEmail("");
        setPassword("");
        if(user == null){
            notification(FacesMessage.SEVERITY_ERROR, "Error", "Wrong Email or Password.");
            return "login";
        }
        return user.getRole().toLowerCase() + "?faces-redirect=true";
    }

    public String logOut(){
        user = null;
        return "/login";
    }

    public String createUser() {
        Long id = userService.createUser(email, password, "ADMIN");
        newDB = false;
        setEmail("");
        setPassword("");
        return "/login";
    }

/*    public void register(Long course_id) {
        System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr "+course_id);
        studentService.registerForCourse((Student) user, course_id);
        //return "/student" + "?faces-redirect=true";
    }

    public void deregister(Long course_id) {
        studentService.deregisterFromCourse((Student) user, course_id);
        //return "/student" + "?faces-redirect=true";
    }*/

    public boolean isLoggedIn() {
        return user != null;
    }

    public boolean isStudent() {
        return user != null && user.getRole().equals("STUDENT");
    }

    public boolean isTeacher() {
        return user != null && user.getRole().equals("TEACHER");
    }

    public boolean isAdmin() {
        return user != null && user.getRole().equals("ADMIN");
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isNewDB() {
        return newDB;
    }

    public String getUserInfo(){
        return user.toString();

    }

    public User_ getUser() {
        return user;
    }

    public void setUser(User_ user) {
        this.user = user;
    }

    public List<Course> getCourses() {
        if (isStudent()) {
            return studentService.getCourses(getUser().getId());
        }
        else if (isTeacher()) {
            return teacherService.getCourses(getUser().getId());
        }
        return null;
    }

    public List<Course> getOtherCourses() {
        if (isStudent()) {
            return studentService.getOtherCourses(getUser().getId());
        }
        return null;
    }

    /**
     * Dev method. Remove.
     * @return
     */
    public String autoLogin() {
        user = adminService.getAdmin();
        return "/admin";
    }

    /**
     * Dev method. Remove.
     * @return
     */
    public String autoLoginAsStudent() {
        user = studentService.getFirstStudent();
        return "/student";
    }

    /**
     * Dev method. Remove.
     * @return
     */
    public String teacherPage() {
        user = teacherService.getFirstTeacher();
        return "/teacher?faces-redirect=true";
    }

    public String autoStudentLogin(Long id) {
        user = studentService.getStudent(id);
        return "/student?faces-redirect=true";
    }

    public String adminStudentsPage() {
        autoLogin();
        return "/admin/students?faces-redirect=true";
    }

    public String studentCoursesPage() {
        autoLoginAsStudent();
        return "/student?faces-redirect=true";
    }

    public String attendancePage() {
        autoLogin();
        return "/attendance?faces-redirect=true";
    }

}
