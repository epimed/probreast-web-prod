package dbchubreast_web.entity;
// Generated 19 d�c. 2016 13:44:40 by Hibernate Tools 4.3.1.Final

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * ChuAntecedentGeneral generated by hbm2java
 */
@Entity
@Table(name = "chu_antecedent_general", schema = "db_chu_breast")
public class ChuAntecedentGeneral implements java.io.Serializable {

	private ChuAntecedentGeneralId id;
	private ChuGroupeMaladie chuGroupeMaladie;
	private ChuMaladie chuMaladie;
	private ChuPatient chuPatient;

	public ChuAntecedentGeneral() {
	}

	public ChuAntecedentGeneral(ChuAntecedentGeneralId id, ChuMaladie chuMaladie, ChuPatient chuPatient) {
		this.id = id;
		this.chuMaladie = chuMaladie;
		this.chuPatient = chuPatient;
	}

	public ChuAntecedentGeneral(ChuAntecedentGeneralId id, ChuGroupeMaladie chuGroupeMaladie, ChuMaladie chuMaladie,
			ChuPatient chuPatient) {
		this.id = id;
		this.chuGroupeMaladie = chuGroupeMaladie;
		this.chuMaladie = chuMaladie;
		this.chuPatient = chuPatient;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "idPatient", column = @Column(name = "id_patient", nullable = false, length = 20)),
			@AttributeOverride(name = "idMaladie", column = @Column(name = "id_maladie", nullable = false, length = 10)) })
	public ChuAntecedentGeneralId getId() {
		return this.id;
	}

	public void setId(ChuAntecedentGeneralId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_groupe_maladie")
	public ChuGroupeMaladie getChuGroupeMaladie() {
		return this.chuGroupeMaladie;
	}

	public void setChuGroupeMaladie(ChuGroupeMaladie chuGroupeMaladie) {
		this.chuGroupeMaladie = chuGroupeMaladie;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_maladie", nullable = false, insertable = false, updatable = false)
	public ChuMaladie getChuMaladie() {
		return this.chuMaladie;
	}

	public void setChuMaladie(ChuMaladie chuMaladie) {
		this.chuMaladie = chuMaladie;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_patient", nullable = false, insertable = false, updatable = false)
	public ChuPatient getChuPatient() {
		return this.chuPatient;
	}

	public void setChuPatient(ChuPatient chuPatient) {
		this.chuPatient = chuPatient;
	}

}
