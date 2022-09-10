package com.prodain.scf.model.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.prodain.scf.stereotype.HasName;
import com.prodain.scf.util.Constants;

import lombok.Getter;
import lombok.Setter;

@JsonFilter("role.filter")
@Getter @Setter
@Entity
@Table(name = "mst_roles", catalog = Constants.SCF_SCHEMA)
public class Role extends BaseEntity implements HasName {

	private String name;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "mst_role_menu", joinColumns = @JoinColumn(name = "role_id"), 
					inverseJoinColumns = @JoinColumn(name = "menu_id"))
	private Set<Menu> menus;
}
