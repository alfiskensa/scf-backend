package com.prodain.scf.common.stereotype;

import java.io.Serializable;

public interface IBaseEntity<ID extends Serializable> {

	public ID getId();
	public void setId(ID id);
}
