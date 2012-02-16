//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.12 at 04:54:37 PM EST 
//


package org.slc.sli.test.edfi.entities;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SchoolType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SchoolType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Alternative"/>
 *     &lt;enumeration value="Regular"/>
 *     &lt;enumeration value="Special Education"/>
 *     &lt;enumeration value="Vocational"/>
 *     &lt;enumeration value="JJAEP"/>
 *     &lt;enumeration value="DAEP"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SchoolType")
@XmlEnum
public enum SchoolType {

    @XmlEnumValue("Alternative")
    ALTERNATIVE("Alternative"),
    @XmlEnumValue("Regular")
    REGULAR("Regular"),
    @XmlEnumValue("Special Education")
    SPECIAL_EDUCATION("Special Education"),
    @XmlEnumValue("Vocational")
    VOCATIONAL("Vocational"),
    JJAEP("JJAEP"),
    DAEP("DAEP");
    private final String value;

    SchoolType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SchoolType fromValue(String v) {
        for (SchoolType c: SchoolType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
