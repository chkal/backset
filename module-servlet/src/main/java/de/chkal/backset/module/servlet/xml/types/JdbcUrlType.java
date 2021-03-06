//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.07.24 at 07:29:29 AM CEST 
//


package de.chkal.backset.module.servlet.xml.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 *         [
 *         The jdbc-urlType contains the url pattern of the mapping.
 *         It must follow the rules specified in Section 9.3 of the
 *         JDBC Specification where the format is:
 *         
 *         jdbc:<subprotocol>:<subname>
 *         
 *         Example:
 *         
 *         <url>jdbc:mysql://localhost:3307/testdb</url>
 *         
 *         
 *       
 * 
 * <p>Java class for jdbc-urlType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="jdbc-urlType">
 *   &lt;simpleContent>
 *     &lt;restriction base="&lt;http://xmlns.jcp.org/xml/ns/javaee>string">
 *     &lt;/restriction>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "jdbc-urlType")
public class JdbcUrlType
    extends String
{


}
