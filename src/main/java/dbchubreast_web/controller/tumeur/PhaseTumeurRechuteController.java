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
package dbchubreast_web.controller.tumeur;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import dbchubreast_web.controller.BaseController;
import dbchubreast_web.entity.ChuEvolution;
import dbchubreast_web.entity.ChuMetastase;
import dbchubreast_web.entity.ChuPatient;
import dbchubreast_web.entity.ChuPerformanceStatus;
import dbchubreast_web.entity.ChuPhaseTumeur;
import dbchubreast_web.entity.ChuPrelevement;
import dbchubreast_web.entity.ChuTopographie;
import dbchubreast_web.entity.ChuTraitement;
import dbchubreast_web.entity.ChuTumeur;
import dbchubreast_web.form.tumeur.FormPhaseRechute;
import dbchubreast_web.service.business.AppLogService;
import dbchubreast_web.service.business.ChuEvolutionService;
import dbchubreast_web.service.business.ChuMetastaseService;
import dbchubreast_web.service.business.ChuPatientService;
import dbchubreast_web.service.business.ChuPerformanceStatusService;
import dbchubreast_web.service.business.ChuPhaseTumeurService;
import dbchubreast_web.service.business.ChuPrelevementService;
import dbchubreast_web.service.business.ChuTopographieService;
import dbchubreast_web.service.business.ChuTraitementService;
import dbchubreast_web.service.business.ChuTumeurService;
import dbchubreast_web.service.form.tumeur.FormPhaseRechuteService;
import dbchubreast_web.validator.FormPhaseRechuteValidator;

@Controller
public class PhaseTumeurRechuteController extends BaseController {

	@Autowired
	private ChuPatientService patientService;

	@Autowired
	private ChuTumeurService tumeurService;

	@Autowired
	private ChuTraitementService traitementService;

	@Autowired
	private ChuPrelevementService prelevementService;

	@Autowired
	private ChuPhaseTumeurService phaseTumeurService;

	@Autowired
	private ChuTopographieService topographieService;

	@Autowired
	private ChuEvolutionService evolutionService;

	@Autowired
	private ChuMetastaseService metastaseService;

	@Autowired
	private ChuPerformanceStatusService performanceStatusService;

	@Autowired
	private FormPhaseRechuteService formPhaseRechuteService;

	@Autowired
	private FormPhaseRechuteValidator formPhaseRechuteValidator;
	
	@Autowired
	private AppLogService logService;

	
	/** ====================================================================================== */

	@RequestMapping(value = "/tumeur/{idTumeur}/rechute/add", method = RequestMethod.GET)
	public String showAddRelapseForm(Model model, @PathVariable Integer idTumeur, HttpServletRequest request) {

		logService.log("Affichage d'un formulaire pour ajouter une phase de rechute à la tumeur " + idTumeur);

		ChuPatient patient = patientService.find(idTumeur);
		ChuTumeur tumeur = tumeurService.find(idTumeur);

		if (tumeur != null) {

			FormPhaseRechute formPhaseRechute = formPhaseRechuteService.createNewFormPhaseRechute(tumeur);
			
			model.addAttribute("formPhaseRechute", formPhaseRechute);
			model.addAttribute("tumeur", tumeur);

			this.populateAddTumorForm(patient, model);
			
			return "tumeur/formPhaseRechute";
		}

		// POST/REDIRECT/GET
		return "redirect:/tumeur";
	}

	/** ====================================================================================== */

	@RequestMapping(value = "/rechute/{idPhase}/update", method = RequestMethod.GET)
	public String showUpdateRelapseForm(Model model, @PathVariable Integer idPhase, HttpServletRequest request) {

		logService.log("Affichage d'un formulaire pour modifier la phase de rechute " + idPhase);

		ChuPhaseTumeur phase = phaseTumeurService.findWithDependencies(idPhase);
		ChuTumeur tumeur = tumeurService.findByIdPhaseWithDependencies(idPhase);
		ChuPatient patient = patientService.find(tumeur.getIdTumeur());
		FormPhaseRechute formPhaseRechute = formPhaseRechuteService.getFormPhaseRechute(phase);

		model.addAttribute("formPhaseRechute", formPhaseRechute);
		model.addAttribute("tumeur", tumeur);

		this.populateAddTumorForm(patient, model);

		return "tumeur/formPhaseRechute";
	}

	/** ====================================================================================== */

	@RequestMapping(value = "/rechute/update", method = RequestMethod.GET)
	public String redirectRelapse() {
		// POST/REDIRECT/GET
		return "redirect:/tumeur";
	}

	
	
	/** ====================================================================================== */


	@RequestMapping(value = "/rechute/update", method = RequestMethod.POST)
	public String saveOrUpdateRelapseForm(Model model,
			@ModelAttribute("formPhaseRechute") @Valid FormPhaseRechute formPhaseRechute, BindingResult result,
			@RequestParam(value = "button", required = false) String button,
			final RedirectAttributes redirectAttributes, HttpServletRequest request) {

		// === Bouton "reinitialiser" ===

		if (button != null && button.equals("reset")) {
			if (formPhaseRechute!=null && formPhaseRechute.getIdPhase()!=null) {
				return "redirect:/rechute/" + formPhaseRechute.getIdPhase() + "/update";
			}
			else {
				return "redirect:/tumeur/" + formPhaseRechute.getIdTumeur() + "/rechute/add";
			}

		}


		// === Bouton "enregistrer" ===

		if (button != null && button.equals("save")) {

			formPhaseRechuteValidator.validate(formPhaseRechute, result);

			if (result.hasErrors()) {
				logService.log("Modification échouée de la phase rechute");
				ChuPatient patient = patientService.find(formPhaseRechute.getIdPatient());
				ChuTumeur tumeur = tumeurService.find(formPhaseRechute.getIdTumeur());
				model.addAttribute("formPhaseRechute", formPhaseRechute);
				model.addAttribute("tumeur", tumeur);
				this.populateAddTumorForm(patient, model);
				return "tumeur/formPhaseRechute";
			} 

			else {

				logService.log("Modification validée de la phase rechute");

				redirectAttributes.addFlashAttribute("css", "success");
				if (formPhaseRechute.isNew()) {
					redirectAttributes.addFlashAttribute("msg", "Une nouvelle rechute a été ajoutée avec succès !");
				} 
				else {
					redirectAttributes.addFlashAttribute("msg", "La modification de la rechute "
							+ formPhaseRechute.getIdPhase() + " a été effectuée avec succès !");
				}

				formPhaseRechuteService.saveOrUpdateForm(formPhaseRechute);				
			}
		}


		// POST/REDIRECT/GET
		return "redirect:/tumeur/" + formPhaseRechute.getIdTumeur();

	}

	/** ====================================================================================== */


	@RequestMapping(value = { "/rechute/{idPhase}/delete" }, method =  {RequestMethod.GET, RequestMethod.POST})
	public String deleteRechute(Model model, 
			@PathVariable Integer idPhase, 
			@RequestParam(value = "button", required = false) String button,
			final RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		//Tumeur
		ChuTumeur tumeur = tumeurService.findByIdPhaseWithDependencies(idPhase);
		model.addAttribute("tumeur", tumeur);

		//Phase tumeur
		ChuPhaseTumeur phaseTumeur = phaseTumeurService.find(idPhase); 
		model.addAttribute("phase", phaseTumeur);

		// Patient
		ChuPatient patient = patientService.find(tumeur.getIdTumeur());
		model.addAttribute("patient", patient);

		// Prelevements
		List<ChuPrelevement> listPrelevements = prelevementService.listByIdPhase(idPhase);
		model.addAttribute("listPrelevements", listPrelevements);

		// Traitements 
		List<ChuTraitement> listTraitements = traitementService.listByIdPhase(idPhase);
		model.addAttribute("listTraitements", listTraitements);

		if (button!=null && button.equals("delete")) {
			
			if (phaseTumeur.getChuTypePhase().getIdTypePhase() == 2) {
				phaseTumeurService.deleteWithDependencies(phaseTumeur);
				redirectAttributes.addFlashAttribute("css", "success");
				redirectAttributes.addFlashAttribute("msg", "La rechute " + idPhase + " a été supprimé !");
			}	
			else {
				redirectAttributes.addFlashAttribute("css", "danger");
				redirectAttributes.addFlashAttribute("msg", "La phase de tumeur " + idPhase + " ne peut pas être supprimée");
			}
			return "redirect:/tumeur/" + tumeur.getIdTumeur()+"/";
			
		}

		else if (button!=null && button.equals("cancel")) {
			return "redirect:/tumeur/" + tumeur.getIdTumeur()+"/";
		}

		else {
			return "tumeur/deleteRechute";
		}
	}

	
	
	/** ====================================================================================== */

	public void populateAddTumorForm(ChuPatient patient, Model model) {

		List<String> listIdGroupeTopo = new ArrayList<String>();
		listIdGroupeTopo.add("C50");
		listIdGroupeTopo.add("C50R");

		List<ChuTopographie> listTopographiesRechute = topographieService.list(listIdGroupeTopo);
		List<ChuTumeur> listTumeurs = tumeurService.listByIdPatient(patient.getIdPatient());
		List<ChuEvolution> listEvolutions = evolutionService.list();
		List<ChuMetastase> listMetastases = metastaseService.list();
		List<ChuPerformanceStatus> listPerformanceStatus = performanceStatusService.list();

		model.addAttribute("patient", patient);
		model.addAttribute("listTumeurs", listTumeurs);
		model.addAttribute("listTopographiesRechute", listTopographiesRechute);
		model.addAttribute("listEvolutions", listEvolutions);
		model.addAttribute("listMetastases", listMetastases);
		model.addAttribute("listPerformanceStatus", listPerformanceStatus);
	}

	/** ====================================================================================== */
}