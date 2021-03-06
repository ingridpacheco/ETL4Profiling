/**
*
*/
package br.ufrj.dcc.kettle.ResourcePropertiesAnalyzer;

import java.util.List;
import java.util.Map;

import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

/**
* @author IngridPacheco
*
*/

@SuppressWarnings("deprecation")
public class ResourcePropertiesAnalyzerMeta extends BaseStepMeta implements StepMetaInterface {

	private String DBpedia;
	private String template;
	private String resource;
	private String whichProperty;
	private String browseOutputFilename;
	private String resourcesProperties;
	private String resources;
	private String templateProperties;
	private String chooseInput;
	public String browseOutputCSVFilename;
	private Boolean notMappedResources;
	
	public ResourcePropertiesAnalyzerMeta() {
		super(); // allocate BaseStepInfo
	}
	
	public String getChooseInput() {
		return chooseInput;
	}
	
	public void setChooseInput(String chooseInput) {
		this.chooseInput = chooseInput;
	}
	
	public String getResourceProperties() {
		return resourcesProperties;
	}
	
	public void setResourceProperties(String resourcesProperties) {
		this.resourcesProperties = resourcesProperties;
	}
	
	public String getInputResource() {
		return resources;
	}
	
	public void setInputResource(String resources) {
		this.resources = resources;
	}
	
	public String getTemplateProperties() {
		return templateProperties;
	}
	
	public void setTemplateProperties(String templateProperties) {
		this.templateProperties = templateProperties;
	}
	
	public String getDBpedia() {
		return DBpedia;
	}
	
	public void setDBpedia(String DBpediaValue) {
		this.DBpedia = DBpediaValue;
	}
	
	public String getTemplate() {
		return template;
	}
	
	public void setTemplate(String templateValue) {
		this.template = templateValue;
	}
	
	public String getResource() {
		return resource;
	}
	
	public void setResource(String resourceValue) {
		this.resource = resourceValue;
	}
	
	public String getOutputFile() {
		return browseOutputFilename;
	}

	public void setOutputFile(String browseOutputFilename) {
		this.browseOutputFilename = browseOutputFilename;
	}
	
	public String getWhichProperty() {
		return whichProperty;
	}

	public void setWhichProperty(String whichProperty) {
		this.whichProperty = whichProperty;
	}
	
	public String getOutputCSVFile() {
		return browseOutputCSVFilename;
	}

	public void setOutputCSVFile(String browseOutputCSVFilename) {
		this.browseOutputCSVFilename = browseOutputCSVFilename;
	}
	
	public boolean getNotMappedResources() {
		return notMappedResources;
	}
	
	public void setNotMappedResources(Boolean notMappedResources) {
		this.notMappedResources = notMappedResources;
	}
	
	@Override
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
	Trans trans) {
		return new ResourcePropertiesAnalyzerStep(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}
	
	@Override
	public StepDataInterface getStepData() {
		return new ResourcePropertiesAnalyzerData();
	}
	
	@Override
	public void setDefault() {
		notMappedResources = false;
		setChooseInput("Previous fields input");
	// TODO Auto-generated method stub
	}
	
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
		try {
			DBpedia = XMLHandler.getTagValue(stepnode,"DBPEDIA");
			template = XMLHandler.getTagValue(stepnode,"TEMPLATE");
			resourcesProperties = XMLHandler.getTagValue(stepnode,"RESOURCESPROPERTIES");
			templateProperties = XMLHandler.getTagValue(stepnode,"TEMPLATEPROPERTIES");
			resources = XMLHandler.getTagValue(stepnode,"RESOURCES");
			resource = XMLHandler.getTagValue(stepnode,"RESOURCE");
			whichProperty = XMLHandler.getTagValue(stepnode,"WHICHPROPERTY");
			chooseInput = XMLHandler.getTagValue(stepnode,"CHOOSEINPUT");
			browseOutputFilename = XMLHandler.getTagValue(stepnode,"BROWSEOUTPUTFILENAME");
			browseOutputCSVFilename = XMLHandler.getTagValue(stepnode,"BROWSEOUTPUTCSVFILENAME");
			notMappedResources = "Y".equals(XMLHandler.getTagValue(stepnode, "NOTMAPPEDRESOURCES"));
		} catch (Exception e) {
			throw new KettleXMLException("Load XML: Excption ", e);// Messages.getString(“KafkaTopicPartitionConsumerMeta.Exception.loadXml”),
		// e);
		}
	}
	
	public String getXML() throws KettleException {
		StringBuilder retVal = new StringBuilder();
		if (DBpedia != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("DBPEDIA", DBpedia));
		}
		if (template != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("TEMPLATE", template));
		}
		if (resourcesProperties != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("RESOURCESPROPERTIES", resourcesProperties));
		}
		if (templateProperties != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("TEMPLATEPROPERTIES", templateProperties));
		}
		if (resources != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("RESOURCES", resources));
		}
		if (resource != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("RESOURCE", resource));
		}
		if (whichProperty != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("WHICHPROPERTY", whichProperty));
		}
		if (browseOutputFilename != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("BROWSEOUTPUTFILENAME", browseOutputFilename));
		}
		if (browseOutputCSVFilename != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("BROWSEOUTPUTCSVFILENAME", browseOutputCSVFilename));
		}
		if (chooseInput != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("CHOOSEINPUT", chooseInput));
		}
		retVal.append("    ").append(XMLHandler.addTagValue("NOTMAPPEDRESOURCES", notMappedResources));
		return retVal.toString();
	}
	
	public void readRep(Repository rep, ObjectId stepId, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
		try {
			DBpedia = rep.getStepAttributeString(stepId, "DBPEDIA");
			resourcesProperties = rep.getStepAttributeString(stepId, "RESOURCESPROPERTIES");
			templateProperties = rep.getStepAttributeString(stepId, "TEMPLATEPROPERTIES");
			resources = rep.getStepAttributeString(stepId, "RESOURCES");
			template = rep.getStepAttributeString(stepId, "TEMPLATE");
			resource = rep.getStepAttributeString(stepId, "RESOURCE");
			chooseInput = rep.getStepAttributeString(stepId, "CHOOSEINPUT");
			whichProperty = rep.getStepAttributeString(stepId, "WHICHPROPERTY");
			browseOutputFilename = rep.getStepAttributeString(stepId, "BROWSEOUTPUTFILENAME");
			browseOutputCSVFilename = rep.getStepAttributeString(stepId, "BROWSEOUTPUTCSVFILENAME");
			notMappedResources = rep.getStepAttributeBoolean(stepId, "NOTMAPPEDRESOURCES");
		} catch (Exception e) {
			throw new KettleException("Unexpected error reading step Sample Plug-In from the repository", e);
		}
	}
	
	public void saveRep(Repository rep, ObjectId transformationId, ObjectId stepId) throws KettleException {
		try {
			if (resourcesProperties != null) {
				rep.saveStepAttribute(transformationId, stepId, "RESOURCESPROPERTIES", resourcesProperties);
			}
			if (templateProperties != null) {
				rep.saveStepAttribute(transformationId, stepId, "TEMPLATEPROPERTIES", templateProperties);
			}
			if (resources != null) {
				rep.saveStepAttribute(transformationId, stepId, "RESOURCES", resources);
			}
			if (DBpedia != null) {
				rep.saveStepAttribute(transformationId, stepId, "DBPEDIA", DBpedia);
			}
			if (template != null) {
				rep.saveStepAttribute(transformationId, stepId, "TEMPLATE", template);
			}
			if (resource != null) {
				rep.saveStepAttribute(transformationId, stepId, "RESOURCE", resource);
			}
			if (whichProperty != null) {
				rep.saveStepAttribute(transformationId, stepId, "WHICHPROPERTY", whichProperty);
			}
			if (browseOutputFilename != null) {
				rep.saveStepAttribute(transformationId, stepId, "BROWSEOUTPUTFILENAME", browseOutputFilename);
			}
			if (browseOutputCSVFilename != null) {
				rep.saveStepAttribute(transformationId, stepId, "BROWSEOUTPUTCSVFILENAME", browseOutputCSVFilename);
			}
			if (chooseInput != null) {
				rep.saveStepAttribute(transformationId, stepId, "CHOOSEINPUT", chooseInput);
			}
			rep.saveStepAttribute(transformationId, stepId,
	                "NOTMAPPEDRESOURCES", notMappedResources);
		} catch (Exception e) {
			throw new KettleException("Unexpected error saving step Sample Plug-In from the repository", e);
		}
	}
	
	public void getFields(RowMetaInterface rowMeta, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException {
		rowMeta.clear();
		
		ValueMetaInterface PropertiesMeta = new ValueMeta("", ValueMetaInterface.TYPE_STRING);
		PropertiesMeta.setName("Property");
		PropertiesMeta.setOrigin(origin);
		rowMeta.addValueMeta(PropertiesMeta);
		
		ValueMetaInterface IsMapedProperty = new ValueMeta("", ValueMetaInterface.TYPE_STRING);
		IsMapedProperty.setName("Is property in template?");
		IsMapedProperty.setOrigin(origin);
		rowMeta.addValueMeta(IsMapedProperty);
		
		ValueMetaInterface IsMissingProperty = new ValueMeta("", ValueMetaInterface.TYPE_STRING);
		IsMissingProperty.setName("Is property in resource?");
		IsMissingProperty.setOrigin(origin);
		rowMeta.addValueMeta(IsMissingProperty);
	}
	
	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info) {
		CheckResult cr;
		if (prev == null || prev.size() == 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, "Not receiving any fields from previous steps!", stepMeta);
			remarks.add(cr);
		}
	}
}