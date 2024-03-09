//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 generiert 
// Siehe <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2023.03.13 um 03:17:24 PM CET 
//


package generated;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für quadruple complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="quadruple"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="d1" type="{}positiveDec"/&gt;
 *         &lt;element name="c1" type="{}positiveDec"/&gt;
 *         &lt;element name="d2" type="{}positiveDec"/&gt;
 *         &lt;element name="c2" type="{}positiveDec"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "quadruple", propOrder = {
    "d1",
    "c1",
    "d2",
    "c2"
})
public class Quadruple {

    @XmlElement(required = true)
    protected BigDecimal d1;
    @XmlElement(required = true)
    protected BigDecimal c1;
    @XmlElement(required = true)
    protected BigDecimal d2;
    @XmlElement(required = true)
    protected BigDecimal c2;

    /**
     * Ruft den Wert der d1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getD1() {
        return d1;
    }

    /**
     * Legt den Wert der d1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setD1(BigDecimal value) {
        this.d1 = value;
    }

    /**
     * Ruft den Wert der c1-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getC1() {
        return c1;
    }

    /**
     * Legt den Wert der c1-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setC1(BigDecimal value) {
        this.c1 = value;
    }

    /**
     * Ruft den Wert der d2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getD2() {
        return d2;
    }

    /**
     * Legt den Wert der d2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setD2(BigDecimal value) {
        this.d2 = value;
    }

    /**
     * Ruft den Wert der c2-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getC2() {
        return c2;
    }

    /**
     * Legt den Wert der c2-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setC2(BigDecimal value) {
        this.c2 = value;
    }

}
