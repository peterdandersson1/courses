package testpack;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class TestEJB {
    private String testString;
    private boolean isAdmin = true;
    private boolean isStudent = true;
    private boolean isTeacher = true;
    private boolean isLoggedIn = true;

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public String getTestString() {
        return (testString != null) ? testString : "Hello test!";
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }
}

