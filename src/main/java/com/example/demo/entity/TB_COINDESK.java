package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TB_COINDESK {

	@Id
//	@GeneratedValue

	private String CURCD; // 幣別

	private String NAME; // 幣別中文名稱

	private String RATE; // 匯率

	private String CREATEDATE; // 建立時間

	private String LASTUPDATEDATE; // 最後更新時間

	public void setCURCD(String curcd) {
		this.CURCD = curcd;
	}

	public String getCURCD() {
		return CURCD;
	}

	public void setNAME(String name) {
		this.NAME = name;
	}

	public String getNAME() {
		return NAME;
	}

	public void setRATE(String rate) {
		this.RATE = rate;
	}

	public String getRATE() {
		return RATE;
	}

	public void setCREATEDATE(String createdate) {
		this.CREATEDATE = createdate;
	}

	public String getCREATEDATE() {
		return CREATEDATE;
	}

	public void setLASTUPDATEDATE(String updatedate) {
		this.LASTUPDATEDATE = updatedate;
	}

	public String getLASTUPDATEDATE() {
		return LASTUPDATEDATE;
	}

}
