/**
*
*/
package br.ufrj.dcc.kettle.TemplateResourceAnalyzer;

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
public class TemplateResourceAnalyzerMeta extends BaseStepMeta implements StepMetaInterface {

	private String DBpedia;
	private String template;
	private String order;
	public String browseOutputFilename;
	public String browseOutputCSVFilename;
	private Boolean notMappedResources;
	
	public TemplateResourceAnalyzerMeta() {
		super(); // allocate BaseStepInfo
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
	
	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
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
		return new TemplateResourceAnalyzerStep(stepMeta, stepDataInterface, cnr, transMeta, trans);
	}
	
	@Override
	public StepDataInterface getStepData() {
		return new TemplateResourceAnalyzerData();
	}
	
	@Override
	public void setDefault() {
		notMappedResources = false;
	// TODO Auto-generated method stub
	}
	
	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
		try {
			DBpedia = XMLHandler.getTagValue(stepnode,"DBPEDIA");
			template = XMLHandler.getTagValue(stepnode,"TEMPLATE");
			order = XMLHandler.getTagValue(stepnode,"ORDER");
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
		if (browseOutputFilename != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("BROWSEOUTPUTFILENAME", browseOutputFilename));
		}
		if (browseOutputCSVFilename != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("BROWSEOUTPUTCSVFILENAME", browseOutputCSVFilename));
		}
		if (order != null) {
			retVal.append("    ").append(XMLHandler.addTagValue("ORDER", order));
		}
		retVal.append("    ").append(XMLHandler.addTagValue("NOTMAPPEDRESOURCES", notMappedResources));
		return retVal.toString();
	}
	
	public void readRep(Repository rep, ObjectId stepId, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
		try {
			DBpedia = rep.getStepAttributeString(stepId, "DBPEDIA");
			template = rep.getStepAttributeString(stepId, "TEMPLATE");
			order = rep.getStepAttributeString(stepId, "ORDER");
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
			if (template != null) {
				rep.saveStepAttribute(transformationId, stepId, "TEMPLATE", template);
			}
			if (browseOutputFilename != null) {
				rep.saveStepAttribute(transformationId, stepId, "BROWSEOUTPUTFILENAME", browseOutputFilename);
			}
			if (browseOutputCSVFilename != null) {
				rep.saveStepAttribute(transformationId, stepId, "BROWSEOUTPUTCSVFILENAME", browseOutputCSVFilename);
			}
			if (order != null) {
				rep.saveStepAttribute(transformationId, stepId, "ORDER", order);
			}
			rep.saveStepAttribute(transformationId, stepId,
	                "NOTMAPPEDRESOURCES", notMappedResources);
		} catch (Exception e) {
			throw new KettleException("Unexpected error saving step Sample Plug-In from the repository", e);
		}
	}
	
	public void getFields(RowMetaInterface rowMeta, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) throws KettleStepException {
		ValueMetaInterface ResourcesMeta = new ValueMeta("", ValueMetaInterface.TYPE_STRING);
		ResourcesMeta.setName("Resources");
		ResourcesMeta.setOrigin(origin);
		rowMeta.addValueMeta(ResourcesMeta);
		
		ValueMetaInterface ExistingPropertiesMeta = new ValueMeta("", ValueMetaInterface.TYPE_STRING);
		ExistingPropertiesMeta.setName("Existing Properties");
		ExistingPropertiesMeta.setOrigin(origin);
		rowMeta.addValueMeta(ExistingPropertiesMeta);
		
		ValueMetaInterface MissingPropertiesMeta = new ValueMeta("", ValueMetaInterface.TYPE_STRING);
		MissingPropertiesMeta.setName("Missing Properties");
		MissingPropertiesMeta.setOrigin(origin);
		rowMeta.addValueMeta(MissingPropertiesMeta);
		
		ValueMetaInterface TotalMeta = new ValueMeta("", ValueMetaInterface.TYPE_STRING);
		TotalMeta.setName("Total");
		TotalMeta.setOrigin(origin);
		rowMeta.addValueMeta(TotalMeta);
		
		ValueMetaInterface PercentageMeta = new ValueMeta("", ValueMetaInterface.TYPE_STRING);
		PercentageMeta.setName("Completeness Percentage (%)");
		PercentageMeta.setOrigin(origin);
		rowMeta.addValueMeta(PercentageMeta);
	}
	
	public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info) {
		CheckResult cr;
		if (prev == null || prev.size() == 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_WARNING, "Not receiving any fields from previous steps!", stepMeta);
			remarks.add(cr);
		}
	}
}