package ejb;

import jpa.Admin;
import jpa.Student;
import jpa.Teacher;
import jpa.User_;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Local
@Stateless
public class AdminService {

    @PersistenceContext
    EntityManager em;

    public Admin getAdmin() {
        TypedQuery<Admin> query = em.createNamedQuery("getAdmins", Admin.class);
        Admin admin = query.getResultList().get(0);
        return admin;
    }
}
