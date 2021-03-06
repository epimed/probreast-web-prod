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

		<!-- Fil d'Ariane -->
		<%@ include file="../inc/filAriane.jsp"%>

		<!-- H1 Patient -->
		<%@ include file="../inc/h1Patient.jsp"%>

		<c:choose>
			<c:when test="${formTumeurInitiale['new']}">
				<h2>
					Ajouter une tumeur <small>en phase initiale</small>
				</h2>
			</c:when>
			<c:otherwise>
				<h2>
					Modifier une tumeur <small>en phase initiale</small>
				</h2>
			</c:otherwise>
		</c:choose>

		<form:form class="form-horizontal" method="post"
			modelAttribute="formTumeurInitiale"
			action="${pageContext.request.contextPath}/tumeur/update">

			<form:hidden path="idTumeur" />
			<form:hidden path="idPatient" />
			<form:hidden path="idPhase" />
			<form:hidden path="dateDeces" />

			<!-- Date de diagnostic -->
			<spring:bind path="dateDiagnostic">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Date du diagnostic *</label>
					<div class="col-sm-10">
						<form:input class="form-control" path="dateDiagnostic" type="date" />
						<form:errors path="dateDiagnostic" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<!-- Age au diagnostic -->
			<spring:bind path="ageDiagnostic">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Age au diagnostic</label>
					<div class="col-sm-1">
						<form:input class="form-control" path="ageDiagnostic" type="text" />
					</div>
					<div class="col-sm-9">
						<form:errors path="ageDiagnostic" class="control-label" />
						<span id="helpBlock" class="help-block">L'age est � saisir
							uniquement si la date du diagnostic et/ou la date de naissance ne
							sont pas connues. Si les deux dates sont renseign�es, l'age au
							diagnostic sera calcul� automatiquement.</span>
					</div>
				</div>
			</spring:bind>

			<!-- IMC au diagnostic -->
			<div class="form-group">
				<label class="col-sm-2 control-label">IMC au diagnostic</label>

				<div class="col-sm-1">
					Poids (kg)
					<form:input class="form-control" path="poids" type="text" />
					<form:errors path="poids" class="text-danger" />
				</div>

				<div class="col-sm-1">
					Taille (m)
					<form:input class="form-control" path="taille" type="text" />
					<form:errors path="taille" class="text-danger" />
				</div>
				<div class="col-sm-2">
					IMC
					<form:input class="form-control" path="imcDiagnostic" type="text" />
					<form:errors path="imcDiagnostic" class="text-danger" />
				</div>
				<div class="col-sm-6">
					<span id="helpBlock" class="help-block">On peut saisir soit
						le poids et la taille, soit l'IMC. Si le poids et la taille sont
						renseign�s, l'IMC sera calcul� automatiquement. Si l'IMC est
						renseign�, la poids et la taille seront ignor�s. </span>
				</div>
			</div>


			<!-- Nature de diagnostic -->
			<spring:bind path="natureDiagnostic">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Nature de diagnostic</label>
					<div class="col-sm-10">
						<form:input class="form-control" path="natureDiagnostic"
							type="text" list="listNatureDiagnostic" />
						<datalist id="listNatureDiagnostic">
							<option value="Biopsie" />
							<option value="Mammo/Echo" />
							<option value="Mammographie" />
							<option value="TEP" />
							<option value="IRM" />
						</datalist>
						<form:errors path="natureDiagnostic" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<!-- Topographie ICD-O -->
			<div class="form-group">
				<label class="col-sm-2 control-label">Topographie ICD-O</label>
				<div class="col-sm-10">
					<form:select class="form-control" path="idTopographie">
						<form:option value="" label="--- S�lectionner ---" />
						<c:forEach var="topo" items="${listTopographies}">
							<form:option value="${topo.idTopographie}"
								label="${topo.idTopographie} - ${topo.nomFr}" />
						</c:forEach>
					</form:select>
					<form:errors path="idTopographie" class="control-label" />
				</div>
			</div>

			<!-- C�t� -->
			<div class="form-group">
				<label class="col-sm-2 control-label">C�t�</label>
				<div class="col-sm-10">
					<label class="radio-inline"> <form:radiobutton path="cote"
							value="G" /> Gauche (G)
					</label> <label class="radio-inline"> <form:radiobutton path="cote"
							value="D" /> Droit (D)
					</label>
					<form:errors path="cote" />
				</div>
			</div>

			<!-- Date de la derni�re nouvelle -->
			<spring:bind path="dateEvolution">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Derni�re nouvelle</label>
					<div class="col-sm-10">
						<form:input class="form-control" path="dateEvolution" type="date" />
						<form:errors path="dateEvolution" class="control-label" />
					</div>
				</div>
			</spring:bind>

			<!-- Statut -->
			<div class="form-group">
				<label class="col-sm-2 control-label">Statut � la derni�re
					nouvelle</label>
				<div class="col-sm-10">

					<form:select class="form-control" path="idEvolution">
						<form:option value="" label="--- S�lectionner ---" />
						<c:forEach var="evolution" items="${listEvolutions}">
							<form:option value="${evolution.idEvolution}"
								label="${evolution.nom}" />
						</c:forEach>
					</form:select>

					<form:errors path="idEvolution" class="control-label" />
				</div>
			</div>

			<!-- Profondeur -->
			<div class="form-group">
				<label class="col-sm-2 control-label">Profondeur</label>
				<div class="col-sm-3">
					<form:input class="form-control" path="profondeur" type="text" />
					<form:errors path="profondeur" />
				</div>
			</div>

			<!-- cTNM -->
			<div class="form-group">
				<label class="col-sm-2 control-label">cTNM</label>

				<div class="col-sm-1">
					cT
					<form:input class="form-control" path="cT" type="text" />
					<form:errors path="cT" class="text-danger" />
				</div>

				<div class="col-sm-1">
					cN
					<form:input class="form-control" path="cN" type="text" />
					<form:errors path="cN" class="text-danger" />
				</div>
				<div class="col-sm-1">
					cM
					<form:input class="form-control" path="cM" type="text" />
					<form:errors path="cM" class="text-danger" />
				</div>
				<div class="col-sm-2">
					c Taille (mm)
					<form:input class="form-control" path="cTaille" type="text" />
					<form:errors path="cTaille" class="text-danger" />
				</div>
				<div class="col-sm-5">
					<span id="helpBlock" class="help-block">En cas de nodules,
						noter plusieurs tailles s�par�es par le caract�re <code>/</code>,
						par exemple <code>10/5</code>.
					</span>
				</div>
			</div>

			<!-- pTNM -->
			<div class="form-group">
				<label class="col-sm-2 control-label">pTNM</label>

				<div class="col-sm-1">
					pT
					<form:input class="form-control" path="pT" type="text" />
					<form:errors path="pT" class="text-danger" />
				</div>

				<div class="col-sm-1">
					pN
					<form:input class="form-control" path="pN" type="text" />
					<form:errors path="pN" class="text-danger" />
				</div>
				<div class="col-sm-1">
					pM
					<form:input class="form-control" path="pM" type="text" />
					<form:errors path="pM" class="text-danger" />
				</div>
				<div class="col-sm-2">
					p Taille (mm)
					<form:input class="form-control" path="pTaille" type="text" />
					<form:errors path="pTaille" class="text-danger" />
				</div>
				<div class="col-sm-5">
					<span id="helpBlock" class="help-block">En cas de nodules,
						noter plusieurs tailles s�par�es par le caract�re <code>/</code>,
						par exemple <code>10/5</code>.
					</span>
				</div>
			</div>

			<!-- Metastases -->
			<div class="form-group">
				<label class="col-sm-2 control-label">M�tastases</label>
				<div class="col-sm-10">
					<c:forEach var="metastase" items="${listMetastases}">
						<label class="checkbox-inline"> <form:checkbox
								path="listIdMetastases" value="${metastase.idMetastase}" />
							${metastase.nom}
						</label>
					</c:forEach>
					<form:errors path="listIdMetastases" />
				</div>
			</div>

			<!-- Consentement -->
			<spring:bind path="consentement">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="col-sm-2 control-label">Consentement</label>
					<div class="col-sm-10">
						<label class="radio-inline"> <form:radiobutton
								path="consentement" value="true" />oui
						</label> <label class="radio-inline"> <form:radiobutton
								path="consentement" value="false" />non
						</label> <br />
						<form:errors path="consentement" class="control-label" />
					</div>
				</div>
			</spring:bind>


			<!-- Remarque -->
			<div class="form-group">
				<label class="col-sm-2 control-label">Remarque</label>
				<div class="col-sm-10">
					<form:input class="form-control" path="remarque" type="text" />
					<form:errors path="remarque" />
				</div>
			</div>


			<!-- Buttons -->
			<%@ include file="../inc/boutonsFormulaire.jsp"%>

		</form:form>

	</div>

	<!-- Footer -->
	<%@ include file="/resources/fragments/footer.jsp"%>

</body>
</html>