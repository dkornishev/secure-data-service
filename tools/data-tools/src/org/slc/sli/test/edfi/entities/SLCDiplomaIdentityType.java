//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.16 at 01:39:34 PM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for SLC-DiplomaIdentityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-DiplomaIdentityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EducationalOrgReference" type="{http://ed-fi.org/0100}SLC-EducationalOrgReferenceType"/>
 *         &lt;element name="DiplomaAwardDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="DiplomaType" type="{http://ed-fi.org/0100}DiplomaType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-DiplomaIdentityType", propOrder = {
    "educationalOrgReference",
    "diplomaAwardDate",
    "diplomaType"
})
public class SLCDiplomaIdentityType {

    @XmlElement(name = "EducationalOrgReference", required = true)
    protected SLCEducationalOrgReferenceType educationalOrgReference;
    @XmlElement(name = "DiplomaAwardDate", required = true)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String diplomaAwardDate;
    @XmlElement(name = "DiplomaType", required = true)
    protected DiplomaType diplomaType;

    /**
     * Gets the value of the educationalOrgReference property.
     * 
     * @return
     *     possible object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public SLCEducationalOrgReferenceType getEducationalOrgReference() {
        return educationalOrgReference;
    }

    /**
     * Sets the value of the educationalOrgReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCEducationalOrgReferenceType }
     *     
     */
    public void setEducationalOrgReference(SLCEducationalOrgReferenceType value) {
        this.educationalOrgReference = value;
    }

    /**
     * Gets the value of the diplomaAwardDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDiplomaAwardDate() {
        return diplomaAwardDate;
    }

    /**
     * Sets the value of the diplomaAwardDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDiplomaAwardDate(String value) {
        this.diplomaAwardDate = value;
    }

    /**
     * Gets the value of the diplomaType property.
     * 
     * @return
     *     possible object is
     *     {@link DiplomaType }
     *     
     */
    public DiplomaType getDiplomaType() {
        return diplomaType;
    }

    /**
     * Sets the value of the diplomaType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DiplomaType }
     *     
     */
    public void setDiplomaType(DiplomaType value) {
        this.diplomaType = value;
    }

}
