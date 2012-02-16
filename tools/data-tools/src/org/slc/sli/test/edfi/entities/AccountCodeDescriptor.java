//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.12 at 04:54:37 PM EST 
//


package org.slc.sli.test.edfi.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * This descriptor holds the set of account codes defined for the education accounting system organized by account code type (e.g., fund, function, object, etc.)
 * 
 * <p>Java class for AccountCodeDescriptor complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountCodeDescriptor">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ed-fi.org/0100}ComplexObjectType">
 *       &lt;sequence>
 *         &lt;element name="AccountCodeType" type="{http://ed-fi.org/0100}AccountCodeType"/>
 *         &lt;element name="CodeValue" type="{http://ed-fi.org/0100}CodeValue"/>
 *         &lt;element name="ShortDescription" type="{http://ed-fi.org/0100}ShortDescription"/>
 *         &lt;element name="Description" type="{http://ed-fi.org/0100}Description" minOccurs="0"/>
 *         &lt;element name="BeginDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="EndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="EducationOrganizationReference" type="{http://ed-fi.org/0100}EducationalOrgReferenceType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountCodeDescriptor", propOrder = {
    "accountCodeType",
    "codeValue",
    "shortDescription",
    "description",
    "beginDate",
    "endDate",
    "educationOrganizationReference"
})
public class AccountCodeDescriptor
    extends ComplexObjectType
{

    @XmlElement(name = "AccountCodeType", required = true)
    protected String accountCodeType;
    @XmlElement(name = "CodeValue", required = true)
    protected String codeValue;
    @XmlElement(name = "ShortDescription", required = true)
    protected String shortDescription;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "BeginDate", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar beginDate;
    @XmlElement(name = "EndDate", type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected Calendar endDate;
    @XmlElement(name = "EducationOrganizationReference", required = true)
    protected List<EducationalOrgReferenceType> educationOrganizationReference;

    /**
     * Gets the value of the accountCodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountCodeType() {
        return accountCodeType;
    }

    /**
     * Sets the value of the accountCodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountCodeType(String value) {
        this.accountCodeType = value;
    }

    /**
     * Gets the value of the codeValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeValue() {
        return codeValue;
    }

    /**
     * Sets the value of the codeValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeValue(String value) {
        this.codeValue = value;
    }

    /**
     * Gets the value of the shortDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShortDescription() {
        return shortDescription;
    }

    /**
     * Sets the value of the shortDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShortDescription(String value) {
        this.shortDescription = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the beginDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getBeginDate() {
        return beginDate;
    }

    /**
     * Sets the value of the beginDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeginDate(Calendar value) {
        this.beginDate = value;
    }

    /**
     * Gets the value of the endDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Calendar getEndDate() {
        return endDate;
    }

    /**
     * Sets the value of the endDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndDate(Calendar value) {
        this.endDate = value;
    }

    /**
     * Gets the value of the educationOrganizationReference property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the educationOrganizationReference property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEducationOrganizationReference().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EducationalOrgReferenceType }
     * 
     * 
     */
    public List<EducationalOrgReferenceType> getEducationOrganizationReference() {
        if (educationOrganizationReference == null) {
            educationOrganizationReference = new ArrayList<EducationalOrgReferenceType>();
        }
        return this.educationOrganizationReference;
    }

}
