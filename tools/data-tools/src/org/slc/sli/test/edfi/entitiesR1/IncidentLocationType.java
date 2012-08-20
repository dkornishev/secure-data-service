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
 * <p>Java class for incidentLocationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="incidentLocationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token">
 *     &lt;enumeration value="On School"/>
 *     &lt;enumeration value="Administrative offices area"/>
 *     &lt;enumeration value="Cafeteria area"/>
 *     &lt;enumeration value="Classroom"/>
 *     &lt;enumeration value="Hallway or stairs"/>
 *     &lt;enumeration value="Locker room or gym areas"/>
 *     &lt;enumeration value="Restroom"/>
 *     &lt;enumeration value="Library/media center"/>
 *     &lt;enumeration value="Computer lab"/>
 *     &lt;enumeration value="Auditorium"/>
 *     &lt;enumeration value="On-School other inside area"/>
 *     &lt;enumeration value="Athletic field or playground"/>
 *     &lt;enumeration value="Stadium"/>
 *     &lt;enumeration value="Parking lot"/>
 *     &lt;enumeration value="On-School other outside area"/>
 *     &lt;enumeration value="Off School"/>
 *     &lt;enumeration value="Bus stop"/>
 *     &lt;enumeration value="School bus"/>
 *     &lt;enumeration value="Walking to or from school"/>
 *     &lt;enumeration value="Off-School at other school"/>
 *     &lt;enumeration value="Off-School at other school district facility"/>
 *     &lt;enumeration value="Online"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "incidentLocationType")
@XmlEnum
public enum IncidentLocationType {

    @XmlEnumValue("On School")
    ON_SCHOOL("On School"),
    @XmlEnumValue("Administrative offices area")
    ADMINISTRATIVE_OFFICES_AREA("Administrative offices area"),
    @XmlEnumValue("Cafeteria area")
    CAFETERIA_AREA("Cafeteria area"),
    @XmlEnumValue("Classroom")
    CLASSROOM("Classroom"),
    @XmlEnumValue("Hallway or stairs")
    HALLWAY_OR_STAIRS("Hallway or stairs"),
    @XmlEnumValue("Locker room or gym areas")
    LOCKER_ROOM_OR_GYM_AREAS("Locker room or gym areas"),
    @XmlEnumValue("Restroom")
    RESTROOM("Restroom"),
    @XmlEnumValue("Library/media center")
    LIBRARY_MEDIA_CENTER("Library/media center"),
    @XmlEnumValue("Computer lab")
    COMPUTER_LAB("Computer lab"),
    @XmlEnumValue("Auditorium")
    AUDITORIUM("Auditorium"),
    @XmlEnumValue("On-School other inside area")
    ON_SCHOOL_OTHER_INSIDE_AREA("On-School other inside area"),
    @XmlEnumValue("Athletic field or playground")
    ATHLETIC_FIELD_OR_PLAYGROUND("Athletic field or playground"),
    @XmlEnumValue("Stadium")
    STADIUM("Stadium"),
    @XmlEnumValue("Parking lot")
    PARKING_LOT("Parking lot"),
    @XmlEnumValue("On-School other outside area")
    ON_SCHOOL_OTHER_OUTSIDE_AREA("On-School other outside area"),
    @XmlEnumValue("Off School")
    OFF_SCHOOL("Off School"),
    @XmlEnumValue("Bus stop")
    BUS_STOP("Bus stop"),
    @XmlEnumValue("School bus")
    SCHOOL_BUS("School bus"),
    @XmlEnumValue("Walking to or from school")
    WALKING_TO_OR_FROM_SCHOOL("Walking to or from school"),
    @XmlEnumValue("Off-School at other school")
    OFF_SCHOOL_AT_OTHER_SCHOOL("Off-School at other school"),
    @XmlEnumValue("Off-School at other school district facility")
    OFF_SCHOOL_AT_OTHER_SCHOOL_DISTRICT_FACILITY("Off-School at other school district facility"),
    @XmlEnumValue("Online")
    ONLINE("Online"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    IncidentLocationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IncidentLocationType fromValue(String v) {
        for (IncidentLocationType c: IncidentLocationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
