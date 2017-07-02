package domain;

import jpa.Attendance;
import jpa.Student;

import java.util.Comparator;


public class AttendanceComparator implements Comparator<Attendance> {

    @Override
    public int compare(Attendance a1, Attendance a2) {
        String email1 = a1.getStudentCourse().getStudent().getEmail();
        String email2 = a2.getStudentCourse().getStudent().getEmail();

        return email1.compareTo(email2);
    }

}
