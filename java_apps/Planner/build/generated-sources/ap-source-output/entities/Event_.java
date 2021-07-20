package entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-06-19T04:24:13")
@StaticMetamodel(Event.class)
public class Event_ { 

    public static volatile SingularAttribute<Event, Long> duration;
    public static volatile SingularAttribute<Event, String> destination;
    public static volatile SingularAttribute<Event, Integer> id;
    public static volatile SingularAttribute<Event, Date> startDate;

}