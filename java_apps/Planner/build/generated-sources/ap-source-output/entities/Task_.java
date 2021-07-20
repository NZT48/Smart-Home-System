package entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-06-19T14:06:32")
@StaticMetamodel(Task.class)
public class Task_ { 

    public static volatile SingularAttribute<Task, Long> duration;
    public static volatile SingularAttribute<Task, Date> startDatetime;
    public static volatile SingularAttribute<Task, String> destination;
    public static volatile SingularAttribute<Task, Integer> alarmId;
    public static volatile SingularAttribute<Task, Integer> userId;
    public static volatile SingularAttribute<Task, Integer> taskId;

}