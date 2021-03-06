//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.31 at 10:43:34 AM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * The legal document or authorization giving
 *                 authorization to perform
 *                 teaching assignment services.
 *             
 * 
 * <p>Java class for credential complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="credential">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="credentialType" type="{http://slc-sli/ed-org/0.1}credentialType"/>
 *         &lt;element name="credentialField" type="{http://slc-sli/ed-org/0.1}descriptorReferenceType"/>
 *         &lt;element name="level" type="{http://slc-sli/ed-org/0.1}levelType"/>
 *         &lt;element name="teachingCredentialType" type="{http://slc-sli/ed-org/0.1}teachingCredentialType"/>
 *         &lt;element name="credentialIssuanceDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="credentialExpirationDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="teachingCredentialBasis" type="{http://slc-sli/ed-org/0.1}teachingCredentialBasisType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "credential", propOrder = {
    "credentialType",
    "credentialField",
    "level",
    "teachingCredentialType",
    "credentialIssuanceDate",
    "credentialExpirationDate",
    "teachingCredentialBasis"
})
public class Credential {

    @XmlElement(required = true)
    protected CredentialType credentialType;
    @XmlElement(required = true)
    protected DescriptorReferenceType credentialField;
    @XmlElement(required = true)
    protected LevelType level;
    @XmlElement(required = true)
    protected TeachingCredentialType teachingCredentialType;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String credentialIssuanceDate;
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "date")
    protected String credentialExpirationDate;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String teachingCredentialBasis;

    /**
     * Gets the value of the credentialType property.
     * 
     * @return
     *     possible object is
     *     {@link CredentialType }
     *     
     */
    public CredentialType getCredentialType() {
        return credentialType;
    }

    /**
     * Sets the value of the credentialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CredentialType }
     *     
     */
    public void setCredentialType(CredentialType value) {
        this.credentialType = value;
    }

    /**
     * Gets the value of the credentialField property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptorReferenceType }
     *     
     */
    public DescriptorReferenceType getCredentialField() {
        return credentialField;
    }

    /**
     * Sets the value of the credentialField property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptorReferenceType }
     *     
     */
    public void setCredentialField(DescriptorReferenceType value) {
        this.credentialField = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link LevelType }
     *     
     */
    public LevelType getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelType }
     *     
     */
    public void setLevel(LevelType value) {
        this.level = value;
    }

    /**
     * Gets the value of the teachingCredentialType property.
     * 
     * @return
     *     possible object is
     *     {@link TeachingCredentialType }
     *     
     */
    public TeachingCredentialType getTeachingCredentialType() {
        return teachingCredentialType;
    }

    /**
     * Sets the value of the teachingCredentialType property.
     * 
     * @param value
     *     allowed object is
     *     {@link TeachingCredentialType }
     *     
     */
    public void setTeachingCredentialType(TeachingCredentialType value) {
        this.teachingCredentialType = value;
    }

    /**
     * Gets the value of the credentialIssuanceDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCredentialIssuanceDate() {
        return credentialIssuanceDate;
    }

    /**
     * Sets the value of the credentialIssuanceDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCredentialIssuanceDate(String value) {
        this.credentialIssuanceDate = value;
    }

    /**
     * Gets the value of the credentialExpirationDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCredentialExpirationDate() {
        return credentialExpirationDate;
    }

    /**
     * Sets the value of the credentialExpirationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCredentialExpirationDate(String value) {
        this.credentialExpirationDate = value;
    }

    /**
     * Gets the value of the teachingCredentialBasis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTeachingCredentialBasis() {
        return teachingCredentialBasis;
    }

    /**
     * Sets the value of the teachingCredentialBasis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTeachingCredentialBasis(String value) {
        this.teachingCredentialBasis = value;
    }

}
