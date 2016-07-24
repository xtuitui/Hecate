package org.xiaotuitui.hecate.domain.model;

import java.util.Date;

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

@Entity
@Table(name = "tcs_user")
@NamedQueries(value = {
		@NamedQuery(name = "User.queryAllUser", query = "from User"),
		@NamedQuery(name = "User.queryUserByUsername", query = "from User u where u.username = :username"),
		@NamedQuery(name = "User.queryUserByNickname", query = "from User u where u.nickname like :nickname")
})
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "username", nullable = false, unique = true, updatable = false)
	private String username;
	
	@Column(name = "nickname")
	private String nickname;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "email", nullable = false)
	private String email;
	
	@Column(name = "login_success_times")
	private Integer loginSuccessTimes;
	
	@Column(name = "last_login_fail_times")
	private Integer lastLoginFailTimes;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "last_updated_time", nullable = false)
	private Date lastUpdatedTime;
	
	public User() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getLoginSuccessTimes() {
		return loginSuccessTimes;
	}

	public void setLoginSuccessTimes(Integer loginSuccessTimes) {
		this.loginSuccessTimes = loginSuccessTimes;
	}

	public Integer getLastLoginFailTimes() {
		return lastLoginFailTimes;
	}

	public void setLastLoginFailTimes(Integer lastLoginFailTimes) {
		this.lastLoginFailTimes = lastLoginFailTimes;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public String toString() {
		return "User [id=" + id + ", username=" + username + ", nickname="
				+ nickname + ", password=" + password + ", email=" + email
				+ ", loginSuccessTimes=" + loginSuccessTimes
				+ ", lastLoginFailTimes=" + lastLoginFailTimes
				+ ", lastUpdatedTime=" + lastUpdatedTime + "]";
	}

}