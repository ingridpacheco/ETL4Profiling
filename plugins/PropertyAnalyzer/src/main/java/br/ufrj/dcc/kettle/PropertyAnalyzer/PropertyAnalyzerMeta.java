/**
*
*/
package br.ufrj.dcc.kettle.PropertyAnalyzer;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaString;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

/**
* @author IngridPacheco
*
*/

public class PropertyAnalyzerMeta extends BaseStepMeta implements StepMetaInterface {

	private String DBpedia;
	private String template;
	private String property;
	private String resource;
	public String browseOutputFilename;
	public String browseOutputCSVFilename;
	private Boolean notMappedResources;
	private String resourcesProperties;
	private String inputProperty;
	private String resources;
	private String chooseInput;
	
	public PropertyAnalyzerMeta() {
		super(); // allocate BaseStepInfo
	}
	
	public String getInputProperty() {
		return inputProperty;
	}
	
	public void setInputProperty(String inputProperty) {
		this.inputProperty = inputProperty;
	}
	
	public String getChooseInput() {
		return chooseInput;
	}
	
	public void setChooseInput(String chooseInput) {
		this.chooseInput = chooseInput;
	}
	
	public String getInputResource() {
		return resources;
	}
	
	public void setInputResource(String resources) {
		this.resources = resources;
	}
	
	public String getResourceProperties() {
		return resourcesProperties;
	}
	
	public void setResourceProperties(String resourcesProperties) {
		this.resourcesProperties = resourcesProperties;
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
	
	public String getProperty() {
		return property;
	}
	
	public void setProperty(String propertyValue) {
		this.property = propertyValue;
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
	
	public StepDialogInterface getDialog( Shell shell, StepMetaInterface meta, TransMeta transMeta, String name ) {
	    return new PropertyAnalyzerDialog( shell, meta, transMeta, name );
	  }
	
	@Override
	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
	Trans trans) {
		return new PropertyAnalyzerStep(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}
	
	@Override
	public StepDataInterface getStepData() {
		return new PropertyAnalyzerData();
	}
	
	@Override
	public void setDefault() {
		setNotMappedResources(false);
		setChooseInput("Previous resources input");
	// TODO Auto-generated method stub
	}
	
	public Object clone() {
	    Object retval = super.clone();
	    return retval;
	}
	
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
		try {
			DBpedia = XMLHandler.getTagValue(stepnode,"DBPEDIA");
			template = XMLHandler.getTagValue(stepnode,"TEMPLATE");
			resourcesProperties = XMLHandler.getTagValue(stepnode,"RESOURCESPROPERTIES");
			inputProperty = XMLHandler.getTagValue(stepnode,"INPUTPROPERTY");
			resources = XMLHandler.getTagValue(stepnode,"RESOURCES");
			property = XMLHandler.getTagValue(stepnode,"PROPERTY");
			resource = XMLHandler.getTagValue(stepnode,"RESOURCE");
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
		if (inputProperty != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("INPUTPROPERTY", inputProperty));
		}
		if (template != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("TEMPLATE", template));
		}
		if (property != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("PROPERTY", property));
		}
		if (resourcesProperties != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("RESOURCESPROPERTIES", resourcesProperties));
		}
		if (resources != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("RESOURCES", resources));
		}
		if (resource != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("RESOURCE", resource));
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
			inputProperty = rep.getStepAttributeString(stepId, "INPUTPROPERTY");
			template = rep.getStepAttributeString(stepId, "TEMPLATE");
			property = rep.getStepAttributeString(stepId, "PROPERTY");
			resource = rep.getStepAttributeString(stepId, "RESOURCE");
			resourcesProperties = rep.getStepAttributeString(stepId, "RESOURCESPROPERTIES");
			resources = rep.getStepAttributeString(stepId, "RESOURCES");
			chooseInput = rep.getStepAttributeString(stepId, "CHOOSEINPUT");
			browseOutputFilename = rep.getStepAttributeString(stepId, "BROWSEOUTPUTFILENAME");
			browseOutputCSVFilename = rep.getStepAttributeString(stepId, "BROWSEOUTPUTCSVFILENAME");
			notMappedResources = rep.getStepAttributeBoolean(stepId, "NOTMAPPEDRESOURCES");
		} catch (Exception e) {
			throw new KettleException("Unexpected error reading step Sample Plug-In from the repository", e);
		}
	}
	
	public void saveRep(Repository rep, ObjectId transformationId, ObjectId stepId) throws KettleException {
		try {
			if (DBpedia != null) {
				rep.saveStepAttribute(transformationId, stepId, "DBPEDIA", DBpedia);
			}
			if (inputProperty != null) {
				rep.saveStepAttribute(transformationId, stepId, "INPUTPROPERTY", inputProperty);
			}
			if (template != null) {
				rep.saveStepAttribute(transformationId, stepId, "TEMPLATE", template);
			}
			if (property != null) {
				rep.saveStepAttribute(transformationId, stepId, "PROPERTY", property);
			}
			if (resourcesProperties != null) {
				rep.saveStepAttribute(transformationId, stepId, "RESOURCESPROPERTIES", resourcesProperties);
			}
			if (resources != null) {
				rep.saveStepAttribute(transformationId, stepId, "RESOURCES", resources);
			}
			if (resource != null) {
				rep.saveStepAttribute(transformationId, stepId, "RESOURCE", resource);
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
		
		ValueMetaInterface ResourceMeta = new ValueMetaString("");
		ResourceMeta.setName("Resource");
		ResourceMeta.setOrigin(origin);
		rowMeta.addValueMeta(ResourceMeta);
		
		ValueMetaInterface InsideResourcesMeta = new ValueMetaString("");
		InsideResourcesMeta.setName("Has the property?");
		InsideResourcesMeta.setOrigin(origin);
		rowMeta.addValueMeta(InsideResourcesMeta);
	}
	
	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info) {
		CheckResult cr;
		if (prev == null || prev.size() == 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, "Not receiving any fields from previous steps!", stepMeta);
			remarks.add(cr);
		}
	}
}