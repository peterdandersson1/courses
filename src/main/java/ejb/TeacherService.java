package ejb;


import domain.CourseComparator;
import jpa.Course;
import jpa.Student;
import jpa.Teacher;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Local
@Stateless
public class TeacherService {

    @PersistenceContext
    EntityManager em;

    public List<Teacher> getAllTeachers(){
        return em.createNamedQuery("selectAll", Teacher.class).getResultList();
    }

    public Teacher getTeacher(Long id) {
        TypedQuery<Teacher> query = em.createNamedQuery("getTeacher", Teacher.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public Teacher getFirstTeacher() {
        TypedQuery<Teacher> query = em.createNamedQuery("selectAll", Teacher.class);
        Teacher teacher = query.getResultList().get(0);
        return teacher;
    }

    public List<Course> getCourses(Long teacherId) {
        Teacher teacher = getTeacher(teacherId);
        List<Course> courses = teacher.getCourses();
        return courses.stream().sorted(new CourseComparator()).collect(Collectors.toList());
    }

    public void removeTeacher(Long id){
        Teacher teacher = em.find(Teacher.class, id);
        em.remove(teacher);
    }

    public void saveTeacher(Teacher teacher){
        if(teacher.getId() == null){
            em.persist(teacher);
        } else {
            em.merge(teacher);
        }
    }


}
