package dbchubreast_web.entity;
// Generated 19 d�c. 2016 13:44:40 by Hibernate Tools 4.3.1.Final

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * ChuTypeTraitement generated by hbm2java
 */
@Entity
@Table(name = "chu_type_traitement", schema = "db_chu_breast")
public class ChuTypeTraitement implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idTypeTraitement;
	private String nom;
	private List<ChuTraitement> chuTraitements = new ArrayList<ChuTraitement>(0);

	public ChuTypeTraitement() {
	}

	public ChuTypeTraitement(Integer idTypeTraitement, String nom) {
		this.idTypeTraitement = idTypeTraitement;
		this.nom = nom;
	}

	public ChuTypeTraitement(Integer idTypeTraitement, String nom, List<ChuTraitement> chuTraitements) {
		this.idTypeTraitement = idTypeTraitement;
		this.nom = nom;
		this.chuTraitements = chuTraitements;
	}

	@Id

	@Column(name = "id_type_traitement", unique = true, nullable = false)
	public Integer getIdTypeTraitement() {
		return this.idTypeTraitement;
	}

	public void setIdTypeTraitement(Integer idTypeTraitement) {
		this.idTypeTraitement = idTypeTraitement;
	}

	@Column(name = "nom", nullable = false)
	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "chuTypeTraitement")
	public List<ChuTraitement> getChuTraitements() {
		return this.chuTraitements;
	}

	public void setChuTraitements(List<ChuTraitement> chuTraitements) {
		this.chuTraitements = chuTraitements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idTypeTraitement == null) ? 0 : idTypeTraitement.hashCode());
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
		ChuTypeTraitement other = (ChuTypeTraitement) obj;
		if (idTypeTraitement == null) {
			if (other.idTypeTraitement != null)
				return false;
		} else if (!idTypeTraitement.equals(other.idTypeTraitement))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChuTypeTraitement [idTypeTraitement=" + idTypeTraitement + ", nom=" + nom + "]";
	}

}
