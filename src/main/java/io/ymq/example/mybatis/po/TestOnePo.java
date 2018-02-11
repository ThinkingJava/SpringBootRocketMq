
package io.ymq.example.mybatis.po;

import java.io.Serializable;
import java.util.Date;

public class TestOnePo implements Serializable{
	private static final long serialVersionUID = 1L;
	//alias
	public static final String TABLE_ALIAS = "test";
	

	//columns START
    /**
     * id       db_column: id   
     */ 	
	private String id;
    /**
     * 名称       db_column: name   
     */ 	
	private String name;
    /**
     * 备注       db_column: remark   
     */ 	
	private Date lrrq;
	//columns END

	private Date xgrq;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLrrq() {
		return lrrq;
	}

	public void setLrrq(Date lrrq) {
		this.lrrq = lrrq;
	}

	public Date getXgrq() {
		return xgrq;
	}

	public void setXgrq(Date xgrq) {
		this.xgrq = xgrq;
	}
}

