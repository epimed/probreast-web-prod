<%@ include file="/resources/fragments/jstlTags.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>

<!-- Header -->
<%@ include file="/resources/fragments/header.jsp"%>

</head>

<body>

	<!-- Navigation bar -->
	<%@ include file="/resources/fragments/navbar.jsp"%>

	<!-- Container -->
	<div class="container">

		<c:choose>
			<c:when test="${formPatient['new']}">
				<h1>Ajouter un patient</h1>
			</c:when>
			<c:otherwise>
				<h1>Modifier un patient</h1>
			</c:otherwise>
		</c:choose>

		<form:form class="form-horizontall" method="post"
			modelAttribute="formPatient"
			action="${pageContext.request.contextPath}/patient/update">

			<form:hidden path="idPatient" />

			<spring:bind path="tk">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-2 control-label">Num�ro TK</label>
					<div class="col-10">
						<form:input path="tk" type="text" class="form-control " id="tk"
							placeholder="TK" />
						<form:errors path="tk" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="rcp">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-2 control-label">Num�ro RCP</label>
					<div class="col-10">
						<form:input path="rcp" type="text" class="form-control " id="rcp"
							placeholder="RCP" />
						<form:errors path="rcp" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="prenom">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-2 control-label required-field">Pr�nom *</label>
					<div class="col-10">
						<form:input path="prenom" type="text" class="form-control "
							id="prenom" placeholder="Pr�nom" />
						<form:errors path="prenom" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="nom">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-2 control-label required-field">Nom *</label>
					<div class="col-10">
						<form:input path="nom" type="text" class="form-control " id="nom"
							placeholder="NOM" />
						<form:errors path="nom" class="control-label" />
					</div>
				</div>
			</spring:bind>
			
			<spring:bind path="nomNaissance">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-2 control-label">Nom de naissance (si diff�rent)</label>
					<div class="col-10">
						<form:input path="nomNaissance" type="text" class="form-control " id="nomNaissance"
							placeholder="NOM DE NAISSANCE" />
						<form:errors path="nomNaissance" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="sexe">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-2 control-label required-field">Sexe *</label>
					<div class="col-10">
						<label class="radio-inline"> <form:radiobutton path="sexe"
								value="F" />Femme
						</label> <label class="radio-inline"> <form:radiobutton
								path="sexe" value="M" />Homme
						</label> <br />
						<form:errors path="sexe" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="dateNaissance">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-2 control-label required-field">Date de
						naissance *</label>
					<div class="col-10">
						<form:input path="dateNaissance" type="date" class="form-control "
							id="dateNaissance" placeholder="Date de naissance" />
						<form:errors path="dateNaissance" class="control-label" />
					</div>
				</div>
			</spring:bind>


			<spring:bind path="dateDeces">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-2 control-label">Date de d�c�s</label>
					<div class="col-10">
						<form:input path="dateDeces" type="date" class="form-control "
							id="dateDeces" placeholder="Date de d�c�s" />
						<form:errors path="dateDeces" class="control-label" />
					</div>
				</div>
			</spring:bind>


			<spring:bind path="causeDeces">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-2 control-label">Cause de d�c�s</label>
					<div class="col-10">
						<form:select class="form-control" path="causeDeces">
							<form:option value="" label="--- S�lectionner ---" />
							<form:option value="Suites n�oplasiques" />
							<form:option value="Autre cancer" />
							<form:option value="Autre cancer du sein" />
							<form:option value="Multiples cancers dont le sein" />
							<form:option value="Maladie intercurrente (ou suicide)" />
							<form:option value="Complications li�es au traitement" />
							<form:option value="Cause inconnue" />
						</form:select>
						<form:errors path="causeDeces" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<spring:bind path="statutBrca">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-2 control-label">Statut BRCA</label>
					<div class="col-10">
						<form:select path="statutBrca" class="form-control">
							<form:option value="" label="--- S�lectionner ---" />
							<form:option value="Non mut�" />
							<form:option value="BRCA1" />
							<form:option value="BRCA2" />
						</form:select>
						<form:errors path="statutBrca" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<!-- Buttons -->
			<%@ include file="../inc/boutonsFormulaire.jsp"%>



		</form:form>

	</div>

	<!-- Footer -->
	<%@ include file="/resources/fragments/footer.jsp"%>

</body>
</html>