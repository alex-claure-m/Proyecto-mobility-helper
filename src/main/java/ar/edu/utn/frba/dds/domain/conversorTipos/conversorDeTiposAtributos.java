package ar.edu.utn.frba.dds.domain.conversorTipos;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;
import java.sql.Timestamp;

/*como le pongo autoAplly , es para que cuando el ORM me este persistiendo los datos
* y se encuentre con que LocalDate no puede pasar a DATE (a nivel sql), se fija en las clases
* si hay algun tipo de conversor , como lo hay, que lo aplique de directamente*/

/* ************ ARREGLAR DE DONDE VIENE TIMESTAMP *************** */
// arreglado -- 27/12
@Converter(autoApply = true)
public class conversorDeTiposAtributos implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
        return localDateTime == null ? null: Timestamp.valueOf(localDateTime);
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp date) {
        return date == null ? null: date.toLocalDateTime();
    }
}
