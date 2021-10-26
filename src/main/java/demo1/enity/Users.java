package demo1.enity;

import java.util.Date;
import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name="users")
@NamedQueries({
	@NamedQuery(name="findUserByEmail",query="SELECT u FROM Users u WHERE u.email = :emailData"),
	@NamedQuery(name="findUserBySecretKey",query="SELECT u FROM Users u WHERE u.secretKey = :keyData")
})
public class Users {
    private int idusers;
    private String email;
    private String name;
    private String surname;
    private String password;
    private String secretKey;
    private Date dateKey;

    public Users() {}
    
    public Users(String email, String name, String surname, String password, String secretKey, Date dataKey) {
		super();
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.secretKey = secretKey;
		this.dateKey = dataKey;
	}

	@Id
    @Column(name = "idusers")
    public int getIdusers() {
        return idusers;
    }
	public void setIdusers(int idusers) {
        this.idusers = idusers;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "surname")
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Basic
    @Column(name = "secretKey")
    public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Basic
    @Column(name = "dateKey")
	public Date getDateKey() {
		return dateKey;
	}

	public void setDateKey(Date dateKey) {
		this.dateKey = dateKey;
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
		return Objects.equals(dateKey, other.dateKey) && Objects.equals(email, other.email) && idusers == other.idusers
				&& Objects.equals(name, other.name) && Objects.equals(password, other.password)
				&& Objects.equals(secretKey, other.secretKey) && Objects.equals(surname, other.surname);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dateKey, email, idusers, name, password, secretKey, surname);
	}
}
