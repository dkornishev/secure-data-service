//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.04.17 at 01:12:00 PM EDT 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A coding scheme that is used for identification and record-keeping purposes by schools, social services, or other agencies to refer to a student.
 * 
 * <p>Java class for StudentIdentificationCode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="StudentIdentificationCode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdentificationCode" type="{http://ed-fi.org/0100}IdentificationCode"/>
 *       &lt;/sequence>
 *       &lt;attribute name="IdentificationSystem" use="required" type="{http://ed-fi.org/0100}StudentIdentificationSystemType" />
 *       &lt;attribute name="AssigningOrganizationCode" type="{http://ed-fi.org/0100}IdentificationCode" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StudentIdentificationCode", propOrder = {
    "identificationCode"
})
public class StudentIdentificationCode {

    @XmlElement(name = "IdentificationCode", required = true)
    protected String identificationCode;
    @XmlAttribute(name = "IdentificationSystem", required = true)
    protected StudentIdentificationSystemType identificationSystem;
    @XmlAttribute(name = "AssigningOrganizationCode")
    protected String assigningOrganizationCode;

    /**
     * Gets the value of the identificationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificationCode() {
        return identificationCode;
    }

    /**
     * Sets the value of the identificationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificationCode(String value) {
        this.identificationCode = value;
    }

    /**
     * Gets the value of the identificationSystem property.
     * 
     * @return
     *     possible object is
     *     {@link StudentIdentificationSystemType }
     *     
     */
    public StudentIdentificationSystemType getIdentificationSystem() {
        return identificationSystem;
    }

    /**
     * Sets the value of the identificationSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link StudentIdentificationSystemType }
     *     
     */
    public void setIdentificationSystem(StudentIdentificationSystemType value) {
        this.identificationSystem = value;
    }

    /**
     * Gets the value of the assigningOrganizationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssigningOrganizationCode() {
        return assigningOrganizationCode;
    }

    /**
     * Sets the value of the assigningOrganizationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssigningOrganizationCode(String value) {
        this.assigningOrganizationCode = value;
    }

}
