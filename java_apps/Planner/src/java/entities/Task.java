package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t"),
    @NamedQuery(name = "Task.findByTaskId", query = "SELECT t FROM Task t WHERE t.taskId = :taskId"),
    @NamedQuery(name = "Task.findByDestination", query = "SELECT t FROM Task t WHERE t.destination = :destination"),
    @NamedQuery(name = "Task.findByDuration", query = "SELECT t FROM Task t WHERE t.duration = :duration"),
    @NamedQuery(name = "Task.findByStartDatetime", query = "SELECT t FROM Task t WHERE t.startDatetime = :startDatetime"),
    @NamedQuery(name = "Task.findByAlarmId", query = "SELECT t FROM Task t WHERE t.alarmId = :alarmId"),
    @NamedQuery(name = "Task.findByUserId", query = "SELECT t FROM Task t WHERE t.userId = :userId")})
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TaskId")
    private Integer taskId;
    @Size(max = 45)
    @Column(name = "Destination")
    private String destination;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Duration")
    private long duration;
    @Basic(optional = false)
    @NotNull
    @Column(name = "StartDatetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDatetime;
    @Column(name = "AlarmId")
    private Integer alarmId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UserId")
    private int userId;

    public Task() {
    }

    public Task(Integer taskId) {
        this.taskId = taskId;
    }

    public Task(Integer taskId, long duration, Date startDatetime, int userId) {
        this.taskId = taskId;
        this.duration = duration;
        this.startDatetime = startDatetime;
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskId != null ? taskId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        if ((this.taskId == null && other.taskId != null) || (this.taskId != null && !this.taskId.equals(other.taskId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Task[ taskId=" + taskId + " ]";
    }
    
}
