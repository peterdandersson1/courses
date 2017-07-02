package jpa;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

import static lib.Helpers.truncateDate;

@Entity
@NamedQueries({
        @NamedQuery(name = "getCourse", query="SELECT c FROM Course c WHERE c.id = :id"),
        @NamedQuery(name = "getAllCourses", query="SELECT c FROM Course c ORDER BY c.courseName"),
        @NamedQuery(name = "countCourses", query="SELECT COUNT(c) FROM Course c")
})
public class Course {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 255)
    private String courseName;
    @Column(length = 3000)
    private String description;
    private Date startDate;
    private Date endDate;
    private int maxStudents;


    //@OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @OneToMany(mappedBy = "course")
    private Set<StudentCourse> studentCourses = new HashSet<StudentCourse>();

    @OneToMany(mappedBy = "course")
    private Set<Day> days = new HashSet<Day>();

    public Course() {
    }

    public Course(String courseName, String description, Date startDate, Date endDate, int maxStudents) {
        this.courseName = courseName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxStudents = maxStudents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = truncateDate(startDate);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = truncateDate(endDate);
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Set<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(Set<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }

    /**
     * Add a student to a course.
     * @param studentCourse
     */
    public void addStudentCourse(StudentCourse studentCourse) {
        studentCourses.add(studentCourse);
    }

    /**
     * Romove a particular StudentCourse instance fro this course.
     * (This is equivalent to a student leaving tthe course).
     * @param studentCourse
     */
    public void removeStudentCourse(StudentCourse studentCourse) {
        Set updatedStudentCourses = getStudentCourses()
                .stream()
                .filter(sc -> sc.getId() != studentCourse.getId())
                .collect(Collectors.toSet());
        setStudentCourses(updatedStudentCourses);
    }

    public int getStudentCount() {
        return studentCourses.size();
    }

    /**
     * Returns true if no more students can register for the course (because it is full).
     * @return
     */
    public boolean isFull() {
        return getStudentCount() >= getMaxStudents();
    }


    /**
     * Gets all the students on the course.
     * @return
     */
    public Set<Student> getStudents() {
        return getStudentCourses()
                .stream()
                .map(sc -> sc.getStudent())
                .collect(Collectors.toSet());
    }

    public boolean isStudentOnCourse(Long studentId) {
        return getStudentCourse(studentId) != null;
    }

    /**
     * Gets all of the days for which attendance records exist for this course.
     * @return
     */
    public Set<Day> getDays() {
        return days;
    }

    public void setDays(Set<Day> days) {
        this.days = days;
    }

    /**
     * Attempts to get an attendance record day for this course.
     * Returns the day, or null if it is not present.
     * @param date
     * @return
     */
    public Day getDay(Date date) {
        Date truncatedDate = truncateDate(date);
        Optional<Day> day = getDays()
                .stream()
                .filter(d -> d.getDate().equals(truncatedDate))
                .findFirst();
        return day.isPresent() ? day.get() : null;
    }

    /**
     * Attempts to get a student course record for a particular student.
     * If the student is not currently registered for this course null is returned.
     * @param studentId
     * @return
     */
    public StudentCourse getStudentCourse(Long studentId) {
        Optional<StudentCourse> studentCourse = studentCourses
                .stream()
                .filter(sc -> sc.getStudent().getId().equals(studentId))
                .findFirst();
        return studentCourse.isPresent() ? studentCourse.get() : null;
    }

    public void addDay(Day day) {
        days.add(day);
    }


    /**
     * Did the course start after a particular date.
     * (Or whether a given date is BEFORE the start of the course).
     * Note: returns true if the course has ended.
     * @param date
     * @return
     */
    public boolean isDateAfterStartDate(Date date) {
        return date.after(getStartDate()) || date.equals(getStartDate());
    }


    /**
     * Did the course end after a particular date.
     * (Or whether a given date is BEFORE the end of the course).
     * @param date - a truncated date  is used  - where time components
     *             (hours, minutes, etc) are converted to zero.
     * @return
     */
    public boolean isDateAfterEndDate(Date date) {
        return truncateDate(date).after(getEndDate());
    }

    /**
     * Establishes whether the course is in progress on a given date.
     * @param date
     * @return
     */
    public boolean isCourseCurrentOn(Date date) {
        return isDateAfterStartDate(date) && !isDateAfterEndDate(date);
    }

}
