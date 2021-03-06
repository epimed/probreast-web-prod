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
package dbchubreast_web.service.form.tumeur;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dbchubreast_web.dao.ChuEvolutionDao;
import dbchubreast_web.dao.ChuMetastaseDao;
import dbchubreast_web.dao.ChuPatientDao;
import dbchubreast_web.dao.ChuPhaseTumeurDao;
import dbchubreast_web.dao.ChuTnmDao;
import dbchubreast_web.dao.ChuTopographieDao;
import dbchubreast_web.dao.ChuTumeurDao;
import dbchubreast_web.dao.ChuTypePhaseDao;
import dbchubreast_web.entity.ChuEvolution;
import dbchubreast_web.entity.ChuMetastase;
import dbchubreast_web.entity.ChuPatient;
import dbchubreast_web.entity.ChuPhaseTumeur;
import dbchubreast_web.entity.ChuTnm;
import dbchubreast_web.entity.ChuTopographie;
import dbchubreast_web.entity.ChuTumeur;
import dbchubreast_web.entity.ChuTypePhase;
import dbchubreast_web.form.tumeur.FormTumeurInitiale;
import dbchubreast_web.service.BaseService;
import dbchubreast_web.service.updater.UpdaterNodule;
import dbchubreast_web.service.updater.UpdaterSurvival;
import dbchubreast_web.service.util.FormatService;

@Service
public class FormPhaseTumeurInitialeServiceImpl extends BaseService implements FormPhaseTumeurInitialeService {

	@Autowired
	private ChuPatientDao patientService;

	@Autowired
	private ChuPhaseTumeurDao phaseTumeurDao;

	@Autowired
	private ChuTumeurDao tumeurDao;

	@Autowired
	private ChuTypePhaseDao typePhaseDao;

	@Autowired
	private ChuTopographieDao topographieDao;

	@Autowired
	private ChuEvolutionDao evolutionDao;

	@Autowired
	private ChuMetastaseDao metastaseDao;

	@Autowired
	private ChuTnmDao tnmDao;

	@Autowired
	private FormatService formatService;

	@Autowired
	private UpdaterSurvival updaterSurvival;

	@Autowired
	private UpdaterNodule updaterNodule;

	/** =================================================================== */

	public void saveOrUpdateForm(FormTumeurInitiale form) {

		logger.debug("=== " + this.getClass().getName() + " saveOrUpdate()" + " ===");

		ChuTumeur tumeur = null;
		ChuPhaseTumeur phaseTumeur = null;

		if (form.isNew()) {
			tumeur = new ChuTumeur();
			phaseTumeur = new ChuPhaseTumeur();
			tumeur.getChuPhaseTumeurs().add(phaseTumeur);
			phaseTumeur.setChuTumeur(tumeur);
		} else {
			tumeur = tumeurDao.findWithDependencies(form.getIdTumeur());
			phaseTumeur = phaseTumeurDao.find(form.getIdPhase());
		}

		// === Patient ===

		ChuPatient patient = patientService.find(form.getIdPatient());
		tumeur.setChuPatient(patient);

		// === Tumeur ===

		tumeur.setIdTumeur(form.getIdTumeur());
		tumeur.setDateDiagnostic(form.getDateDiagnostic());
		tumeur.setConsentement(form.getConsentement());

		// Age diagnostic

		Date dateNaissance = patient.getDateNaissance();
		Date dateDiagnostic = tumeur.getDateDiagnostic();

		if (dateDiagnostic != null && dateNaissance != null) {
			tumeur.setAgeDiagnostic(formatService.calculateAge(dateNaissance, dateDiagnostic));
		} else {
			tumeur.setAgeDiagnostic(form.getAgeDiagnostic());
		}

		// IMC diagnostic

		if (form.getImcDiagnostic()!=null) {
			tumeur.setImcDiagnostic(form.getImcDiagnostic());
		}
		else {
			tumeur.setImcDiagnostic(formatService.calculateImc(form.getTaille(), form.getPoids()));
		}

		tumeur.setCote(form.getCote());

		// === Evolution / date derniere nouvelle ===

		patient.setDateEvolution(form.getDateEvolution());
		ChuEvolution evolution = evolutionDao.find(form.getIdEvolution());
		tumeur.setChuEvolution(evolution);

		// === Phase tumeur ===

		phaseTumeur.setDateDiagnostic(form.getDateDiagnostic());
		phaseTumeur.setNatureDiagnostic(form.getNatureDiagnostic());
		phaseTumeur.setProfondeur(form.getProfondeur());

		ChuTopographie topographie = topographieDao.find(form.getIdTopographie());
		phaseTumeur.setChuTopographie(topographie);

		// === Type phase ===

		ChuTypePhase typePhase = typePhaseDao.find(form.getIdTypePhase());
		phaseTumeur.setChuTypePhase(typePhase);

		// === cTNM ===

		ChuTnm cTnm = tnmDao.find(phaseTumeur.getIdPhase(), "c");
		if (cTnm == null) {
			cTnm = new ChuTnm();
			cTnm.setType("c");
			cTnm.setChuPhaseTumeur(phaseTumeur);
		}
		cTnm.setT(form.getcT());
		cTnm.setN(form.getcN());
		cTnm.setM(form.getcM());
		cTnm.setTaille(form.getcTaille());

		// === pTNM ===

		ChuTnm pTnm = tnmDao.find(phaseTumeur.getIdPhase(), "p");
		if (pTnm == null) {
			pTnm = new ChuTnm();
			pTnm.setType("p");
			pTnm.setChuPhaseTumeur(phaseTumeur);
		}
		pTnm.setT(form.getpT());
		pTnm.setN(form.getpN());
		pTnm.setM(form.getpM());
		pTnm.setTaille(form.getpTaille());

		// === Metastases ===

		this.addMetastases(phaseTumeur, form.getListIdMetastases());

		// === Remarque ===

		phaseTumeur.setRemarque(form.getRemarque());

		logger.debug("Tumeur {}", tumeur);
		logger.debug("Phase tumeur {}", phaseTumeur);

		// ====== Save or Update =======

		if (form.isNew()) {
			// Save new

			logger.debug("Save new");

			tumeurDao.save(tumeur);
			phaseTumeurDao.save(phaseTumeur);

			form.setIdTumeur(tumeur.getIdTumeur());
			form.setIdPhase(phaseTumeur.getIdPhase());

			if (!this.isEmptyTnm(cTnm)) {
				logger.debug("Saving cTnm {}", cTnm);
				tnmDao.save(cTnm);
			}
			if (!this.isEmptyTnm(pTnm)) {
				logger.debug("Saving pTnm {}", pTnm);
				tnmDao.save(pTnm);
			}
		} 
		else {

			// update existing

			logger.debug("Update existing");

			tumeurDao.update(tumeur);
			phaseTumeurDao.saveOrUpdate(phaseTumeur);

			if (!this.isEmptyTnm(cTnm)) {
				tnmDao.saveOrUpdate(cTnm);
			}

			if (!this.isEmptyTnm(pTnm)) {
				tnmDao.saveOrUpdate(pTnm);
			}

		}

		patientService.update(patient);
		
		// ====== UPDATE DEPENDENCIES =====

		this.updateDependencies(tumeur, phaseTumeur);
	}

	/** =================================================================== */

	public FormTumeurInitiale getFormTumeurInitiale(ChuTumeur tumeur) {

		logger.debug("=== " + this.getClass().getName() + " getFormTumeurInitiale()" + " ===");

		ChuPatient patient = tumeur.getChuPatient();
		FormTumeurInitiale formTumeurInitiale = new FormTumeurInitiale(patient.getIdPatient(), patient.getDateDeces());

		// === Tumeur ===

		formTumeurInitiale.setIdTumeur(tumeur.getIdTumeur());
		formTumeurInitiale.setDateDiagnostic(tumeur.getDateDiagnostic());
		formTumeurInitiale.setAgeDiagnostic(tumeur.getAgeDiagnostic());
		formTumeurInitiale.setImcDiagnostic(tumeur.getImcDiagnostic());

		formTumeurInitiale.setCote(tumeur.getCote());
		formTumeurInitiale.setConsentement(tumeur.getConsentement());

		// === Evolution ===
		formTumeurInitiale.setDateEvolution(patient.getDateEvolution());
		formTumeurInitiale
		.setIdEvolution(tumeur.getChuEvolution() == null ? null : tumeur.getChuEvolution().getIdEvolution());

		logger.debug("Date deces {}", formTumeurInitiale.getDateDeces());

		// === Phase ===

		List<ChuPhaseTumeur> listPhases = phaseTumeurDao.list(tumeur.getIdTumeur(), 1);
		ChuPhaseTumeur phase = listPhases.get(0);
		formTumeurInitiale.setIdPhase(phase.getIdPhase());
		formTumeurInitiale.setNatureDiagnostic(phase.getNatureDiagnostic());
		formTumeurInitiale.setProfondeur(phase.getProfondeur());

		formTumeurInitiale.setIdTopographie(
				phase.getChuTopographie() == null ? null : phase.getChuTopographie().getIdTopographie());

		// === cTNM ===

		ChuTnm cTnm = tnmDao.find(phase.getIdPhase(), "c");
		if (cTnm == null) {
			cTnm = new ChuTnm();
		}
		formTumeurInitiale.setcT(cTnm.getT());
		formTumeurInitiale.setcN(cTnm.getN());
		formTumeurInitiale.setcM(cTnm.getM());
		formTumeurInitiale.setcTaille(cTnm.getTaille());

		// === pTNM ===

		ChuTnm pTnm = tnmDao.find(phase.getIdPhase(), "p");
		if (pTnm == null) {
			pTnm = new ChuTnm();
		}
		formTumeurInitiale.setpT(pTnm.getT());
		formTumeurInitiale.setpN(pTnm.getN());
		formTumeurInitiale.setpM(pTnm.getM());
		formTumeurInitiale.setpTaille(pTnm.getTaille());

		// === Metastases ===

		List<ChuMetastase> listMetastases = metastaseDao.list(phase.getIdPhase());
		for (ChuMetastase metastase : listMetastases) {
			formTumeurInitiale.getListIdMetastases().add(metastase.getIdMetastase());
		}

		formTumeurInitiale.setRemarque(phase.getRemarque());

		return formTumeurInitiale;
	}


	/** =================================================================== */

	private void updateDependencies(ChuTumeur tumeur, ChuPhaseTumeur phaseTumeur) {

		// ====== UPDATE DEPENDENCIES =====

		// === Survival ===
		List<ChuTumeur> listTumeurs = new ArrayList<ChuTumeur>();
		listTumeurs.add(tumeur);
		updaterSurvival.update(listTumeurs);


		// === Update nodules ===
		updaterNodule.update(phaseTumeur);
	}

	/** =================================================================== */

	private boolean isEmptyTnm(ChuTnm tnm) {
		return tnm.getIdTnm() == null && tnm.getT() == null && tnm.getN() == null && tnm.getM() == null
				&& tnm.getTaille() == null;
	}

	/** =================================================================== */

	private void addMetastases(ChuPhaseTumeur phaseTumeur, List<Integer> listIdMetastases) {

		if (listIdMetastases != null && !listIdMetastases.isEmpty()) {
			List<ChuMetastase> metastases = metastaseDao.list(listIdMetastases);
			phaseTumeur.setChuMetastases(metastases);
			phaseTumeur.setMetastases(true);
		} else {
			phaseTumeur.setMetastases(false);
			phaseTumeur.setChuMetastases(null);
		}

	}

	/** =================================================================== */

}
