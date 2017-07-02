package ejb;

import jpa.*;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Local
@Stateless
public class UserService {

    @PersistenceContext
    EntityManager em;

    public String userToString() {
        return this.toString();
    }

    public Long createUser(String email, String password, String role){

        Long id = createUser(role, email, password, "fnTest", "lnTest", "addrTest");
        return id;
    }

    public Long createUser(String role, String email, String password, String firstName, String lastName, String address){
        User_ user = null;
        if (role.equals("TEACHER")) {
            user = new Teacher(email, password, firstName, lastName, address);
        }
        else if(role.equals("STUDENT")) {
            user = new Student(email, password, firstName, lastName, address);
        }
        else if(role.equals("ADMIN")) {
            user = new Admin(email, password, firstName, lastName, address);
        }
        em.persist(user);
        return user.getId();
    }

    public Long countUsers() {
        List<Long> c = em.createNamedQuery("countUsers").getResultList();
        Long i = c.get(0);
        return i;
    }

    public User_ getUser(Long id) {
        User_ user = em.find(User_.class, id);
        //User_ user = (User_) em.createNamedQuery("getUser").setParameter("id", id).getSingleResult();
        return user;
    }

    public void removeUser(Long id) {
        User_ user = getUser(id);
        em.remove(user);
    }

    public User_ logIn(String email, String password) {
        try{
            TypedQuery<User_> query = em.createNamedQuery("LogIn", User_.class);
            query.setParameter("email", email);
            query.setParameter("password", password);

            User_ user = query.getSingleResult();

            return user;
        } catch (Exception e){
            return null;
        }
    }

    public boolean checkForUser(){
        try{
            TypedQuery<User_> query = em.createNamedQuery("countUsers", User_.class);

            int count = ((Number)em.createNamedQuery("countUsers", Long.class).getSingleResult()).intValue();
            System.out.println(count);
            return count == 0 ? false : true;

        } catch (Exception e){
            return false;
        }
    }

}
