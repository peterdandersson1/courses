package jpa;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static lib.Helpers.truncateDate;

@Entity
public class Day {

    @Id
    @GeneratedValue
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "day")
    private Set<Attendance> attendances = new HashSet<>();

    public Day() {}

    public Day(Course course, Date date) {
        setCourse(course);
        setDate(date);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = truncateDate(date);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(Set<Attendance> attendances) {
        this.attendances = attendances;
    }

    public boolean addAttendance(Attendance attendance) {
        return getAttendances().add(attendance);
    }
}
