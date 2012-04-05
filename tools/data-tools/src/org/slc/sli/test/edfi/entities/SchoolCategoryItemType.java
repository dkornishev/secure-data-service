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
 * <p>Java class for SchoolCategoryItemType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SchoolCategoryItemType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Elementary/Secondary School"/>
 *     &lt;enumeration value="Elementary School"/>
 *     &lt;enumeration value="High School"/>
 *     &lt;enumeration value="Middle School"/>
 *     &lt;enumeration value="Junior High School"/>
 *     &lt;enumeration value="Elementary School"/>
 *     &lt;enumeration value="SecondarySchool"/>
 *     &lt;enumeration value="Ungraded"/>
 *     &lt;enumeration value="Adult School"/>
 *     &lt;enumeration value="Infant/toddler School"/>
 *     &lt;enumeration value="Preschool/early childhood"/>
 *     &lt;enumeration value="Primary School"/>
 *     &lt;enumeration value="Intermediate School"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SchoolCategoryItemType")
@XmlEnum
public enum SchoolCategoryItemType {

    @XmlEnumValue("Elementary/Secondary School")
    ELEMENTARY_SECONDARY_SCHOOL("Elementary/Secondary School"),
    @XmlEnumValue("Elementary School")
    ELEMENTARY_SCHOOL("Elementary School"),
    @XmlEnumValue("High School")
    HIGH_SCHOOL("High School"),
    @XmlEnumValue("Middle School")
    MIDDLE_SCHOOL("Middle School"),
    @XmlEnumValue("Junior High School")
    JUNIOR_HIGH_SCHOOL("Junior High School"),
    @XmlEnumValue("SecondarySchool")
    SECONDARY_SCHOOL("SecondarySchool"),
    @XmlEnumValue("Ungraded")
    UNGRADED("Ungraded"),
    @XmlEnumValue("Adult School")
    ADULT_SCHOOL("Adult School"),
    @XmlEnumValue("Infant/toddler School")
    INFANT_TODDLER_SCHOOL("Infant/toddler School"),
    @XmlEnumValue("Preschool/early childhood")
    PRESCHOOL_EARLY_CHILDHOOD("Preschool/early childhood"),
    @XmlEnumValue("Primary School")
    PRIMARY_SCHOOL("Primary School"),
    @XmlEnumValue("Intermediate School")
    INTERMEDIATE_SCHOOL("Intermediate School");
    private final String value;

    SchoolCategoryItemType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SchoolCategoryItemType fromValue(String v) {
        for (SchoolCategoryItemType c: SchoolCategoryItemType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
