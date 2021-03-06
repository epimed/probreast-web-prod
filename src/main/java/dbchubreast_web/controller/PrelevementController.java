/**
 * EpiMed - Information system for bioinformatics developments in the field of epigenetics
 * 
 * This software is a computer program which performs the data management 
 * for EpiMed platform of the Institute for Advances Biosciences (IAB)
 *
 * Copyright University of Grenoble Alps (UGA)

 * Please check LICENSE file
 *
 * Author: Ekaterina Bourova-Flin 
 *
 */
package dbchubreast_web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dbchubreast_web.entity.ChuMorphologie;
import dbchubreast_web.entity.ChuPatient;
import dbchubreast_web.entity.ChuPhaseTumeur;
import dbchubreast_web.entity.ChuPrelevement;
import dbchubreast_web.entity.ChuPrelevementBiomarqueur;
import dbchubreast_web.entity.ChuTumeur;
import dbchubreast_web.entity.ChuTypePhase;
import dbchubreast_web.entity.ChuTypePrelevement;
import dbchubreast_web.form.FormPrelevement;
import dbchubreast_web.service.business.AppLogService;
import dbchubreast_web.service.business.ChuBiomarqueurService;
import dbchubreast_web.service.business.ChuMorphologieService;
import dbchubreast_web.service.business.ChuPatientService;
import dbchubreast_web.service.business.ChuPhaseTumeurService;
import dbchubreast_web.service.business.ChuPrelevementBiomarqueurService;
import dbchubreast_web.service.business.ChuPrelevementService;
import dbchubreast_web.service.business.ChuTumeurService;
import dbchubreast_web.service.business.ChuTypePhaseService;
import dbchubreast_web.service.business.ChuTypePrelevementService;
import dbchubreast_web.service.form.FormPrelevementService;
import dbchubreast_web.service.updater.UpdaterCategorie;
import dbchubreast_web.validator.FormPrelevementValidator;

@Controller
public class PrelevementController extends BaseController {

	@Autowired
	private ChuPatientService patientService;

	@Autowired
	private ChuTumeurService tumeurService;

	@Autowired
	private ChuPhaseTumeurService phaseTumeurService;

	@Autowired
	private ChuTypePhaseService typePhaseService;

	@Autowired
	private ChuPrelevementService prelevementService;

	@Autowired
	private ChuMorphologieService morphologieService;

	@Autowired
	private ChuTypePrelevementService typePrelevementService;

	@Autowired
	private ChuPrelevementBiomarqueurService prelevementBiomarqueurService;

	@Autowired
	private ChuBiomarqueurService biomarqueurService;

	@Autowired
	private FormPrelevementService formPrelevementService;

	@Autowired
	private FormPrelevementValidator formPrelevementValidator;
	
	@Autowired
	private UpdaterCategorie updaterCategorie;

	@Autowired
	private AppLogService logService;

	/** ====================================================================================== */

	@RequestMapping(value = { "/patient/{idPatient}/prelevements" }, method = RequestMethod.GET)
	public String listPrelevements(Model model, @PathVariable String idPatient, HttpServletRequest request) {

		ChuPatient patient = patientService.find(idPatient);
		model.addAttribute("patient", patient);

		List<ChuTumeur> listTumeurs = tumeurService.listByIdPatientWithDependencies(idPatient, "prelevements");
		model.addAttribute("listTumeurs", listTumeurs);

		return "prelevement/list";
	}

	/** ====================================================================================== */

	@RequestMapping(value = { "/prelevement" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String searchPrelevement(Model model, 
			@RequestParam(value = "idPatient", required = false) String idPatient,
			HttpServletRequest request) {

		logService.log("Affichage de prélèvements du patient " + idPatient);

		if (idPatient != null) {
			ChuPatient patient = patientService.find(idPatient);
			model.addAttribute("patient", patient);
			List<ChuTumeur> listTumeurs = tumeurService.listByIdPatientWithDependencies(idPatient, "prelevements");
			model.addAttribute("listTumeurs", listTumeurs);
		}

		List<ChuPatient> listPatients = patientService.list();
		;
		model.addAttribute("idPatient", idPatient);
		model.addAttribute("listPatients", listPatients);

		return "prelevement/search";
	}

	/** ====================================================================================== */

	@RequestMapping(value = { "/prelevement/{idPrelevement}" }, method = { RequestMethod.GET, RequestMethod.POST })
	public String showPrelevement(Model model, @PathVariable Integer idPrelevement, HttpServletRequest request) {

		logService.log("Affichage du prélèvement " + idPrelevement);

		// Patient
		ChuPatient patient = patientService.findByIdPrelevement(idPrelevement);
		model.addAttribute("patient", patient);

		// Prelevements
		List<ChuPrelevement> listPrelevements = prelevementService.listByIdPatient(patient.getIdPatient());
		model.addAttribute("listPrelevements", listPrelevements);

		// Prelevement
		ChuPrelevement prelevement = prelevementService.find(idPrelevement);
		model.addAttribute("prelevement", prelevement);
		this.populatePrelevementBiomarqueurs(model, prelevement);

		return "prelevement/show";
	}

	/** ====================================================================================== */

	@RequestMapping(value = { "/prelevement/{idPrelevement}/update" }, method = RequestMethod.GET)
	public String showUpdateSampleForm(Model model, @PathVariable Integer idPrelevement, HttpServletRequest request) {

		logService.log("Affichage d'un formulaire pour modifier le prélèvement " + idPrelevement);

		// Form prelevement
		ChuPrelevement prelevement = prelevementService.find(idPrelevement);
		FormPrelevement formPrelevement = formPrelevementService.getForm(prelevement);
		this.populateAddSampleForm(formPrelevement, model);

		return "prelevement/form";
	}

	/** ====================================================================================== */

	@RequestMapping(value = { "/patient/{idPatient}/prelevement/add" }, method = RequestMethod.GET)
	public String showAddSampleForm(Model model, 
			@PathVariable String idPatient,
			@RequestParam(value = "idTumeur", required = false) Integer idTumeur,
			@RequestParam(value = "idPhase", required = false) Integer idPhase,
			HttpServletRequest request) {

		logService.log("Affichage d'un formulaire pour ajouter un prélèvement au patient " + idPatient);

		// Form prelevement
		FormPrelevement formPrelevement = new FormPrelevement(idPatient);
		formPrelevement.init(biomarqueurService.list());

		if (idTumeur!=null) {
			formPrelevement.setIdTumeur(idTumeur);
		}
		if (idPhase!=null) {
			formPrelevement.setIdPhase(idPhase);
		}

		this.populateAddSampleForm(formPrelevement, model);

		return "prelevement/form";
	}

	/** ====================================================================================== */

	@RequestMapping(value = { "/prelevement/update" }, method = RequestMethod.GET)
	public String redirectSampleForm() {

		// POST/REDIRECT/GET
		return "redirect:/prelevement";
	}

	/** ====================================================================================== */

	@RequestMapping(value = { "/prelevement/update" }, method = RequestMethod.POST)
	public String showDetailAddSampleForm(Model model,
			@RequestParam(value = "button", required = false) String button,
			@ModelAttribute("formPrelevement") FormPrelevement formPrelevement, BindingResult result,
			final RedirectAttributes redirectAttributes, HttpServletRequest request) {

		// === Mise a jour de la forme sans bouton ===

		if (button == null) {
			this.populateAddSampleForm(formPrelevement, model);
			return "prelevement/form";
		}

		// === Bouton "reinitialiser" ===

		if (button != null && button.equals("reset")) {
			if (formPrelevement!=null && formPrelevement.getIdPrelevement()!=null) {
				return "redirect:/prelevement/" + formPrelevement.getIdPrelevement() + "/update";
			}
			else {
				return "redirect:/patient/" + formPrelevement.getIdPatient() + "/prelevement/add";
			}
		}

		// === Bouton "enregistrer" ===

		if (button != null && button.equals("save")) {

			formPrelevementValidator.validate(formPrelevement, result);

			if (result.hasErrors()) {
				logService.log("Modification échouée de prélèvement ");
				this.populateAddSampleForm(formPrelevement, model);
				return "prelevement/form";
			}

			else {
				logService.log("Modification validée de prélèvement ");
				redirectAttributes.addFlashAttribute("css", "success");
				if (formPrelevement.isNew()) {
					redirectAttributes.addFlashAttribute("msg", "Un nouveau prélèvement a été ajouté avec succès !");
				} else {
					redirectAttributes.addFlashAttribute("msg",
							"La modification du prélèvement a été effectuée avec succès !");
				}
				formPrelevementService.saveOrUpdateForm(formPrelevement);
			}

		}

		// === Bouton "annuler" ===

		if (button != null && button.equals("cancel")) {
			return "redirect:/patient/" + formPrelevement.getIdPatient() + "/prelevements";
		}


		// POST/REDIRECT/GET
		return "redirect:/prelevement/" + formPrelevement.getIdPrelevement();

	}
	
	/** ====================================================================================== */

	@RequestMapping(value = { "/prelevement/{idPrelevement}/delete" }, method = {RequestMethod.GET, RequestMethod.POST})
	public String deletePrelevement(Model model, 
			@PathVariable Integer idPrelevement, 
			@RequestParam(value = "button", required = false) String button,
			final RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		

		// Patient
		ChuPatient patient = patientService.findByIdPrelevement(idPrelevement);
		model.addAttribute("patient", patient);

		// Prelevements
		List<ChuPrelevement> listPrelevements = prelevementService.listByIdPatient(patient.getIdPatient());
		model.addAttribute("listPrelevements", listPrelevements);

		// Prelevement
		ChuPrelevement prelevement = prelevementService.find(idPrelevement);
		model.addAttribute("prelevement", prelevement);

		if (button!=null && button.equals("delete")) {
			ChuTumeur tumeur = prelevement.getChuPhaseTumeur().getChuTumeur();
			prelevementService.delete(prelevement);
			updaterCategorie.update(tumeur);
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Le prelevement " + idPrelevement + " a été supprimé !");
			logService.log("Suppression du prélèvement " + idPrelevement);
			return "redirect:/patient/" + patient.getIdPatient() + "/prelevements";
		}

		if (button!=null && button.equals("cancel")) {
			return "redirect:/patient/" + patient.getIdPatient() + "/prelevements";
		}

		return "prelevement/delete";
	}


	/** ====================================================================================== */

	private void populatePrelevementBiomarqueurs(Model model, ChuPrelevement prelevement) {
		List<ChuPrelevementBiomarqueur> listPrelevementBiomarqueurs = prelevementBiomarqueurService
				.list(prelevement.getIdPrelevement());
		model.addAttribute("listPrelevementBiomarqueurs", listPrelevementBiomarqueurs);
	}
	
	/** ====================================================================================== */

	private void populateAddSampleForm(FormPrelevement formPrelevement, Model model) {

		// Patient
		ChuPatient patient = patientService.find(formPrelevement.getIdPatient());
		model.addAttribute("patient", patient);

		// Tumeurs
		List<ChuTumeur> listTumeurs = tumeurService.listByIdPatient(formPrelevement.getIdPatient());
		model.addAttribute("listTumeurs", listTumeurs);

		// Tumeur pre-selectionnee si unique
		if (formPrelevement.getIdTumeur() == null && listTumeurs != null && listTumeurs.size() == 1) {
			formPrelevement.setIdTumeur(listTumeurs.get(0).getIdTumeur());
		}

		// Phases tumeur
		if (formPrelevement.getIdTumeur() != null) {
			List<ChuPhaseTumeur> listPhases = phaseTumeurService.listWithDependencies(formPrelevement.getIdTumeur());
			model.addAttribute("listPhases", listPhases);

			// Phase pre-selectionnee si unique
			if (formPrelevement.getIdPhase() == null && listPhases != null && listPhases.size() == 1) {
				formPrelevement.setIdPhase(listPhases.get(0).getIdPhase());
			}
			
			// Metastases
			if (formPrelevement.getIdPhase() != null) {
				ChuPhaseTumeur phase =  phaseTumeurService.findWithDependencies(formPrelevement.getIdPhase());
				model.addAttribute("listMetastases", phase.getChuMetastases());
			}
			
		}

		// Types de prelevements
		if (formPrelevement.getIdPhase() != null) {
			ChuTypePhase typePhase = typePhaseService.findByIdPhase(formPrelevement.getIdPhase());
			List<ChuTypePrelevement> listTypePrelevements = null;
			if (typePhase != null && typePhase.getIdTypePhase() == 1) {
				listTypePrelevements = typePrelevementService.listPhaseInitiale();
			}
			if (typePhase != null && typePhase.getIdTypePhase() == 2) {
				listTypePrelevements = typePrelevementService.listPhaseRechute();
			}
			model.addAttribute("listTypePrelevements", listTypePrelevements);

			// Type de prelevement pre-selectionne si unique

			if (formPrelevement.getIdTypePrelevement() == null && listTypePrelevements != null
					&& listTypePrelevements.size() == 1) {
				formPrelevement.setIdTypePrelevement(listTypePrelevements.get(0).getIdTypePrelevement());
			}
		}

		// Prelevements
		List<ChuPrelevement> listPrelevements = prelevementService.listByIdPatient(patient.getIdPatient());
		model.addAttribute("listPrelevements", listPrelevements);

		// Morphologies
		List<ChuMorphologie> listMorphologies = morphologieService.list();
		model.addAttribute("listMorphologies", listMorphologies);

		// Form
		model.addAttribute("formPrelevement", formPrelevement);
	}

	/** ====================================================================================== */

}
