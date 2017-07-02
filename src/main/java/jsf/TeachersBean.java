package jsf;

import ejb.CourseService;
import ejb.TeacherService;
import jpa.Course;
import jpa.Teacher;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.view.ViewScoped;
import java.util.*;

@ManagedBean
@SessionScoped
public class TeachersBean {

    @EJB
    TeacherService teacherService;
    @EJB
    CourseService courseService;

    Teacher editTeacherObj = new Teacher();

    private String page = "admin/teachers";

    private List<Course> courseList;

    private Long courseId;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    private boolean popUp = false;

    public boolean isPopUp() {
        return popUp;
    }

    public Teacher getEditTeacherObj() {
        return editTeacherObj;
    }

    public void setEditTeacherObj(Teacher editTeacherObj) {
        this.editTeacherObj = editTeacherObj;
    }

    public String saveTeacher(Teacher teacher){
        teacher.setCourses(courseList);
        teacherService.saveTeacher(teacher);
        popUp = false;
        return page;
    }

    public String addCourse(Long courseId){
        Course c = courseService.getCourse(courseId);

        boolean isNew = true;
        for(Course co : courseList){
            if(c.getId() == co.getId()){
                isNew = false;
                break;
            }
        }
        if(isNew){
            courseList.add(c);
        }

        popUp = true;
        return page;
    }

    public String cancelPopup(){
        System.out.println("Abort");
        popUp = false;
        return page;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public String edit(Teacher teacher){
        editTeacherObj = teacher;
        courseList = teacher.getCourses();

        popUp = true;
        return page;
    }

    public String create(){
        editTeacherObj = new Teacher();
        courseList = new ArrayList<>();
        popUp = true;
        return page;
    }

    public String removeCourse(Long courseId){

        for(Course co : courseList){
            if(co.getId() == courseId){
                courseList.remove(co);
                break;
            }
        }
        popUp = true;
        return page;
    }

    public List<Course> getAllCourses(){
        return courseService.getAllCourses();
    }

    public List<Teacher> getAllTeachers(){
        return teacherService.getAllTeachers();
    }

    public boolean teachersExist(){
        return (null != getAllTeachers() && getAllTeachers().size() != 0);
    }

    public List<Map<String, String>> textItems(){

        List<Map<String, String>> list = new ArrayList<>();
        Map map;

        for(Teacher t: getAllTeachers()){
            map = new HashMap();
            map.put("First Name:",t.getFirstName());
            map.put("Last Name:", t.getLastName());
            map.put("Email:", t.getEmail());
            map.put("ID:", t.getId());

            list.add(map);
        }

        return list;
    }

    public String removeTeacher(Long id){
        teacherService.removeTeacher(id);
        return page;
    }

    public String test(){
        return "dev-links";
    }

}
