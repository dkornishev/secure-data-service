//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.12 at 04:54:37 PM EST 
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A categorization of the disabilities associated with a student pursuant to Section 504.
 * 
 * <p>Java class for Section504DisabilitiesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Section504DisabilitiesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Section504Disability" type="{http://ed-fi.org/0100}Section504DisabilityItemType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Section504DisabilitiesType", propOrder = {
    "section504Disability"
})
public class Section504DisabilitiesType {

    @XmlElement(name = "Section504Disability")
    protected List<Section504DisabilityItemType> section504Disability;

    /**
     * Gets the value of the section504Disability property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the section504Disability property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSection504Disability().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Section504DisabilityItemType }
     * 
     * 
     */
    public List<Section504DisabilityItemType> getSection504Disability() {
        if (section504Disability == null) {
            section504Disability = new ArrayList<Section504DisabilityItemType>();
        }
        return this.section504Disability;
    }

}
