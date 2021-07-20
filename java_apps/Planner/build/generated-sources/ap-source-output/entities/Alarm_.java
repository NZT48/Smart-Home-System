package entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-06-19T14:06:32")
@StaticMetamodel(Alarm.class)
public class Alarm_ { 

    public static volatile SingularAttribute<Alarm, String> songOfAlarm;
    public static volatile SingularAttribute<Alarm, Date> alarmTime;
    public static volatile SingularAttribute<Alarm, Integer> alarmId;
    public static volatile SingularAttribute<Alarm, Integer> userId;

}