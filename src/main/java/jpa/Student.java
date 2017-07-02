package jpa;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity()
@NamedQueries({
        @NamedQuery(name = "getStudent", query = "SELECT s FROM Student s WHERE s.id = :id"),
        @NamedQuery(name = "getStudents", query = "SELECT s FROM Student s ORDER BY s.email")
})
@DiscriminatorValue("STUDENT")
public class Student extends User_ {

    //@OneToMany(mappedBy = "student", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @OneToMany(mappedBy = "student")
    private Set<StudentCourse> studentCourses = new HashSet<>();


    public Student(String email, String password, String firstName, String lastName, String address) {
        super(email, password, firstName, lastName, address);
    }

    public Student() {
    }

    public void registerForCourse(Course course) {
        //courses.add(course);
    }

    public boolean isRegisteredForCourse(Course course) {
        return getCourseIds().contains(course.getId());
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", role=" + getDecriminatorValue() +
                '}';
    }

    public Set<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public StudentCourse getStudentCourse(Course course) {
        Long course_id = course.getId();
        return getStudentCourse(course_id);
    }

    public StudentCourse getStudentCourse(Long course_id) {
        Optional<StudentCourse> studentCourse = getStudentCourses()
                .stream()
                .filter(sc -> sc.getCourse().getId() == course_id)
                .findFirst();
        return studentCourse.get();
    }

    public void setStudentCourses(Set<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public void addStudentCourse(StudentCourse studentCourse) {
        studentCourses.add(studentCourse);
    }

    public void removeStudentCourse(StudentCourse studentCourse) {
        Set updatedStudentCourses = getStudentCourses().
                stream().
                filter(sc -> sc.getId() != studentCourse.getId()).
                collect(Collectors.toSet());
        setStudentCourses(updatedStudentCourses);
    }

    public Set<Course> getCourses() {
        return getStudentCourses().stream().map(sc -> sc.getCourse()).collect(Collectors.toSet());
    }

    public Set<Long> getCourseIds() {
        return getStudentCourses().stream().map(sc -> sc.getCourse().getId()).collect(Collectors.toSet());
    }

    public int getCourseCount() {
        return getStudentCourses().size();
    }

    /**
     * Add a course to the set of courses that the student is registered for,
     * if possible, and return true.
     * If not possible (either because the course is full or the student is
     * already registered for the course) return false.
     *
     * @param course
     * @return boolean
     */
    public boolean addCourse(Course course) {
        if (!getCourses().contains(course)) {
            StudentCourse sc = new StudentCourse(this, course);

            getCourses().add(course);
            return true;
        }
        return false;
    }

}
