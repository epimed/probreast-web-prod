/**
 * EpiMed - Information system for bioinformatics developments in the field of epigenetics
 * 
 * This software is a computer program which performs the data management 
 * for EpiMed platform of the Institute for Advances Biosciences (IAB)
 *
 * Copyright University of Grenoble Alps (UGA)
 * GNU GENERAL PUBLIC LICENSE
 * Please check LICENSE file
 *
 * Author: Ekaterina Bourova-Flin 
 *
 */
package dbchubreast_web.service.business;

import java.util.List;

import dbchubreast_web.entity.ChuTumeur;

public interface ChuTumeurService {
	public ChuTumeur find(Integer idTumeur);

	public ChuTumeur findWithDependencies(Integer idTumeur);

	public ChuTumeur findByIdPhaseWithDependencies(Integer idPhase);

	public List<ChuTumeur> listByIdPatient(String idPatient);

	public List<ChuTumeur> listByIdPatientWithDependencies(String idPatient, String dependency);

	public List<ChuTumeur> findAsListWithDependencies(Integer idTumeur);

	public List<ChuTumeur> findInAttributesWithDependencies(String text);

	public List<ChuTumeur> list();

	public List<ChuTumeur> listWithDependencies();

	public void save(ChuTumeur tumeur);

	public void update(ChuTumeur tumeur);
	
	public Long count();
}
