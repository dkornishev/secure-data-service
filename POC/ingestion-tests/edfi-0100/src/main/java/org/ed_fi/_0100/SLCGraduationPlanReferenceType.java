//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.01.22 at 01:42:02 PM EST 
//


package org.ed_fi._0100;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * New SLC natural reference type for GraduationPlan.
 * 
 * <p>Java class for SLC-GraduationPlanReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SLC-GraduationPlanReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GraduationPlanIdentity" type="{http://ed-fi.org/0100}SLC-GraduationPlanIdentityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SLC-GraduationPlanReferenceType", propOrder = {
    "graduationPlanIdentity"
})
public class SLCGraduationPlanReferenceType {

    @XmlElement(name = "GraduationPlanIdentity", required = true)
    protected SLCGraduationPlanIdentityType graduationPlanIdentity;

    /**
     * Gets the value of the graduationPlanIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link SLCGraduationPlanIdentityType }
     *     
     */
    public SLCGraduationPlanIdentityType getGraduationPlanIdentity() {
        return graduationPlanIdentity;
    }

    /**
     * Sets the value of the graduationPlanIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SLCGraduationPlanIdentityType }
     *     
     */
    public void setGraduationPlanIdentity(SLCGraduationPlanIdentityType value) {
        this.graduationPlanIdentity = value;
    }

}
