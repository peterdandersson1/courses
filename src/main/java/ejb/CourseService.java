package ejb;

import domain.StudentComparator;
import jpa.*;
import lib.StatusCode;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

import static lib.Helpers.dateToWeek;
import static lib.Helpers.dateToYear;
import static lib.Helpers.truncateDate;
import static lib.StatusCode.*;


@Local
@Stateless
public class CourseService {
    @PersistenceContext
    EntityManager em;

    @EJB
    StudentService studentService;

    public String courseToString() {
        return this.toString();
    }

    public Long createCourse(String courseName, String description){
        Date date = new Date();
        Long id = createCourse(courseName, description, date, date, 2);
        return id;
    }

    public Long createCourse(String courseName, String description, Date startDate, Date endDate, int maxStudents) {
        Course course = new Course(courseName, description, startDate, endDate, maxStudents);
        em.persist(course);
        return course.getId();
    }

    public Course getCourse(Long id) {
        //Course course = (Course) em.createNamedQuery("getCourse").setParameter("id", id).getSingleResult();
        Course course = em.find(Course.class, id);
        return course;
    }

    public List<Course> getAllCourses() {
        return em.createNamedQuery("getAllCourses").getResultList();
    }

    public List<Long> getAllCourseIds() {
        return getAllCourses().stream().map(course -> course.getId()).collect(Collectors.toList());
    }

    public void updateCourse(Long id, String courseName, String description, Date startDate, Date endDate, int maxStudents) {
        Course course = em.find(Course.class, id);
        course.setCourseName(courseName);
        course.setDescription(description);
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        course.setMaxStudents(maxStudents);
        em.merge(course);
    }

    public void removeCourse(Long id) {
        Course course = getCourse(id);
        em.remove(course);
    }

    public Date getEndDate(Long courseId) {
        Course course = getCourse(courseId);
        return course.getEndDate();
    }

    public boolean isFull(Long id) {
        Course course = getCourse(id);
        return course.isFull();
    }

    public Long countCourses() {
        List<Long> c = em.createNamedQuery("countCourses").getResultList();
        Long i = c.get(0);
        return i;
    }

    public List<Student> getStudents(Long course_id) {
        Course course = getCourse(course_id);
        List<Student> students = new ArrayList<>(course.getStudents());
        students.sort(new StudentComparator());
        return students;
    }

    public boolean isStudentRegistered(Long courseId, Long studentId) {
        Course course = getCourse(courseId);
        return course.isStudentOnCourse(studentId);
    }

    public boolean isDateAfterStart(Long courseId, Date date) {
        Course course = getCourse(courseId);
        return course.isDateAfterStartDate(date);
    }

    public boolean isDateAfterEnd(Long courseId, Date date) {
        Course course = getCourse(courseId);
        return course.isDateAfterEndDate(date);
    }

    public boolean isCourseCurrent(Long courseId, Date date) {
        Course course = getCourse(courseId);
        return course.isCourseCurrentOn(date);
    }

    public Date getLeavingDate(Long courseId, Long studentId) {
        Course course = getCourse(courseId);
        StudentCourse studentCourse = course.getStudentCourse(studentId);
        if (studentCourse == null) {
            //return STUDENT_NOT_ON_COURSE;
            return null;
        }
        else {
            return studentCourse.getEndDate();
        }
    }

    public StatusCode registerForCourse(Long courseId, Long studentId) {
        Course course = getCourse(courseId);
        Student student = studentService.getStudent(studentId);

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

    }

    private void deregisterFromCourse(Course course, Student student) {
        StudentCourse studentCourse = student.getStudentCourse(course);
        Set<Attendance> attendances = studentCourse.getAttendances();
        for (Attendance attendance : attendances) {
            em.remove(attendance);
        }
        student.removeStudentCourse(studentCourse);
        course.removeStudentCourse(studentCourse);
        em.merge(student);
        em.merge(course);
        StudentCourse sc = em.merge(studentCourse);
        em.remove(sc);
    }

    private void leaveCourse(Course course, Student student) {
        StudentCourse studentCourse = student.getStudentCourse(course);
        studentCourse.setEndDate(truncateDate(new Date()));
        em.merge(studentCourse);
    }

    /**
     * Student leaves the course if it has already started (by setting endDate
     * in the studentCourses record).
     * Student deregisters from the course if it has not yet started started (by
     * deleting the student courses record).
     * @param course_id
     * @param studentId
     * @return
     */
    public StatusCode leaveOrDeregisterFromCourse(Long course_id, Long studentId) {
        Course course = getCourse(course_id);
        Student student = studentService.getStudent(studentId);

        if (student.isRegisteredForCourse(course)) {
            if (course.isCourseCurrentOn(new Date())) {
                leaveCourse(course, student);
                return LEFT_COURSE;
            }
            else if (!course.isDateAfterStartDate(new Date())) { // The course hasn't begun yet.
                deregisterFromCourse(course, student);
                return DEREGISTERED_FROM_COURSE;
            }
            return COURSE_ENDED;
        }
        else {
            return STUDENT_NOT_ON_COURSE;
        }
    }

    /**
     * Gets a list of calendar objects. Each represents a day (it could be any day)
     * in each week of the course's duration.
     * @param courseId
     * @return
     */
    public List<Calendar> getCourseWeeks(Long courseId) {
        Course course = getCourse(courseId);
        Date startDate = course.getStartDate();
        int startWeek = dateToWeek(startDate);
        int startYear = dateToYear(startDate);
        int endWeek = dateToWeek(course.getEndDate());
        int endYear = dateToYear(course.getEndDate());
        int endEpochWeek = endYear * 53 + endWeek;   // Hackish!

        List<Calendar> weeks = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, startWeek);
        calendar.set(Calendar.YEAR, startYear);

        while(calendar.get(Calendar.YEAR) * 53 + calendar.get(Calendar.WEEK_OF_YEAR) <= endEpochWeek) {
            // Create new calendar instance and add it to the the list.
            Calendar cal = Calendar.getInstance();
            cal.setTime(calendar.getTime());
            weeks.add(cal);

            // Increment by a week.
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        return weeks;
    }

}
