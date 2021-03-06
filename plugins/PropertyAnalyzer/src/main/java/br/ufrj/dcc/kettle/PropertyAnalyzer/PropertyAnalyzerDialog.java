/**
*
*/
package br.ufrj.dcc.kettle.PropertyAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.widget.ComboVar;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

import br.ufrj.ppgi.greco.kettle.plugin.tools.swthelper.SwtHelper;

/**
* @author IngridPacheco
*
*/
public class PropertyAnalyzerDialog extends BaseStepDialog implements StepDialogInterface {
	
	private static Class<?> PKG = PropertyAnalyzerMeta.class;
	
	private PropertyAnalyzerMeta PropertyAnalyzer;
	private SwtHelper swthlp;
	private String dialogTitle;
	
	private Group wFirstInputGroup;
	private ComboVar wChooseInput;
	private Group wSecondInputGroup;
	private ComboVar wInputResource;
	private ComboVar wResourceProperties;
	private ComboVar wPropertyField;
	private ComboVar wDBpedia;
	private ComboVar wTemplate;
	private Button wGetNotMappedResources;
	private ComboVar wProperty;
	
	private Group wOutputGroup;
	private ComboVar wResource;
	private TextVar wOutputBrowse;
	private TextVar wOutputCSVBrowse;
	
	private String[] DBpediaValues = {
			"pt", "en", "ja",
			"ar", "az", "be",
			"bg", "bn", "ca",
			"ceb", "Commons", "cs",
			"cy", "da", "de",
			"el", "en", "eo",
			"es", "et", "eu",
			"fa", "fi", "fr",
			"ga", "gl", "hi",
			"hr", "hu", "hy",
			"id", "it", "ko",
			"lt", "lv", "mk",
			"mt", "nl", "pl",
			"pt", "ru", "ro",
			"sk", "sl", "sr",
			"sv", "tr", "uk",
			"ur", "vi", "war",
			"zh"
	};
	private String[] TemplateValues;
	private String[] PropertyValues;
	private String[] CheckResources = {"Has property", "Doesn't have property", "All"};
	
	public PropertyAnalyzerDialog(Shell parent, Object in, TransMeta tr, String sname) {
		super(parent, (BaseStepMeta) in, tr, sname);
		PropertyAnalyzer = (PropertyAnalyzerMeta) in;
		swthlp = new SwtHelper(tr, this.props);
		dialogTitle = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.Title");
	}
	
	public String[] getTemplateValues(String DBpedia){
		try {
			String url = String.format("http://mappings.dbpedia.org/index.php/Mapping_%s", DBpedia.toLowerCase());
			Document doc = Jsoup.connect(url).get();
			Elements mappings = doc.select(String.format("a[href^=\"/index.php/Mapping_%s:\"]", DBpedia.toLowerCase()));
			
			TemplateValues = new String[mappings.size()];
			for (int i = 0; i < mappings.size(); i++) {
				String templateMapping = mappings.get(i).text();
				TemplateValues[i] = templateMapping.split(":")[1];
			}
			
		  	return TemplateValues;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  	TemplateValues = new String[1];
		  	TemplateValues[0] = "";
		  	return TemplateValues;
		}
	}
	
	public String[] getPropertyValues(String DBpedia, String template) {
		List<String> templateProperties = new ArrayList<>();
		template = template.replaceAll(" ", "_");
		try {
			String url = String.format("http://mappings.dbpedia.org/index.php/Mapping_%s:%s", DBpedia, template);
			Document doc = Jsoup.connect(url).get();
			Elements properties = doc.select("td[width=\"400px\"]");
			
			for (int i = 0; i < properties.size(); i++) {
				String templateProperty = properties.get(i).text();
				String[] propertyName = templateProperty.split("\\s|_|-");
	
			    Integer size = propertyName.length - 1;
			    Integer counter = 1;
	
			    String newString = propertyName[0];

			    while(size > 0){
			        String newPropertyName = propertyName[counter].substring(0,1).toUpperCase().concat(propertyName[counter].substring(1));
			        System.out.println(newPropertyName);
			        newString = newString.concat(newPropertyName);
			        System.out.println(newString);
			        counter = counter + 1;
			        size = size - 1;
			    }

				templateProperties.add(newString);
			}
			
			templateProperties.remove(0);
			
			PropertyValues = templateProperties.toArray(new String[templateProperties.size()]);
			
		  	return PropertyValues;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			PropertyValues = new String[1];
			PropertyValues[0] = "";
			
		  	return PropertyValues;
		}		
	}
	
	private ComboVar appendComboVar(Control lastControl, ModifyListener defModListener, Composite parent,
			String label) {
		ComboVar combo = swthlp.appendComboVarRow(parent, lastControl, label, defModListener);
		BaseStepDialog.getFieldsFromPrevious(combo, transMeta, stepMeta);
		return combo;
	}
	
	private TextVar textVarWithButton(Composite parent, Control lastControl, String label, ModifyListener lsMod,
			String btnLabel, SelectionListener listener) {
		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;
		Label wLabel = new Label(parent, SWT.RIGHT);
		wLabel.setText(label);
		props.setLook(wLabel);
		FormData fdLabel = new FormData();
		fdLabel.left = new FormAttachment(0, 0);
		fdLabel.top = new FormAttachment(lastControl, margin);
		fdLabel.right = new FormAttachment(middle, -margin);
		wLabel.setLayoutData(fdLabel);

		Button button = new Button(parent, SWT.PUSH | SWT.CENTER);
		props.setLook(button);
		button.setText(btnLabel);
		FormData fdButton = new FormData();
		fdButton.right = new FormAttachment(100, 0);
		fdButton.top = new FormAttachment(lastControl, margin);
		button.setLayoutData(fdButton);

		TextVar text = new TextVar(transMeta, parent, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(text);
		text.addModifyListener(lsMod);
		FormData fdText = new FormData();
		fdText.left = new FormAttachment(middle, 0);
		fdText.right = new FormAttachment(button, -margin);
		fdText.top = new FormAttachment(lastControl, margin);
		text.setLayoutData(fdText);

		button.addSelectionListener(listener);
		return text;
	}
	
	private void fileDialogFunction(int type, String[] fileExtensions, TextVar receptor, String[] filterNames) {
		FileDialog dialog = new FileDialog(shell, type);
		dialog.setFilterExtensions(fileExtensions);
		if (receptor.getText() != null) {
			dialog.setFileName(receptor.getText());
		}

		dialog.setFilterNames(filterNames);

		if (dialog.open() != null) {
			String str = dialog.getFilterPath() + System.getProperty("file.separator") + dialog.getFileName();
			receptor.setText(str);
		}
	}
	
	private Control buildContents(Control lastControl, ModifyListener defModListener) {
		CTabFolder wTabFolder = swthlp.appendTabFolder(shell, lastControl, 90);
		CTabItem item = new CTabItem(wTabFolder, SWT.NONE);
		item.setText(BaseMessages.getString(PKG, "PropertiesAnalyzerStep.Tab.InputFields"));
		Composite cpt = swthlp.appendComposite(wTabFolder, lastControl);
		
		String chooseInputField = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.ChooseInput.Label");
		wChooseInput = appendComboVar(null, defModListener, cpt, chooseInputField);
		wChooseInput.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
			}

			public void focusGained(FocusEvent e) {
				Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
				shell.setCursor(busy);
				shell.setCursor(null);
				wChooseInput.setItems(new String[] {"Previous resources input", "Get resources automatically"});
				busy.dispose();
			}
		});
		wChooseInput.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				widgetSelected(arg0);
			}

			public void widgetSelected(SelectionEvent e) {
				chooseInputField(wChooseInput.getText());
				PropertyAnalyzer.setChanged(true);
			}
		});
		
		String inputFieldName = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.FirstInputField.Label");
		wFirstInputGroup = swthlp.appendGroup(cpt, wChooseInput, inputFieldName);
		
		String resourceField = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.ResourceField.Label");
		wInputResource = appendComboVar(wFirstInputGroup, defModListener, wFirstInputGroup, resourceField);
		wInputResource.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
			}

			public void focusGained(FocusEvent e) {
				Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
				shell.setCursor(busy);
				shell.setCursor(null);
				wInputResource.setItems(getFields(ValueMetaInterface.TYPE_STRING));
				busy.dispose();
			}
		});
		
		String resourcePropertyField = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.ResourcePropertyField.Label");
		wResourceProperties = appendComboVar(wInputResource, defModListener, wFirstInputGroup, resourcePropertyField);
		wResourceProperties.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
			}

			public void focusGained(FocusEvent e) {
				Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
				shell.setCursor(busy);
				shell.setCursor(null);
				wResourceProperties.setItems(getFields(ValueMetaInterface.TYPE_STRING));
				busy.dispose();
			}
		});
		
		String propertyField = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.InputPropertyField.Label");
		wPropertyField = appendComboVar(wResourceProperties, defModListener, wFirstInputGroup, propertyField);
		wPropertyField.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
			}

			public void focusGained(FocusEvent e) {
				Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
				shell.setCursor(busy);
				shell.setCursor(null);
				wPropertyField.setItems(getFields(ValueMetaInterface.TYPE_STRING));
				busy.dispose();
			}
		});
		
		String secondInputFieldName = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.SecondInputFields.Label");
		wSecondInputGroup = swthlp.appendGroup(cpt, wFirstInputGroup, secondInputFieldName);
		
		String DBpediaLabel = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.DBpediaField.Label");
		wDBpedia = appendComboVar(wSecondInputGroup, defModListener, wSecondInputGroup, DBpediaLabel);
		wDBpedia.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
			}

			public void focusGained(FocusEvent e) {
				Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
				shell.setCursor(busy);
				wDBpedia.setItems(DBpediaValues);
				shell.setCursor(null);
				busy.dispose();
			}
		});
		
		String templateLabel = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.TemplateField.Label");
		wTemplate = appendComboVar(wDBpedia, defModListener, wSecondInputGroup, templateLabel);
		wTemplate.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
			}

			public void focusGained(FocusEvent e) {
				Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
				shell.setCursor(busy);
				shell.setCursor(null);
				wTemplate.setItems(getTemplateValues(wDBpedia.getText()));
				busy.dispose();
			}
		});
		
		String propertyLabel = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.PropertyField.Label");	
		wProperty = appendComboVar(wTemplate, defModListener, wSecondInputGroup, propertyLabel);
		wProperty.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
			}

			public void focusGained(FocusEvent e) {
				Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
				shell.setCursor(busy);
				shell.setCursor(null);
				wProperty.setItems(getPropertyValues(wDBpedia.getText(), wTemplate.getText()));
				busy.dispose();
			}
		});
		
		String notMappedResourcesLabel = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.GetNotMappedFields.Label");	
		wGetNotMappedResources = swthlp.appendCheckboxRow(wSecondInputGroup, wProperty, notMappedResourcesLabel,
				new SelectionListener() {
	            @Override
	            public void widgetSelected(SelectionEvent arg0)
	            {
	            	PropertyAnalyzer.setChanged();
	            }
	
	            @Override
	            public void widgetDefaultSelected(SelectionEvent arg0)
	            {
	            	PropertyAnalyzer.setChanged();
	            }
        	});
		
		item.setControl(cpt);
		item = new CTabItem(wTabFolder, SWT.NONE);
		item.setText(BaseMessages.getString(PKG, "PropertiesAnalyzerStep.Tab.OutputFields"));
		cpt = swthlp.appendComposite(wTabFolder, lastControl);

		String outputLabel = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.OutputFields.Label");
		wOutputGroup = swthlp.appendGroup(cpt, null, outputLabel);
		
		String resourceLabel = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.Resource.Label");
		wResource = appendComboVar(wOutputGroup, defModListener, wOutputGroup, resourceLabel);
		wResource.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
			}

			public void focusGained(FocusEvent e) {
				Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
				shell.setCursor(busy);
				shell.setCursor(null);
				wResource.setItems(CheckResources);
				busy.dispose();
			}
		});
		
		String outputReportLabel = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.OutputReport.Label");
		wOutputBrowse = textVarWithButton(wOutputGroup, wResource, outputReportLabel,
				defModListener, BaseMessages.getString(PKG, "PropertiesAnalyzerStep.Btn.Browse"), new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						fileDialogFunction(SWT.OPEN, new String[] { "*.txt; *.TXT" },
								wOutputBrowse, new String[] { ".(txt) files" });
					}
				});
		
		String outputCSVLabel = BaseMessages.getString(PKG, "PropertiesAnalyzerStep.OutputCSVBrowse.Label");
		wOutputCSVBrowse = textVarWithButton(wOutputGroup, wOutputBrowse, outputCSVLabel,
				defModListener, BaseMessages.getString(PKG, "PropertiesAnalyzerStep.Btn.Browse"), new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						fileDialogFunction(SWT.OPEN, new String[] { "*.csv; *.CSV" },
								wOutputCSVBrowse, new String[] { ".(csv) files" });
					}
				});

		item.setControl(cpt);
		
		wTabFolder.setSelection(0);

		return wTabFolder;
	}
	
	private void chooseInputField(String choice) {
		Boolean enabled = (choice.equals("Previous resources input")) ? true : false;
		wInputResource.setEnabled(enabled);
		wResourceProperties.setEnabled(enabled);
		wGetNotMappedResources.setEnabled(!enabled);
		wDBpedia.setEnabled(!enabled);
		wTemplate.setEnabled(!enabled);
		wProperty.setEnabled(!enabled);
		wPropertyField.setEnabled(enabled);
	}
	
	private String[] getFields(int type) {

		List<String> result = new ArrayList<String>();

		try {
			RowMetaInterface inRowMeta = this.transMeta.getPrevStepFields(stepname);

			List<ValueMetaInterface> fields = inRowMeta.getValueMetaList();

			for (ValueMetaInterface field : fields) {
				if (field.getType() == type || type == -1)
					result.add(field.getName());
			}

		} catch (KettleStepException e) {
			e.printStackTrace();
		}

		return result.toArray(new String[result.size()]);
	}
	
	/**
	   * This method is called by Spoon when the user opens the settings dialog of the step.
	   * It should open the dialog and return only once the dialog has been closed by the user.
	   *  
	   * If the user confirms the dialog, the meta object (passed in the constructor) must
	   * be updated to reflect the new step settings. The changed flag of the meta object must  
	   * reflect whether the step configuration was changed by the dialog.
	   *  
	   * If the user cancels the dialog, the meta object must not be updated, and its changed flag
	   * must remain unaltered.
	   *  
	   * The open() method must return the name of the step after the user has confirmed the dialog,
	   * or null if the user cancelled the dialog.
	   */
	@Override
	public String open() {
		Shell parent = getParent();
		Display display = parent.getDisplay();
		
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
		props.setLook(shell);
		setShellImage(shell, PropertyAnalyzer);
		
		ModifyListener lsMod = new ModifyListener() {
		
			public void modifyText(ModifyEvent e) {
				PropertyAnalyzer.setChanged();
			}
		};
	
		changed = PropertyAnalyzer.hasChanged();
		
		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;
		
		shell.setLayout(formLayout);
		shell.setText(dialogTitle);// Messages.getString(“KafkaTopicPartitionConsumerDialog.Shell.Title”));
		
		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;
		
		// Step name
		wlStepname = new Label(shell, SWT.RIGHT);
		wlStepname.setText("Step Name");// Messages.getString(“KafkaTopicPartitionConsumerDialog.StepName.Label”));
		props.setLook(wlStepname);
		fdlStepname = new FormData();
		fdlStepname.left = new FormAttachment(0, 0);
		fdlStepname.right = new FormAttachment(middle, -margin);
		fdlStepname.top = new FormAttachment(0, margin);
		wlStepname.setLayoutData(fdlStepname);
		wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wStepname);
		wStepname.addModifyListener(lsMod);
		fdStepname = new FormData();
		fdStepname.left = new FormAttachment(middle, 0);
		fdStepname.top = new FormAttachment(0, margin);
		fdStepname.right = new FormAttachment(100, 0);
		wStepname.setLayoutData(fdStepname);
		Control lastWidget = wStepname;
		
		lastWidget = buildContents(lastWidget, lsMod);
		
		// Buttons
		wOK = new Button(shell, SWT.PUSH);
		wOK.setText(BaseMessages.getString(PKG, "PropertiesAnalyzerStep.Btn.OK")); //$NON-NLS-1$
		wCancel = new Button(shell, SWT.PUSH);
		wCancel.setText(BaseMessages.getString(PKG, "PropertiesAnalyzerStep.Btn.Cancel")); //$NON-NLS-1$
		
		setButtonPositions(new Button[] { wOK, wCancel }, margin, null);
		
		lsCancel = new Listener() {
			public void handleEvent(Event e) {
				cancel();
			}
		};
		lsOK = new Listener() {
			public void handleEvent(Event e) {
				ok();
			}
		};
		wCancel.addListener(SWT.Selection, lsCancel);
		wOK.addListener(SWT.Selection, lsOK);
		
		lsDef = new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				ok();
			}
		};
		
		// Set the shell size, based upon previous time…
		setSize(shell, 200, 150, true);
		
		// populate the dialog with the values from the meta object
		getData();
		
		// restore the changed flag to original value, as the modify listeners fire during dialog population  
		PropertyAnalyzer.setChanged( changed );
		
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
		return stepname;
	}
	
	/**
	* Called when the user confirms the dialog
	*/
	private void ok() {
		if (StringUtils.isEmpty(wStepname.getText())) {
			return;
		}
		setData();
		dispose();
	}
	
	/**
	* Called when the user cancels the dialog.  
	*/
	private void cancel() {
		// The "stepname" variable will be the return value for the open() method.  
	    // Setting to null to indicate that dialog was cancelled.
		stepname = null;
		// Restoring original "changed" flag on the met aobject
		PropertyAnalyzer.setChanged(changed);
		// close the SWT dialog window
		dispose();
	}
	
	/**
	* Copy information from the meta-data input to the dialog fields.
	*/
	private void getData() {
		wStepname.setText(stepname);
		if (PropertyAnalyzer.getDBpedia() != null)
			wDBpedia.setText(PropertyAnalyzer.getDBpedia());
		if (PropertyAnalyzer.getTemplate() != null)
			wTemplate.setText(PropertyAnalyzer.getTemplate());
		if (PropertyAnalyzer.getProperty() != null)
			wProperty.setText(PropertyAnalyzer.getProperty());
		if (PropertyAnalyzer.getResource() != null)
			wResource.setText(PropertyAnalyzer.getResource());
		if (PropertyAnalyzer.getOutputFile() != null)
			wOutputBrowse.setText(PropertyAnalyzer.getOutputFile());
		if (PropertyAnalyzer.getOutputCSVFile() != null)
			wOutputCSVBrowse.setText(PropertyAnalyzer.getOutputCSVFile());
		wGetNotMappedResources.setSelection(PropertyAnalyzer.getNotMappedResources());
		
		if (PropertyAnalyzer.getResourceProperties() != null)
			wResourceProperties.setText(PropertyAnalyzer.getResourceProperties());
		if (PropertyAnalyzer.getInputProperty() != null)
			wPropertyField.setText(PropertyAnalyzer.getInputProperty());
		if (PropertyAnalyzer.getInputResource() != null)
			wInputResource.setText(PropertyAnalyzer.getInputResource());
		if (PropertyAnalyzer.getChooseInput() != null)
			wChooseInput.setText(PropertyAnalyzer.getChooseInput());
		
		chooseInputField(wChooseInput.getText());
	}
	
	/**
	* Copy information from the dialog fields to the meta-data input
	*/
	private void setData() {
		// The "stepname" variable will be the return value for the open() method.  
	    // Setting to step name from the dialog control
		stepname = wStepname.getText();
		
		// Setting the  settings to the meta object
		PropertyAnalyzer.setResourceProperties(wResourceProperties.getText());
		PropertyAnalyzer.setInputResource(wInputResource.getText());
		PropertyAnalyzer.setInputProperty(wPropertyField.getText());
		PropertyAnalyzer.setDBpedia(wDBpedia.getText());
		PropertyAnalyzer.setTemplate(wTemplate.getText());
		PropertyAnalyzer.setProperty(wProperty.getText());
		PropertyAnalyzer.setResource(wResource.getText());
		PropertyAnalyzer.setOutputFile(wOutputBrowse.getText());
		PropertyAnalyzer.setOutputCSVFile(wOutputCSVBrowse.getText());
		PropertyAnalyzer.setNotMappedResources(wGetNotMappedResources.getSelection());
		PropertyAnalyzer.setChooseInput(wChooseInput.getText());
		// close the SWT dialog window
		PropertyAnalyzer.setChanged();
	}
}