//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.08.07 at 08:25:34 AM CEST 
//


package de.chkal.backset.module.jsp.xml.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 	
 * 
 * 	  The homeType defines the fully-qualified name of
 * 	  an enterprise bean's home interface.
 * 
 * 	  Example:
 * 
 * 	      <home>com.aardvark.payroll.PayrollHome</home>
 * 
 * 	  
 *       
 * 
 * <p>Java class for homeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="homeType">
 *   &lt;simpleContent>
 *     &lt;restriction base="&lt;http://java.sun.com/xml/ns/javaee>fully-qualified-classType">
 *     &lt;/restriction>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "homeType")
public class HomeType
    extends FullyQualifiedClassType
{


}