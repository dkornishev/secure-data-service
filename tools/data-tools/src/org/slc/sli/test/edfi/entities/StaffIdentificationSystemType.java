//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.16 at 01:39:34 PM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StaffIdentificationSystemType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="StaffIdentificationSystemType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Drivers License"/>
 *     &lt;enumeration value="Health Record"/>
 *     &lt;enumeration value="Medicaid"/>
 *     &lt;enumeration value="Professional Certificate"/>
 *     &lt;enumeration value="School"/>
 *     &lt;enumeration value="District"/>
 *     &lt;enumeration value="State"/>
 *     &lt;enumeration value="Federal"/>
 *     &lt;enumeration value="Other Federal"/>
 *     &lt;enumeration value="Selective Service"/>
 *     &lt;enumeration value="SSN"/>
 *     &lt;enumeration value="US Visa"/>
 *     &lt;enumeration value="PIN"/>
 *     &lt;enumeration value="Canadian SIN"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StaffIdentificationSystemType")
@XmlEnum
public enum StaffIdentificationSystemType {

    @XmlEnumValue("Drivers License")
    DRIVERS_LICENSE("Drivers License"),
    @XmlEnumValue("Health Record")
    HEALTH_RECORD("Health Record"),
    @XmlEnumValue("Medicaid")
    MEDICAID("Medicaid"),
    @XmlEnumValue("Professional Certificate")
    PROFESSIONAL_CERTIFICATE("Professional Certificate"),
    @XmlEnumValue("School")
    SCHOOL("School"),
    @XmlEnumValue("District")
    DISTRICT("District"),
    @XmlEnumValue("State")
    STATE("State"),
    @XmlEnumValue("Federal")
    FEDERAL("Federal"),
    @XmlEnumValue("Other Federal")
    OTHER_FEDERAL("Other Federal"),
    @XmlEnumValue("Selective Service")
    SELECTIVE_SERVICE("Selective Service"),
    SSN("SSN"),
    @XmlEnumValue("US Visa")
    US_VISA("US Visa"),
    PIN("PIN"),
    @XmlEnumValue("Canadian SIN")
    CANADIAN_SIN("Canadian SIN"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    StaffIdentificationSystemType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static StaffIdentificationSystemType fromValue(String v) {
        for (StaffIdentificationSystemType c: StaffIdentificationSystemType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
