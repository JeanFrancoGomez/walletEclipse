package demo1.enity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.*;


@Entity
@Table(name = "users")
@SecondaryTable(name = "session", pkJoinColumns = @PrimaryKeyJoinColumn(name = "idusers", referencedColumnName = "idusers"))
@NamedQueries({ 
	@NamedQuery(name = "findUserByEmail", query = "SELECT u FROM Users u WHERE u.email = :emailData"),
	@NamedQuery(name = "findUserBySecretKey", query = "SELECT u FROM Users u WHERE u.secretKey = :secretKeyData"),
	@NamedQuery(name = "findUsersNotConfirmed", query = "SELECT u FROM Users u WHERE u.stateUser = :stateData AND u.dateKey < :dateKeyData"),
	@NamedQuery(name = "deleteUsersToConfirm", query = "DELETE FROM Users u WHERE u.stateUser = :stateData AND u.dateKey < :dateKeyData"),
	@NamedQuery(name = "deleteExpiredSession", query = "UPDATE Users u SET u.sessionKey = NULL WHERE u.dateStartSession < :dateStartSessionData")
})
public class Users {

	@Id
	@Column(name = "idusers", updatable=false, nullable=false)
	private int idusers;
	
	@Basic
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private UsersRole role;
	
	@Basic
	@Column(name = "email", unique = true, length = 256)
	private String email;
	
	@Basic
	@Column(name = "name")
	private String name;
	
	@Basic
	@Column(name = "surname")
	private String surname;
	
	@Basic
	@Column(name = "password")
	private String password;
	
	@Basic
	@Column(name = "secretKey")
	private String secretKey;
	
	@Basic
	@Column(name = "dateKey")
	private Date dateKey;
	
	@Basic
	@Column(name = "stateUser")
	@Enumerated(EnumType.STRING)
	private UsersStatus stateUser;
	
	@Column(table = "session")
	private String sessionKey;
	
	@Column(table = "session")
	private Date dateStartSession;

	public Users() {
	}	 

	public Users(UsersRole role, String email, String name, String surname,
			String password, String secretKey, Date dateKey, UsersStatus stateUser) {
		super();
		this.role = role;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.secretKey = secretKey;
		this.dateKey = dateKey;
		this.stateUser = stateUser;
	}

	public int getIdusers() {
		return idusers;
	}

	public void setIdusers(int idusers) {
		this.idusers = idusers;
	}

	public UsersRole getRole() {
		return role;
	}

	public void setRole(UsersRole role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Date getDateKey() {
		return dateKey;
	}

	public void setDateKey(Date dateKey) {
		this.dateKey = dateKey;
	}

	public UsersStatus getStateUser() {
		return stateUser;
	}

	public void setStateUser(UsersStatus stateUser) {
		this.stateUser = stateUser;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Users other = (Users) obj;
		return Objects.equals(dateKey, other.dateKey) && Objects.equals(dateStartSession, other.dateStartSession)
				&& Objects.equals(email, other.email) && idusers == other.idusers && Objects.equals(name, other.name)
				&& Objects.equals(password, other.password) && role == other.role
				&& Objects.equals(secretKey, other.secretKey) && Objects.equals(sessionKey, other.sessionKey)
				&& stateUser == other.stateUser && Objects.equals(surname, other.surname);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateKey, dateStartSession, email, idusers, name, password, role, secretKey, sessionKey,
				stateUser, surname);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Users [idusers=");
		builder.append(idusers);
		builder.append(", role=");
		builder.append(role);
		builder.append(", email=");
		builder.append(email);
		builder.append(", name=");
		builder.append(name);
		builder.append(", surname=");
		builder.append(surname);
		builder.append(", password=");
		builder.append(password);
		builder.append(", secretKey=");
		builder.append(secretKey);
		builder.append(", dateKey=");
		builder.append(dateKey);
		builder.append(", stateUser=");
		builder.append(stateUser);
		builder.append(", sessionKey=");
		builder.append(sessionKey);
		builder.append(", dateStartSession=");
		builder.append(dateStartSession);
		builder.append("]");
		return builder.toString();
	}



}
