package jsf;


import ejb.AttendanceService;
import ejb.CourseService;
import ejb.StudentService;
import javafx.util.Pair;
import jpa.Attendance;
import jpa.Course;
import jpa.StudentCourse;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ManagedBean
@RequestScoped
public class StudentStats {

    @EJB
    AttendanceService attendanceService;
    @EJB
    CourseService courseService;
    @EJB
    StudentService studentService;

    @ManagedProperty(value="#{userBean}")
    private UserBean userBean;

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
    public UserBean getUserBean() {
        return userBean;
    }

    public List<Course> studentCourses(){

        return studentService.getCourses(userBean.getUser().getId());
    }

    public String getAttendance(Long id){

        Course course = courseService.getCourse(id);

        int attendance = 0;

        StudentCourse studentCourse = course.getStudentCourse(userBean.getUser().getId());
        Set<Attendance> attendanceList = studentCourse.getAttendances();
        for (Attendance attendance1 : attendanceList){
            attendance += attendance1.isPresent() ? 1 : 0;
        }
        int day = attendanceList.size();

        return "" + attendance + "/" + day;
    }
}