/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.05.31 at 09:35:49 AM EDT 
//


package org.slc.sli.test.edfi.entities;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This entity holds a student's grade or competency level for a GradeBookEntry.
 * 
 * <p>Java class for StudentGradebookEntry complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentGradebookEntry">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="DateFulfilled" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="LetterGradeEarned" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://ed-fi.org/0100}GradeEarned">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="NumericGradeEarned" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CompetencyLevel" type="{http://ed-fi.org/0100}CompetencyLevelDescriptorType" minOccurs="0"/>
 *         &lt;element name="DiagnosticStatement" type="{http://ed-fi.org/0100}text" minOccurs="0"/>
 *         &lt;element name="StudentSectionAssociationReference" type="{http://ed-fi.org/0100}StudentSectionAssociationReferenceType"/>
 *         &lt;element name="GradebookEntryReference" type="{http://ed-fi.org/0100}ReferenceType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentGradebookEntry", propOrder = {
    "dateFulfilled",
    "letterGradeEarned",
    "numericGradeEarned",
    "competencyLevel",
    "diagnosticStatement",
    "studentSectionAssociationReference",
    "gradebookEntryReference"
})
@XmlRootElement(name = "StudentGradebookEntry")
public class StudentGradebookEntry
    extends ComplexObjectType
{

    @XmlElement(name = "DateFulfilled")
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String dateFulfilled;
    @XmlElement(name = "LetterGradeEarned")
    protected String letterGradeEarned;
    @XmlElement(name = "NumericGradeEarned")
    protected BigInteger numericGradeEarned;
    @XmlElement(name = "CompetencyLevel")
    protected CompetencyLevelDescriptorType competencyLevel;
    @XmlElement(name = "DiagnosticStatement")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String diagnosticStatement;
    @XmlElement(name = "StudentSectionAssociationReference", required = true)
    protected StudentSectionAssociationReferenceType studentSectionAssociationReference;
    @XmlElement(name = "GradebookEntryReference", required = true)
    protected ReferenceType gradebookEntryReference;

    /**
     * Gets the value of the dateFulfilled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateFulfilled() {
        return dateFulfilled;
    }

    /**
     * Sets the value of the dateFulfilled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateFulfilled(String value) {
        this.dateFulfilled = value;
    }

    /**
     * Gets the value of the letterGradeEarned property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLetterGradeEarned() {
        return letterGradeEarned;
    }

    /**
     * Sets the value of the letterGradeEarned property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLetterGradeEarned(String value) {
        this.letterGradeEarned = value;
    }

    /**
     * Gets the value of the numericGradeEarned property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumericGradeEarned() {
        return numericGradeEarned;
    }

    /**
     * Sets the value of the numericGradeEarned property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumericGradeEarned(BigInteger value) {
        this.numericGradeEarned = value;
    }

    /**
     * Gets the value of the competencyLevel property.
     * 
     * @return
     *     possible object is
     *     {@link CompetencyLevelDescriptorType }
     *     
     */
    public CompetencyLevelDescriptorType getCompetencyLevel() {
        return competencyLevel;
    }

    /**
     * Sets the value of the competencyLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link CompetencyLevelDescriptorType }
     *     
     */
    public void setCompetencyLevel(CompetencyLevelDescriptorType value) {
        this.competencyLevel = value;
    }

    /**
     * Gets the value of the diagnosticStatement property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiagnosticStatement() {
        return diagnosticStatement;
    }

    /**
     * Sets the value of the diagnosticStatement property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiagnosticStatement(String value) {
        this.diagnosticStatement = value;
    }

    /**
     * Gets the value of the studentSectionAssociationReference property.
     * 
     * @return
     *     possible object is
     *     {@link StudentSectionAssociationReferenceType }
     *     
     */
    public StudentSectionAssociationReferenceType getStudentSectionAssociationReference() {
        return studentSectionAssociationReference;
    }

    /**
     * Sets the value of the studentSectionAssociationReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentSectionAssociationReferenceType }
     *     
     */
    public void setStudentSectionAssociationReference(StudentSectionAssociationReferenceType value) {
        this.studentSectionAssociationReference = value;
    }

    /**
     * Gets the value of the gradebookEntryReference property.
     * 
     * @return
     *     possible object is
     *     {@link ReferenceType }
     *     
     */
    public ReferenceType getGradebookEntryReference() {
        return gradebookEntryReference;
    }

    /**
     * Sets the value of the gradebookEntryReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferenceType }
     *     
     */
    public void setGradebookEntryReference(ReferenceType value) {
        this.gradebookEntryReference = value;
    }

}
