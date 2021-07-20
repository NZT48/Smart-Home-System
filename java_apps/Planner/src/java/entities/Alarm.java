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
@Table(name = "alarm")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alarm.findAll", query = "SELECT a FROM Alarm a"),
    @NamedQuery(name = "Alarm.findByAlarmId", query = "SELECT a FROM Alarm a WHERE a.alarmId = :alarmId"),
    @NamedQuery(name = "Alarm.findByAlarmTime", query = "SELECT a FROM Alarm a WHERE a.alarmTime = :alarmTime"),
    @NamedQuery(name = "Alarm.findBySongOfAlarm", query = "SELECT a FROM Alarm a WHERE a.songOfAlarm = :songOfAlarm"),
    @NamedQuery(name = "Alarm.findByUserId", query = "SELECT a FROM Alarm a WHERE a.userId = :userId")})
public class Alarm implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AlarmId")
    private Integer alarmId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AlarmTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date alarmTime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "SongOfAlarm")
    private String songOfAlarm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UserId")
    private int userId;

    public Alarm() {
    }

    public Alarm(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public Alarm(Integer alarmId, Date alarmTime, String songOfAlarm, int userId) {
        this.alarmId = alarmId;
        this.alarmTime = alarmTime;
        this.songOfAlarm = songOfAlarm;
        this.userId = userId;
    }

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getSongOfAlarm() {
        return songOfAlarm;
    }

    public void setSongOfAlarm(String songOfAlarm) {
        this.songOfAlarm = songOfAlarm;
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
        hash += (alarmId != null ? alarmId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Alarm)) {
            return false;
        }
        Alarm other = (Alarm) object;
        if ((this.alarmId == null && other.alarmId != null) || (this.alarmId != null && !this.alarmId.equals(other.alarmId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Alarm[ alarmId=" + alarmId + " ]";
    }
    
}
