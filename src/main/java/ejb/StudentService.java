package ejb;

import domain.CourseComparator;
import jpa.*;
import jpa.Student;
import lib.StatusCode;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static lib.StatusCode.*;
import static lib.Helpers.*;

@Local
@Stateless
public class StudentService {

    @PersistenceContext
    EntityManager em;

    @EJB
    UserService userService;

    @EJB
    CourseService courseService;

    public Long createStudent(String email, String password) {
        return userService.createUser(email, password, "STUDENT");
    }

    public void saveStudent(Student student) {
        if(student.getId() == null){
            em.persist(student);
        } else {
            Student s = em.find(Student.class,student.getId());
            s.setEmail(student.getEmail());
            s.setPassword(student.getPassword());
            s.setFirstName(student.getFirstName());
            s.setLastName(student.getLastName());
            s.setAddress(student.getAddress());
            s.setAddress(student.getAddress());
            em.merge(s);
        }
    }

    public Student getStudent(Long id) {
        return (Student) userService.getUser(id);
    }

    public Student getFirstStudent() {
        TypedQuery<Student> query = em.createNamedQuery("getStudents", Student.class);
        Student student = query.getResultList().get(0);
        return student;
    }

    public List<Student> getStudents() {
        TypedQuery<Student> query = em.createNamedQuery("getStudents", Student.class);
        List<Student> students = query.getResultList();
        return students;
    }

    public void updateStudent(Long id, String email, String password) {
        Student student = getStudent(id); // em.find(Student.class, id);
        student.setEmail(email);
        student.setPassword(password);
        em.merge(student);
    }

    public void removeStudent(Long id) {
        userService.removeUser(id);
    }

    public List<Course> getCourses(Long studentId) {
        Student student = getStudent(studentId);
        Set<Course> courses = student.getCourses();
        return courses.stream().sorted(new CourseComparator()).collect(Collectors.toList());
    }

    public List<Course> getOtherCourses(Long studentId) {
        Student student = getStudent(studentId);
        Set<Long> registeredCourseIds = student.getCourseIds();
        List<Long> allCourseIds = courseService.getAllCourseIds();
        allCourseIds.removeAll(registeredCourseIds);
        return allCourseIds.stream()
                .map(id -> courseService.getCourse(id))
                .filter(course -> !course.isFull())
                .sorted(new CourseComparator())
                .collect(Collectors.toList());
    }

/*    public StatusCode registerForCourse(Student student, Long course_id) {
        Course course = courseService.getCourse(course_id);

        if (student.isRegisteredForCourse(course)) {
            return ALREADY_REGISTERED;
        }
        else if (course.isFull()) {
            return COURSE_FULL;
        }
        else {
            StudentCourse studentCourse = new StudentCourse(student, course);
            student.addStudentCourse(studentCourse);
            course.addStudentCourse(studentCourse);
            em.persist(studentCourse);
            em.merge(student);
            em.merge(course);
            return SUCCESSFULLY_REGISTERED;
        }

    }*/

/*    public StatusCode deregisterFromCourse(Student student, Long course_id) {
        Course course = courseService.getCourse(course_id);

        if (student.isRegisteredForCourse(course)) {
            StudentCourse studentCourse = student.getStudentCourse(course);
*//*            student.removeStudentCourse(studentCourse);
            course.removeStudentCourse(studentCourse);
            em.merge(student);
            em.merge(course);*//*
            studentCourse.setEndDate(truncateDate(new Date()));
            em.merge(studentCourse);
            //StudentCourse sc = em.merge(studentCourse);
            //em.remove(sc);
            return DEREGISTERED;
        }
        else {
            return STUDENT_NOT_ON_COURSE;
        }

    }*/

    /**
     * Has the student left the course early
     * @param student
     * @param courseId
     * @return boolean - true if the student has left the course early, else false.
     */
    public boolean hasLeftCourse(Student student, Long courseId) {
        Course course = courseService.getCourse(courseId);
        return getLeavingDate(student, course) != null;

    }

    /**
     * I the student leaves the course early
     * @param student
     * @param course
     * @return
     */
    public Date getLeavingDate(Student student, Course course) {
        StudentCourse studentCourse = student.getStudentCourse(course);
        return studentCourse.getEndDate();
    }

}
