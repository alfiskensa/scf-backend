package com.prodain.scf.model.entity;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@MappedSuperclass
public class BaseEntityAuditTrail extends BaseEntity {

	private Long createdBy;
	
	private Date createdDate;
	
	private Date updatedDate;
	
	private Long updatedBy;
}
