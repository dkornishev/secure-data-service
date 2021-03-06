/****** Script for SelectTopNRows command from SSMS  ******/
SELECT sa.StudentUSI
      ,sa.AssessmentTitle
      ,sa.AcademicSubjectTypeId
      ,sa.AssessedGradeLevelTypeId
      ,ast.CodeValue as AcademicSubject
      ,glt.CodeValue as AssessedGradeLevel
      ,sa.Version
      ,sa.AdministrationDate
      ,sa.AdministrationEndDate
      ,sa.SerialNumber
      ,lt.CodeValue as AdministrationLanguageType
      ,aet.CodeValue as AdministrationEnvironmentType
      ,rit.CodeValue as RetestIndicatorType
      ,rntt.CodeValue as ReasonNotTestedType
      ,glt.CodeValue as WhenAssessedGradeLevelType
  FROM EdFi.edfi.StudentAssessment sa
    LEFT JOIN EdFi.edfi.AcademicSubjectType ast ON sa.AcademicSubjectTypeId = ast.AcademicSubjectTypeId
	LEFT JOIN EdFi.edfi.GradeLevelType glt ON sa.AssessedGradeLevelTypeId = glt.GradeLevelTypeId
	LEFT JOIN EdFi.edfi.LanguagesType lt ON sa.AdministrationLanguageTypeId = lt.LanguageTypeId
	LEFT JOIN EdFi.edfi.AdministrationEnvironmentType aet ON sa.AdministrationEnvironmentTypeId = aet.AdministrationEnvironmentTypeId
	LEFT JOIN EdFi.edfi.RetestIndicatorType rit ON sa.RetestIndicatorTypeId = rit.RetestIndicatorTypeId
	LEFT JOIN EdFi.edfi.ReasonNotTestedType rntt ON sa.ReasonNotTestedTypeId = rntt.ReasonNotTestedTypeId
	LEFT JOIN EdFi.edfi.GradeLevelType glt1 ON sa.WhenAssessedGradeLevelTypeId = glt1.GradeLevelTypeId
  ORDER BY sa.StudentUSI, sa.AssessmentTitle, sa.AcademicSubjectTypeId, sa.AssessedGradeLevelTypeId, sa.Version, sa.AdministrationDate
