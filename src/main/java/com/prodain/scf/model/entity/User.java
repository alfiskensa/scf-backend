package com.prodain.scf.model.entity;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.prodain.scf.common.stereotype.HasName;
import com.prodain.scf.common.util.Constants;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@JsonFilter("user.filter")
@Getter @Setter
@Entity
@Table(name = "mst_user", catalog = Constants.SCF_SCHEMA)
public class User extends BaseEntityAuditTrail implements HasName {

	private String name;
	
	private String username;
	
	@Column(name = "password_hash")
	private String password;
	
	private String email;
	
	@Type(type = "boolean")
	private Boolean status;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "mst_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	
	@Column(name = "last_login_date")
	private Date lastLogin;
	
	public User(String username, String email, String encode) {
		this.username = username;
		this.email = email;
		this.password = encode;
	}
	
	public String getRoleLabel() {
		return Optional.ofNullable(roles).map(r -> r.stream().map(Role::getName).distinct().collect(Collectors.joining(","))).orElse("");
	}

}
