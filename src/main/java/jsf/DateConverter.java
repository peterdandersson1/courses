package jsf;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Date;

import static lib.Helpers.dateToString;
import static lib.Helpers.stringToDate;

@FacesConverter("groupkmp.DateConverter")
public class DateConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String dateStr) {
        return stringToDate(dateStr);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object date) {
        return dateToString((Date) date);
    }
}
