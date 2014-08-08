package de.chkal.backset.module.jsp.xml;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;

import org.apache.jasper.deploy.FunctionInfo;
import org.apache.jasper.deploy.TagAttributeInfo;
import org.apache.jasper.deploy.TagFileInfo;
import org.apache.jasper.deploy.TagInfo;
import org.apache.jasper.deploy.TagLibraryInfo;
import org.apache.jasper.deploy.TagLibraryValidatorInfo;
import org.apache.jasper.deploy.TagVariableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import de.chkal.backset.module.jsp.xml.types.FunctionType;
import de.chkal.backset.module.jsp.xml.types.ListenerType;
import de.chkal.backset.module.jsp.xml.types.TagFileType;
import de.chkal.backset.module.jsp.xml.types.TagType;
import de.chkal.backset.module.jsp.xml.types.TldAttributeType;
import de.chkal.backset.module.jsp.xml.types.TldTaglibType;
import de.chkal.backset.module.jsp.xml.types.ValidatorType;
import de.chkal.backset.module.jsp.xml.types.VariableType;

public class TaglibParser {

  private final Logger log = LoggerFactory.getLogger(TaglibParser.class);

  private JAXBContext context;

  public TaglibParser() {
    try {
      context = JAXBContext.newInstance(TldTaglibType.class.getPackage().getName());
    } catch (JAXBException e) {
      throw new IllegalStateException(e);
    }
  }

  public TagLibraryInfo parse(InputStream stream) {

    try {

      XMLReader reader = XMLReaderFactory.createXMLReader();

      NamespaceFilter filter = new NamespaceFilter();
      filter.setParent(reader);

      SAXSource source = new SAXSource(filter, new InputSource(stream));

      Unmarshaller unmarshaller = context.createUnmarshaller();
      JAXBElement<?> rootElement = (JAXBElement<?>) unmarshaller.unmarshal(source);

      if (rootElement.getDeclaredType().equals(TldTaglibType.class)) {
        return convert((TldTaglibType) rootElement.getValue());
      }

      return null;

    } catch (JAXBException | SAXException e) {
      log.warn("Failed to parse TLD", e);
      return null;
    }
  }

  private TagLibraryInfo convert(TldTaglibType taglibType) {

    TagLibraryInfo libraryInfo = new TagLibraryInfo();

    if (taglibType.getShortName() != null) {
      libraryInfo.setPrefix(taglibType.getShortName().getValue());
    }

    if (taglibType.getUri() != null) {
      libraryInfo.setUri(taglibType.getUri().getValue());
    }

    if (taglibType.getListener() != null) {
      for (ListenerType listenerType : taglibType.getListener()) {
        libraryInfo.addListener(listenerType.getListenerClass().getValue());
      }
    }

    ValidatorType validatorType = taglibType.getValidator();
    if (validatorType != null) {

      TagLibraryValidatorInfo validatorInfo = new TagLibraryValidatorInfo();

      if (validatorType.getValidatorClass() != null) {
        validatorInfo.setValidatorClass(validatorType.getValidatorClass().getValue());
      }

      libraryInfo.setValidator(validatorInfo);

    }

    if (taglibType.getTag() != null) {
      for (TagType tagType : taglibType.getTag()) {
        libraryInfo.addTagInfo(createTagInfo(tagType));
      }
    }

    if (taglibType.getTagFile() != null) {
      for (TagFileType tagFileType : taglibType.getTagFile()) {
        libraryInfo.addTagFileInfo(createTagFileInfo(tagFileType));
      }
    }

    if (taglibType.getFunction() != null) {
      for (FunctionType functionType : taglibType.getFunction()) {
        libraryInfo.addFunctionInfo(createFunctionInfo(functionType));
      }
    }

    if (taglibType.getTlibVersion() != null) {
      libraryInfo.setTlibversion(taglibType.getTlibVersion());
    }

    if (taglibType.getVersion() != null) {
      libraryInfo.setJspversion(taglibType.getVersion());
    }

    if (taglibType.getShortName() != null) {
      libraryInfo.setShortname(taglibType.getShortName().getValue());
    }

    if (taglibType.getUri() != null) {
      libraryInfo.setUrn(taglibType.getUri().getValue());
    }

    return libraryInfo;
  }

  private TagFileInfo createTagFileInfo(TagFileType tagFileType) {

    TagFileInfo tagFileInfo = new TagFileInfo();

    if (tagFileType.getName() != null) {
      tagFileInfo.setName(tagFileType.getName().getValue());
    }

    if (tagFileType.getPath() != null) {
      tagFileInfo.setPath(tagFileType.getPath().getValue());
    }

    return tagFileInfo;

  }

  private FunctionInfo createFunctionInfo(FunctionType functionType) {

    FunctionInfo functionInfo = new FunctionInfo();

    if (functionType.getName() != null) {
      functionInfo.setName(functionType.getName().getValue());
    }

    if (functionType.getFunctionClass() != null) {
      functionInfo.setFunctionClass(functionType.getFunctionClass().getValue());
    }

    if (functionType.getFunctionSignature() != null) {
      functionInfo.setFunctionSignature(functionType.getFunctionSignature().getValue());
    }

    return functionInfo;

  }

  private TagInfo createTagInfo(TagType tagType) {

    TagInfo tagInfo = new TagInfo();

    if (tagType.getName() != null) {
      tagInfo.setTagName(tagType.getName().getValue());
    }

    if (tagType.getTagClass() != null) {
      tagInfo.setTagClassName(tagType.getTagClass().getValue());
    }

    if (tagType.getBodyContent() != null) {
      tagInfo.setBodyContent(tagType.getBodyContent().getValue());
    }

    if (tagType.getTeiClass() != null) {
      tagInfo.setTagExtraInfo(tagType.getTeiClass().getValue());
    }

    if (tagType.getAttribute() != null) {
      for (TldAttributeType attributeType : tagType.getAttribute()) {
        tagInfo.addTagAttributeInfo(createTagAttributeInfo(attributeType));
      }
    }

    if (tagType.getVariable() != null) {
      for (VariableType variableType : tagType.getVariable()) {
        tagInfo.addTagVariableInfo(createTagVariableInfo(variableType));
      }
    }

    if (tagType.getDynamicAttributes() != null) {
      tagInfo.setDynamicAttributes(tagType.getDynamicAttributes().getValue());
    }

    return tagInfo;

  }

  private TagVariableInfo createTagVariableInfo(VariableType variableType) {

    TagVariableInfo variableInfo = new TagVariableInfo();

    if (variableType.getNameGiven() != null) {
      variableInfo.setNameGiven(variableType.getNameGiven().getValue());
    }

    if (variableType.getNameFromAttribute() != null) {
      variableInfo.setNameFromAttribute(variableType.getNameFromAttribute().getValue());
    }

    if (variableType.getVariableClass() != null) {
      variableInfo.setClassName(variableType.getVariableClass().getValue());
    }

    if (variableType.getDeclare() != null) {
      variableInfo.setDeclare(variableType.getDeclare().getValue());
    }

    if (variableType.getScope() != null) {
      variableInfo.setScope(variableType.getScope().getValue());
    }

    return variableInfo;

  }

  private TagAttributeInfo createTagAttributeInfo(TldAttributeType attributeType) {

    TagAttributeInfo attributeInfo = new TagAttributeInfo();

    if (attributeType.getName() != null) {
      attributeInfo.setName(attributeType.getName().getValue());
    }

    if (attributeType.getType() != null) {
      attributeInfo.setType(attributeType.getType().getValue());
    }

    if (attributeType.getRtexprvalue() != null) {
      attributeInfo.setReqTime(attributeType.getRtexprvalue().getValue());
    }

    if (attributeType.getRequired() != null) {
      attributeInfo.setRequired(attributeType.getRequired().getValue());
    }

    if (attributeType.getFragment() != null) {
      attributeInfo.setFragment(attributeType.getFragment().getValue());
    }

    // not sure if Jasper requires the attributes like this
    if (attributeType.getDeferredValue() != null) {
      attributeInfo.setDeferredValue("true"); // correct?
      if (attributeType.getDeferredValue().getType() != null) {
        attributeInfo.setExpectedTypeName(attributeType.getDeferredValue().getType().getValue());
      }
    }

    // not sure if Jasper requires the attributes like this
    if (attributeType.getDeferredMethod() != null) {
      attributeInfo.setDeferredMethod("true"); // correct?
      if (attributeType.getDeferredMethod().getMethodSignature() != null) {
        attributeInfo.setMethodSignature(
            attributeType.getDeferredMethod().getMethodSignature().getValue());
      }
    }

    return attributeInfo;

  }

}
