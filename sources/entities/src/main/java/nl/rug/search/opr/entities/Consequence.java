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
 * <p>Java class for consequence complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="consequence">
 *   &lt;complexContent>
 *     &lt;extension base="{}driver">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="impactIndication" type="{}indicator" minOccurs="0"/>
 *         &lt;element name="qualityAttribute" type="{}qualityAttribute" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consequence",
         propOrder = {
    "description",
    "impactIndication",
    "qualityAttribute"
})
public class Consequence
        extends Driver {

    protected String description;

    protected Indicator impactIndication;

    protected QualityAttribute qualityAttribute;




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
     * Gets the value of the impactIndication property.
     * 
     * @return
     *     possible object is
     *     {@link Indicator }
     *     
     */
    public Indicator getImpactIndication() {
        return impactIndication;
    }




    /**
     * Sets the value of the impactIndication property.
     * 
     * @param value
     *     allowed object is
     *     {@link Indicator }
     *     
     */
    public void setImpactIndication(Indicator value) {
        this.impactIndication = value;
    }




    /**
     * Gets the value of the qualityAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link QualityAttribute }
     *     
     */
    public QualityAttribute getQualityAttribute() {
        return qualityAttribute;
    }




    /**
     * Sets the value of the qualityAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link QualityAttribute }
     *     
     */
    public void setQualityAttribute(QualityAttribute value) {
        this.qualityAttribute = value;
    }
}
