package com.prodain.scf.model.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFilter;

import lombok.Getter;
import lombok.Setter;

@JsonFilter("privilege.filter")
@Getter @Setter
@Entity
@Table(name = "mst_privileges")
public class Privilege extends BaseEntity {

	private String name;
}
