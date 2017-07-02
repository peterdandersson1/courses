package ejb;


import domain.AttendanceComparator;
import jpa.Attendance;
import jpa.Course;
import jpa.Day;
import jpa.StudentCourse;
import org.eclipse.persistence.annotations.TimeOfDay;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static lib.Helpers.truncateDate;

@Local
@Stateless
public class AttendanceService {
    @PersistenceContext
    EntityManager em;

    @EJB
    CourseService courseService;

    /**
     * Add a day to a course.
     * This record is used in the management of attendance keeping.
     * @param course
     * @param date
     * @return
     */
    private Day createDay(Course course, Date date) {
        Day day = new Day(course, date);
        course.addDay(day);
        em.persist(day);
        em.merge(course);
        return day;
    }

    /**
     * Create an attendance record for a particular student on a particular day.
     * @param studentCourse
     * @param day
     * @return
     */
    private Attendance createAttendance(StudentCourse studentCourse, Day day) {
        Attendance attendance = new Attendance(studentCourse, day);
        studentCourse.addAttendance(attendance);
        day.addAttendance(attendance);
        em.persist(attendance);
        em.merge(studentCourse);
        em.merge(day);
        return attendance;
    }

/*    public Day getOrCreateDay(Long courseId, Date date) {
        Course course = courseService.getCourse(courseId);
        return getOrCreateDay(course, date);
    }*/


    /**
     * Get the day record for a course. But if it doesn't exist, return and create it.
     * @param course
     * @param date
     * @return
     */
    private Day getOrCreateDay(Course course, Date date) {
        Day day = course.getDay(date);
        if (day == null) {
            day = createDay(course, date);
        }
        return day;
    }


    public void createAllAttendanceRecords(Long courseId, Date date) {
        Course course = courseService.getCourse(courseId);
        // Don't create a record if the course is not running.
        if (!course.isCourseCurrentOn(date)) return;
        Day day = getOrCreateDay(course, date);
        for(StudentCourse studentCourse : course.getStudentCourses()) {
            getOrCreateAttendance(studentCourse, day);
        }
    }

/*    public Attendance getOrCreateAttendance(Long courseId, Long studentId, Date date) {
        Course course = courseService.getCourse(courseId);
        StudentCourse studentCourse = course.getStudentCourse(studentId);
        Attendance attendance = studentCourse.getAttendance(date);
        if (attendance == null) {
            Day day = getOrCreateDay(course, date);
            attendance = createAttendance(studentCourse, day);
        }
        return attendance;
    }*/

    private Attendance getOrCreateAttendance(StudentCourse studentCourse, Day day) {
        Attendance attendance = studentCourse.getAttendance(day.getDate());
        if (attendance == null) {
            attendance = createAttendance(studentCourse, day);
        }
        return attendance;
    }

    /**
     * Return a list of attendance records for all
     * students on a particular course on a particular date.
     * @param courseId
     * @param date
     * @return
     */
    public List<Attendance> getAttendances(Long courseId, Date date) {
        Course course = courseService.getCourse(courseId);

        // Return an empty list if the course is not running.
        if (!course.isCourseCurrentOn(date)) return new ArrayList<>();

        Day day = getOrCreateDay(course, date);

        List<Attendance> attendances = day.getAttendances()
                .stream()
                .filter(d -> d.getStudentCourse().getCourse().getId().equals(courseId)
                )
                .collect(Collectors.toList());

        attendances.sort(new AttendanceComparator());
        return attendances;
    }

    /**
     * Return a list of booleans that represents the attendance (present - true; not present false) of all
     * students on a particular course on a particular date.
     * @param courseId
     * @param date
     * @return
     */
    public List<Boolean> getPrestentList(Long courseId, Date date) {
        return getAttendances(courseId, date)
                .stream()
                .map(a -> a.isPresent())
                .collect(Collectors.toList());
    }

    public void setAttendances(Long courseId, Date date, List<Boolean> presentList) {
        System.out.println("service setAttemdance");
        List<Attendance> attendances = getAttendances(courseId, date);
        for (int i=0; i<attendances.size(); i++) {
            System.out.println(i + " " + courseId + " " + date + " " + presentList);
            Attendance attendance = attendances.get(i);
            attendance.setPresent(presentList.get(i));
            em.merge(attendance);
        }
    }


    /*

    Data Retrieval Methods.

     */


    /**
     * Expected attendance is the maximum possible attendance. If everyone turned up.
     * @param courseId
     * @param date
     * @return
     */
    public Long getExpectedAttendanceForDate(Long courseId, Date date) {
        Course course = courseService.getCourse(courseId);

        // Return zero for future days.
        if (date.after(new Date()) || !course.isCourseCurrentOn(date)) return 0L;

        return course.getStudentCourses()
                .stream()
                .filter(sc -> sc.getEndDate() == null || !date.after(sc.getEndDate()))
                .count();
    }

    public Long getAttendanceForDate(Long courseId, Date date) {
        Course course = courseService.getCourse(courseId);

        // Return zero for future days.
        if (date.after(new Date())
                || !course.isCourseCurrentOn(date))
            return 0L;

        if (course.getDay(date) == null) return null;

        return course.getDay(date).getAttendances()
                .stream()
                .filter(attendance -> attendance.isPresent())
                .count();
    }

    /**
     * Experimental - might use.
     * @param courseId
     * @param studentId
     * @param date
     * @return
     */
    public List<Attendance> getAttendances(Long courseId, Long studentId, Date date) {
        List<Attendance> attendances = getAttendances(courseId, date);
        return attendances.stream()
                .filter(attendance -> attendance.getStudentCourse().getStudent().getId() == studentId)
                .collect(Collectors.toList());
    }

}
