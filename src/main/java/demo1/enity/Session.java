package demo1.enity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "session")
@NamedQueries({
	@NamedQuery(name = "deleteNullSession", query = "DELETE FROM Session s WHERE s.sessionKey = NULL")
})
public class Session implements Serializable {
	
	@Id
	@Column(name = "idusers")
	private int idusers;
	
	@Basic
	@Column(name = "sessionKey")
	private String sessionKey;
	
	@Basic
	@Column(name = "dateStartSession")
	private Date dateStartSession;

	public Session() {
		super();
	}

	public int getIdusers() {
		return idusers;
	}

	public void setIdusers(int idusers) {
		this.idusers = idusers;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public Date getDateStartSession() {
		return dateStartSession;
	}

	public void setDateStartSession(Date dateStartSession) {
		this.dateStartSession = dateStartSession;
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateStartSession, idusers, sessionKey);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Session other = (Session) obj;
		return Objects.equals(dateStartSession, other.dateStartSession) && idusers == other.idusers
				&& Objects.equals(sessionKey, other.sessionKey);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Session [idusers=");
		builder.append(idusers);
		builder.append(", sessionKey= XXXXXX ");
		builder.append(", dateStartSession=");
		builder.append(dateStartSession);
		builder.append("]");
		return builder.toString();
	}	
   
}
