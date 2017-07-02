package components;

import ejb.CourseService;
import jpa.Course;
import jsf.CourseBean;

import javax.ejb.EJB;
import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.Date;
import java.util.Map;



@FacesComponent("groupkmp.components.QuestionView")
public class deregisterComponent extends UINamingContainer {

    @EJB
    CourseService courseService;

    private Long courseId;
    private Long userId;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

/*    public boolean test(Long id) {
        boolean status = false;
        //Course course = courseService.getCourse(id);
        if (courseService.isDateAfterStart(id, new Date())) {
            status = true;
        }
        else if (courseService.isDateAfterEnd(id, new Date())) {
            status = false;
        }
        return status;
    }

    public String deregister(Long courseId, Long studentId) {
        System.out.println("Dereg...................................................");
        courseService.leaveOrDeregisterFromCourse(courseId, studentId);
        return "/student" + "?faces-redirect=true";
    }

    public void register(Long courseId, Long studentId) {
        System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr "+courseId);
        courseService.registerForCourse(courseId, studentId);
        //return "/student" + "?faces-redirect=true";
    }

    public Object regHandle() {
        MethodExpression expression = (MethodExpression) getAttributes().get("action");

        ELContext elContext = getFacesContext().getELContext();
        Object[] params = {courseId, userId};

        return expression.invoke(elContext, params);
    }*/

/*    private int value = 2;

    public int getValue() {
        FacesContext context = getFacesContext();
        ELContext elContext = context.getELContext();
        UIComponent uiComp = this.getNamingContainer();
        Map<String, Object> ptAttrs = uiComp.getPassThroughAttributes();
        Map<String, Object> attrs = uiComp.getAttributes();
        return value;
    }

    public void setValue(int value) {

        this.value = value;

        //select();
        Object[] params = {getValue()};
        execEL("select", params);
    }

    public Object select() {
        MethodExpression expression = (MethodExpression) getAttributes().get("select");

        ELContext elContext = getFacesContext().getELContext();
        Object[] params = {getValue()};

        return expression.invoke(elContext, params);


    }

    public Object execEL(String method, Object[] params) {
        MethodExpression expression = (MethodExpression) getAttributes().get(method);
        ELContext elContext = getFacesContext().getELContext();
        return expression.invoke(elContext, params);
    }*/

}
