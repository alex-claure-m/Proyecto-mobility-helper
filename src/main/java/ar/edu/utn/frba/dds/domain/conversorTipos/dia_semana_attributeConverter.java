package ar.edu.utn.frba.dds.domain.conversorTipos;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.DayOfWeek;

@Converter (autoApply = true)
public class dia_semana_attributeConverter implements AttributeConverter<DayOfWeek,String> {

    @Override
    public String convertToDatabaseColumn(DayOfWeek dayOfWeek) {
        /* guardare los valores ya convertidos , osea los veo en español*/
        String dia;
       switch (dayOfWeek){
           case MONDAY: dia = "Lunes";
               break;
           case TUESDAY: dia = "Martes";
               break;
           case WEDNESDAY: dia = "Miercoles";
               break;
           case THURSDAY: dia = "Jueves";
               break;
           case FRIDAY:dia = "Viernes";
               break;
           case SATURDAY: dia = "Sabado";
               break;
           case SUNDAY: dia = "Domingo";
               break;
           default: dia = null;
       }
       return dia;
    }

    @Override
    public DayOfWeek convertToEntityAttribute(String s) {
        if (s == null)
            return null;
        DayOfWeek dia;
        switch(s){
            case "Lunes" : dia  = DayOfWeek.MONDAY; break;
            case "Martes" : dia  = DayOfWeek.TUESDAY; break;
            case "Miercoles" : dia  = DayOfWeek.WEDNESDAY; break;
            case "Jueves" : dia  = DayOfWeek.THURSDAY; break;
            case "Viernes" : dia  = DayOfWeek.FRIDAY; break;
            case "Sabado" : dia  = DayOfWeek.SATURDAY; break;
            case "Domingo" : dia  = DayOfWeek.SUNDAY; break;
            default: throw new IllegalArgumentException(s +" no es un DAY OF WEEK valido");

        }
        return dia;
    }
}
