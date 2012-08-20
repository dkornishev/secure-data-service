//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.17 at 02:49:01 PM EDT 
//


package org.slc.sli.test.edfi.entitiesR1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gradeLevelType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gradeLevelType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="Adult Education"/>
 *     &lt;enumeration value="Early Education"/>
 *     &lt;enumeration value="Eighth grade"/>
 *     &lt;enumeration value="Eleventh grade"/>
 *     &lt;enumeration value="Fifth grade"/>
 *     &lt;enumeration value="First grade"/>
 *     &lt;enumeration value="Fourth grade"/>
 *     &lt;enumeration value="Grade 13"/>
 *     &lt;enumeration value="Infant/toddler"/>
 *     &lt;enumeration value="Kindergarten"/>
 *     &lt;enumeration value="Ninth grade"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Postsecondary"/>
 *     &lt;enumeration value="Preschool/Prekindergarten"/>
 *     &lt;enumeration value="Second grade"/>
 *     &lt;enumeration value="Seventh grade"/>
 *     &lt;enumeration value="Sixth grade"/>
 *     &lt;enumeration value="Tenth grade"/>
 *     &lt;enumeration value="Third grade"/>
 *     &lt;enumeration value="Transitional Kindergarten"/>
 *     &lt;enumeration value="Twelfth grade"/>
 *     &lt;enumeration value="Ungraded"/>
 *     &lt;enumeration value="Not Available"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gradeLevelType")
@XmlEnum
public enum GradeLevelType {

    @XmlEnumValue("Adult Education")
    ADULT_EDUCATION("Adult Education"),
    @XmlEnumValue("Early Education")
    EARLY_EDUCATION("Early Education"),
    @XmlEnumValue("Eighth grade")
    EIGHTH_GRADE("Eighth grade"),
    @XmlEnumValue("Eleventh grade")
    ELEVENTH_GRADE("Eleventh grade"),
    @XmlEnumValue("Fifth grade")
    FIFTH_GRADE("Fifth grade"),
    @XmlEnumValue("First grade")
    FIRST_GRADE("First grade"),
    @XmlEnumValue("Fourth grade")
    FOURTH_GRADE("Fourth grade"),
    @XmlEnumValue("Grade 13")
    GRADE_13("Grade 13"),
    @XmlEnumValue("Infant/toddler")
    INFANT_TODDLER("Infant/toddler"),
    @XmlEnumValue("Kindergarten")
    KINDERGARTEN("Kindergarten"),
    @XmlEnumValue("Ninth grade")
    NINTH_GRADE("Ninth grade"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Postsecondary")
    POSTSECONDARY("Postsecondary"),
    @XmlEnumValue("Preschool/Prekindergarten")
    PRESCHOOL_PREKINDERGARTEN("Preschool/Prekindergarten"),
    @XmlEnumValue("Second grade")
    SECOND_GRADE("Second grade"),
    @XmlEnumValue("Seventh grade")
    SEVENTH_GRADE("Seventh grade"),
    @XmlEnumValue("Sixth grade")
    SIXTH_GRADE("Sixth grade"),
    @XmlEnumValue("Tenth grade")
    TENTH_GRADE("Tenth grade"),
    @XmlEnumValue("Third grade")
    THIRD_GRADE("Third grade"),
    @XmlEnumValue("Transitional Kindergarten")
    TRANSITIONAL_KINDERGARTEN("Transitional Kindergarten"),
    @XmlEnumValue("Twelfth grade")
    TWELFTH_GRADE("Twelfth grade"),
    @XmlEnumValue("Ungraded")
    UNGRADED("Ungraded"),
    @XmlEnumValue("Not Available")
    NOT_AVAILABLE("Not Available");
    private final String value;

    GradeLevelType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GradeLevelType fromValue(String v) {
        for (GradeLevelType c: GradeLevelType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
