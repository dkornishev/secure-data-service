//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.12.05 at 01:12:38 PM EST 
//


package org.slc.sli.sample.entitiesR1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LevelType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="LevelType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="All Level (Grade Level PK-12)"/>
 *     &lt;enumeration value="All-Level (Grade Level EC-12)"/>
 *     &lt;enumeration value="Early Childhood (PK-K)"/>
 *     &lt;enumeration value="Elementary (Grade Level 1-6)"/>
 *     &lt;enumeration value="Elementary (Grade Level 1-8)"/>
 *     &lt;enumeration value="Elementary (Grade Level 4-8)"/>
 *     &lt;enumeration value="Elementary (Grade Level EC-4)"/>
 *     &lt;enumeration value="Elementary (Grade Level EC-6)"/>
 *     &lt;enumeration value="Elementary (Grade Level PK-5)"/>
 *     &lt;enumeration value="Elementary (Grade Level PK-6)"/>
 *     &lt;enumeration value="Grade Level NA"/>
 *     &lt;enumeration value="Junior High (Grade Level 6-8)"/>
 *     &lt;enumeration value="Secondary (Grade Level 6-12)"/>
 *     &lt;enumeration value="Secondary (Grade Level 8-12)"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "LevelType")
@XmlEnum
public enum LevelType {

    @XmlEnumValue("All Level (Grade Level PK-12)")
    ALL_LEVEL_GRADE_LEVEL_PK_12("All Level (Grade Level PK-12)"),
    @XmlEnumValue("All-Level (Grade Level EC-12)")
    ALL_LEVEL_GRADE_LEVEL_EC_12("All-Level (Grade Level EC-12)"),
    @XmlEnumValue("Early Childhood (PK-K)")
    EARLY_CHILDHOOD_PK_K("Early Childhood (PK-K)"),
    @XmlEnumValue("Elementary (Grade Level 1-6)")
    ELEMENTARY_GRADE_LEVEL_1_6("Elementary (Grade Level 1-6)"),
    @XmlEnumValue("Elementary (Grade Level 1-8)")
    ELEMENTARY_GRADE_LEVEL_1_8("Elementary (Grade Level 1-8)"),
    @XmlEnumValue("Elementary (Grade Level 4-8)")
    ELEMENTARY_GRADE_LEVEL_4_8("Elementary (Grade Level 4-8)"),
    @XmlEnumValue("Elementary (Grade Level EC-4)")
    ELEMENTARY_GRADE_LEVEL_EC_4("Elementary (Grade Level EC-4)"),
    @XmlEnumValue("Elementary (Grade Level EC-6)")
    ELEMENTARY_GRADE_LEVEL_EC_6("Elementary (Grade Level EC-6)"),
    @XmlEnumValue("Elementary (Grade Level PK-5)")
    ELEMENTARY_GRADE_LEVEL_PK_5("Elementary (Grade Level PK-5)"),
    @XmlEnumValue("Elementary (Grade Level PK-6)")
    ELEMENTARY_GRADE_LEVEL_PK_6("Elementary (Grade Level PK-6)"),
    @XmlEnumValue("Grade Level NA")
    GRADE_LEVEL_NA("Grade Level NA"),
    @XmlEnumValue("Junior High (Grade Level 6-8)")
    JUNIOR_HIGH_GRADE_LEVEL_6_8("Junior High (Grade Level 6-8)"),
    @XmlEnumValue("Secondary (Grade Level 6-12)")
    SECONDARY_GRADE_LEVEL_6_12("Secondary (Grade Level 6-12)"),
    @XmlEnumValue("Secondary (Grade Level 8-12)")
    SECONDARY_GRADE_LEVEL_8_12("Secondary (Grade Level 8-12)"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    LevelType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LevelType fromValue(String v) {
        for (LevelType c: LevelType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
