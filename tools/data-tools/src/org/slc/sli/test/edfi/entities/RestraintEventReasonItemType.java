//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.30 at 01:48:06 PM EDT 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RestraintEventReasonItemType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RestraintEventReasonItemType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Imminent Serious Physical Harm To Themselves"/>
 *     &lt;enumeration value="Imminent Serious Physical Harm To Others"/>
 *     &lt;enumeration value="Imminent Serious Property Destruction"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RestraintEventReasonItemType")
@XmlEnum
public enum RestraintEventReasonItemType {

    @XmlEnumValue("Imminent Serious Physical Harm To Themselves")
    IMMINENT_SERIOUS_PHYSICAL_HARM_TO_THEMSELVES("Imminent Serious Physical Harm To Themselves"),
    @XmlEnumValue("Imminent Serious Physical Harm To Others")
    IMMINENT_SERIOUS_PHYSICAL_HARM_TO_OTHERS("Imminent Serious Physical Harm To Others"),
    @XmlEnumValue("Imminent Serious Property Destruction")
    IMMINENT_SERIOUS_PROPERTY_DESTRUCTION("Imminent Serious Property Destruction");
    private final String value;

    RestraintEventReasonItemType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RestraintEventReasonItemType fromValue(String v) {
        for (RestraintEventReasonItemType c: RestraintEventReasonItemType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
