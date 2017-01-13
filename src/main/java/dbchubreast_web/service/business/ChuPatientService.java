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

import java.util.Date;
import java.util.List;

import dbchubreast_web.entity.ChuPatient;

public interface  ChuPatientService {

	public List<ChuPatient> list(); 
	
	public String getLastIdPatient(); 
	
	public ChuPatient find(String nom, String prenom, Date dateNaissance); 

	public ChuPatient find(String idPatient); 

	public Long count(); 

	public ChuPatient find(Integer idTumeur); 

	public List<ChuPatient> findInAttributes(String text); 

	public void saveOrUpdate(ChuPatient patient);

}