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

import dbchubreast_web.entity.AppUser;

public interface AppUserService {
	public AppUser findByUsername(String username);
	public AppUser findById(Integer idUser);
	public List<AppUser> list();
	public void save(AppUser user);
	public void update(AppUser user);
	public void delete(AppUser user);
}
