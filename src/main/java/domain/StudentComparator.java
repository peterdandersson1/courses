package domain;

import jpa.Course;
import jpa.Student;

import java.util.Comparator;


public class StudentComparator implements Comparator<Student> {

    @Override
    public int compare(Student s1, Student s2) {
        return s1.getEmail().compareTo(s2.getEmail());
    }

}
