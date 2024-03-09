//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.2 generiert 
// Siehe <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2023.03.13 um 03:17:24 PM CET 
//


package generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für constructor complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="constructor"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{}Node"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="left" type="{}Node"/&gt;
 *         &lt;element name="right" type="{}Node"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="type" use="required" type="{}type" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "constructor", propOrder = {
    "left",
    "right"
})
public class Constructor
    extends Node
{

    @XmlElement(required = true)
    protected Node left;
    @XmlElement(required = true)
    protected Node right;
    @XmlAttribute(name = "type", required = true)
    protected Type type;

    /**
     * Ruft den Wert der left-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Node }
     *     
     */
    public Node getLeft() {
        return left;
    }

    /**
     * Legt den Wert der left-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Node }
     *     
     */
    public void setLeft(Node value) {
        this.left = value;
    }

    /**
     * Ruft den Wert der right-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Node }
     *     
     */
    public Node getRight() {
        return right;
    }

    /**
     * Legt den Wert der right-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Node }
     *     
     */
    public void setRight(Node value) {
        this.right = value;
    }

    /**
     * Ruft den Wert der type-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Type }
     *     
     */
    public Type getType() {
        return type;
    }

    /**
     * Legt den Wert der type-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Type }
     *     
     */
    public void setType(Type value) {
        this.type = value;
    }

}
