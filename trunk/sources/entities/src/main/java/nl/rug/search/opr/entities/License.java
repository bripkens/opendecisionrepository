//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2.1.1-4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.11.18 at 01:26:54 PM CET 
//


package nl.rug.search.opr.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for license complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="license">
 *   &lt;complexContent>
 *     &lt;extension base="{}baseEntity">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="restrictive" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="licenseSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "license", propOrder = {
    "name",
    "restrictive",
    "licenseSource"
})
public class License
    extends BaseEntity
{

    protected String name;
    protected boolean restrictive;
    protected String licenseSource;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the restrictive property.
     * 
     */
    public boolean isRestrictive() {
        return restrictive;
    }

    /**
     * Sets the value of the restrictive property.
     * 
     */
    public void setRestrictive(boolean value) {
        this.restrictive = value;
    }

    /**
     * Gets the value of the licenseSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseSource() {
        return licenseSource;
    }

    /**
     * Sets the value of the licenseSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseSource(String value) {
        this.licenseSource = value;
    }

}