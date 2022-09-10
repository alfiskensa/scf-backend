package com.prodain.scf.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.prodain.scf.util.Constants;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "mst_menu", catalog = Constants.SCF_SCHEMA)
public class Menu extends BaseEntity {

	private String name;
	
	private String description;
	
	private Boolean isActive;
}
