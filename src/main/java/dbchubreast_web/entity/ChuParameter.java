package dbchubreast_web.entity;
// Generated 16 mai 2018 12:50:32 by Hibernate Tools 4.3.5.Final

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * ChuParameter generated by hbm2java
 */
@Entity
@Table(name = "chu_parameter")
public class ChuParameter implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer idParameter;
	private String nom;
	private List<ChuPatientParameter> chuPatientParameters = new ArrayList<ChuPatientParameter>();

	public ChuParameter() {
	}

	public ChuParameter(Integer idParameter, String nom) {
		this.idParameter = idParameter;
		this.nom = nom;
	}

	public ChuParameter(Integer idParameter, String nom, List<ChuPatientParameter> chuPatientParameters) {
		this.idParameter = idParameter;
		this.nom = nom;
		this.chuPatientParameters = chuPatientParameters;
	}

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	@Column(name = "id_parameter", unique = true, nullable = false)
	public Integer getIdParameter() {
		return this.idParameter;
	}

	public void setIdParameter(Integer idParameter) {
		this.idParameter = idParameter;
	}

	@Column(name = "nom", nullable = false, length = 500)
	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chuParameter")
	public List<ChuPatientParameter> getChuPatientParameters() {
		return this.chuPatientParameters;
	}

	public void setChuPatientParameters(List<ChuPatientParameter> chuPatientParameters) {
		this.chuPatientParameters = chuPatientParameters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idParameter == null) ? 0 : idParameter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChuParameter other = (ChuParameter) obj;
		if (idParameter == null) {
			if (other.idParameter != null)
				return false;
		} else if (!idParameter.equals(other.idParameter))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChuParameter [idParameter=" + idParameter + ", nom=" + nom + "]";
	}
	
	

}
