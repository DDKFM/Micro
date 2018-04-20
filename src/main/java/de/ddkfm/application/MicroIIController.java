package de.ddkfm.application;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import de.ddkfm.models.*;
import de.ddkfm.util.transform.Microcode;
import de.ddkfm.util.transform.Program;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.fxmisc.richtext.InlineCssTextArea;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.ddkfm.util.MicroIIUtils;
import de.ddkfm.util.ThemeUtils;
import de.ddkfm.util.XMLLoader;
import de.ddkfm.util.transform.Micro2Data;
import de.ddkfm.util.transform.Transformator;
import de.ddkfm.views.ProcessorFX;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * The Controller for the Main Window
 * */
public class MicroIIController implements Initializable {

	@FXML
	private Menu miPrint;

	@FXML
	private MenuItem miSaveProgram;

	@FXML
	private MenuItem miEditProgramFlow;

	@FXML
	private MenuItem miSaveMicrocode;

	@FXML
	private MenuItem miAbout;

	@FXML
	private MenuItem miOpenMicrocode;

	@FXML
	private Menu miSettings;

	@FXML
	private MenuItem miSaveMicrocodeAs;

	@FXML
	private MenuItem miEditMicrocode;

	@FXML
	private MenuItem miOpenProgram;

	@FXML
	private MenuItem miSaveProgramAs;

	@FXML
	private MenuItem miSaveAll;

	@FXML
	private MenuItem miEditProgramm;

	@FXML
	private BorderPane mainPane;

	@FXML
	private MenuItem miPrintPrintProgram;

	@FXML
	private MenuItem miPrintPrintSnapshot;

	@FXML
	private MenuItem miPrintSaveSnapshotAsPicture;

	@FXML
	private MenuItem miDocumentation;

	private Logger logger = LogManager.getRootLogger();
	private Processor processor;
	private ProcessorFX processorFX;

	private Stage programFlowStage;
	private Stage programStage;
	private Stage loadFromServerStage;
	private Stage microcodeStage;
	private Stage aboutStage;

	private Properties serverProperties = new Properties();
	private List<String> serverURLs = new ArrayList<String>();
	private boolean consoleIsShowed = false;
	private String[] commandTemplates = {
			"set <LogicValue>{[index]} = (true|false)",
			"get <LogicValue> {index}",
			"typeof <LogicValue>",
			"execute <LogicValue>.<Methodname>({<MethodParameters>})"
	};
	private List<String> history = new ArrayList<String>();
	private int historyIndex = -1;
	private String result;

	private File lastSavedProgramFile = null;
	private File lastSavedMicrocodeFile = null;

	private WritableImage currentSnapshot = null;

	/**
	 * the Init-Method for the Controller
	 * */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			mainPane.getStylesheets().add(ThemeUtils.getCSSFile().toString());
		} catch (MalformedURLException e) {
			logger.error("Fehler beim Laden des Default-Stylesheets: ",e);
		}
		XMLLoader xmlLoader = new XMLLoader(MicroIIUtils.getAttributesJSON());//new XMLLoader(MicroIIUtils.getAttributesXML());
		processor = new Processor(xmlLoader.getXMLElements());
		processorFX = new ProcessorFX(xmlLoader.getXMLElements(), processor);
		mainPane.setCenter(processorFX);
		MenuItem dummyButtonForConsole = new MenuItem("Konsole öffnen");
		dummyButtonForConsole.setVisible(false);
		dummyButtonForConsole.setOnAction(e -> {
			if(!consoleIsShowed)
				openConsole();
			else
				closeConsole();
			consoleIsShowed = !consoleIsShowed;
		});
		dummyButtonForConsole.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
		miPrint.getItems().addAll(dummyButtonForConsole);
		addMenuActions();
	}
	/**
	 * open the Console if the user is pressing CTRL+C
	 * */
	private void openConsole() {

		VBox console = new VBox();
		TextField inputField = new TextField("");
		Button executeButton = new Button("Kommando ausführen");
		InlineCssTextArea consoleWindow = new InlineCssTextArea("$ >MicroII Console Version " + MicroIIUtils.VERSION + ">\n");
		consoleWindow.setPrefHeight(200);
		consoleWindow.setStyle("-fx-background-color: black; ");
		consoleWindow.setStyle(0, consoleWindow.getLength(), "-fx-fill: lightgreen");
		inputField.setOnKeyPressed(event -> {
			if(event.getCode().equals(KeyCode.ENTER)) {
				executeButton.fire();
			} else
			if(event.getCode().equals(KeyCode.UP)) {
				if (!history.isEmpty()) {
					inputField.setText(history.get(historyIndex));
					historyIndex = historyIndex > 0 ? historyIndex - 1 : 0;
				}
			} else
			if(event.getCode().equals(KeyCode.DOWN)) {
				if (!history.isEmpty()) {
					historyIndex = historyIndex < history.size() - 1 ? historyIndex + 1 : historyIndex;
					inputField.setText(history.get(historyIndex));
				}
			}
			AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(inputField, textField -> {
				String command = inputField.getText();
				Set<String> autoCompletion = new LinkedHashSet<String>();
				if(command.matches("set\\s[A-Za-z0-9_]*")
						|| command.matches("get\\s[A-Za-z0-9_]*")
						|| command.matches("typeof\\s[A-Za-z0-9_]*")
						|| command.matches("execute\\s[A-Za-z0-9_]*")) {
					for(String key : processor.getAllLogicValues().keySet()) {
						String currentLogicValueName = command.split(" ").length > 1 ? command.split(" ")[1].trim() : "";
						if(key.startsWith(currentLogicValueName))
							autoCompletion.add(command.split(" ")[0] + " " + key);
					}
				} else
				if(command.matches("get\\s[A-Za-z0-9]*\\s\\d*")) {
					String logicValueName = command.split(" ")[1];
					LogicValue logicValue = processor.getLogicValueByName(logicValueName);
					if(logicValue != null) {
						for(int i = 0 ; i < logicValue.getValueCount() ; i++)
							autoCompletion.add("get " + logicValueName + " " + Integer.toString(i));
					}
				} else
				if(command.matches("set\\s[A-Za-z0-9_]*\\[(\\d+\\])?") ) {
					String logicValueName = command.split(" ")[1];
					logicValueName = logicValueName.replace("[","");
					LogicValue logicValue = processor.getLogicValueByName(logicValueName);
					if(logicValue != null) {
						for(int i = 0 ; i < logicValue.getValueCount() ; i++)
							autoCompletion.add(command + Integer.toString(i) + "]");
					}
				}
				else
				if(command.matches("set\\s[A-Za-z0-9_]*(\\[\\d+\\])?\\s(=[A-Za-z0-1]*)?")) {
					if(command.contains("]")) {
						autoCompletion.add(command.substring(0, command.indexOf("]") + 1) + " = true");
						autoCompletion.add(command.substring(0, command.indexOf("]") + 1) + " = false");
						autoCompletion.add(command.substring(0, command.indexOf("]") + 1) + " = 42");
					} else {
						autoCompletion.add(command.split(" ")[0] + " " + command.split(" ")[1] + " = true");
						autoCompletion.add(command.split(" ")[0] + " " + command.split(" ")[1] + " = false");
					}
				} else
				if(command.matches("execute\\s[A-Za-z0-9_]*\\.[A-Za-z0-9]*")) {
					String logicValueName = command.substring(command.indexOf(" ") + 1, command.indexOf("."));
					String currentMethodName = command.substring(command.indexOf(".") + 1);
					LogicValue logicValue = processor.getLogicValueByName(logicValueName);
					String prefix = command.substring(0, command.indexOf("."));
					if(logicValue != null) {
						Class logicValueClass = logicValue.getClass();
						for(Method method : logicValueClass.getMethods()) {
							if(method.getName().startsWith(currentMethodName)) {
								String parameter = "";
								for(int p = 0; p < method.getParameterTypes().length ; p++) {
									parameter += method.getParameterTypes()[p].getSimpleName() +
											" " + method.getParameters()[p].getName()
											+ (p < method.getParameterTypes().length - 1 ? ", " : "");
								}
								autoCompletion.add(prefix + "." + method.getName() + "(" + parameter + ")");
							}
						}
					}
				} else {
					autoCompletion = new TreeSet<String>();
					//autoCompletion.add("set <LogicValue>{[index]} = (true|false)");
					//autoCompletion.add("get <LogicValue> {index}");
					//autoCompletion.add("typeof <LogicValue>");
					//autoCompletion.add("execute <LogicValue>.<Methodname>({<MethodParameters>})");
				}
				return autoCompletion;
			});
			binding.setVisibleRowCount(4);
			binding.setMinWidth(250);
			binding.setDelay(500);
		});
		executeButton.setOnAction(e -> {
			executeCommand(inputField.getText(),consoleWindow);
			inputField.clear();
		});
		SplitPane splitPane = new SplitPane(inputField, executeButton);
		console.getChildren().addAll(splitPane,consoleWindow);
		mainPane.setBottom(console);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				inputField.requestFocus();
			}
		});

	}
	/**
	 * Close the Console by pressing CTRL+C while the console is open
	 * */
	private void closeConsole() {
		mainPane.setBottom(null);
	}
	/**
	 * Execute a command as String on the Console
	 * @param command the given command
	 * @param console the Textarea on which the Result of the command will be displayed
	 * */
	private void executeCommand(String command, InlineCssTextArea console) {
		command = command.trim();
		String regexGet = "(?i)get(?-i)\\s[A-Za-z0-9_]+(\\s\\d+)?";
		String regexTypeof = "(?i)typeof(?-i)\\s[A-Za-z0-9_]+";
		String regexSet = "(?i)set(?-i)\\s[A-Za-z0-9_]+(\\[\\d+\\])?(\\s)?=(\\s)?(?i)(true|false|on|off|0|1|42)(?-i)";
		String regexExecute = "(?i)execute(?-i)\\s[A-Za-z0-9_]+\\.[A-Za-z0-9]+\\((([A-Za-z0-9_]+,\\s?)*([A-Za-z0-9_]+))?\\)";
		String regexClear = "clear\\s*";
		result = "$ > ";
		if(command.matches(regexGet) || command.matches(regexTypeof) ||
				command.matches(regexSet) || command.matches(regexExecute) || command.matches(regexClear)) {
			String[] parts = command.split(" ");
			String logicValueName = "";
			LogicValue logicValue = null;
			switch(parts[0].toLowerCase()) {
				case "clear":
					console.clear();
					break;
				case "get":
					logicValueName = parts[1];
					logicValue = processor.getLogicValueByName(logicValueName);
					if(logicValue != null) {
						result += "\tQuery Result: \n";
						if(parts.length > 2) {
							int i = Integer.parseInt(parts[2]);
							result += "   > " + i + ": " + logicValue.getValue(i) + "\n";
						} else
							for (int i = 0; i < logicValue.getValueCount(); i++) {
								result += "   > " + i + ": " + logicValue.getValue(i) + "\n";
							}
					}
					break;
				case "set":
					logicValueName = parts[1];
					if(logicValueName.contains("["))
						logicValueName = logicValueName.substring(0, logicValueName.lastIndexOf("["));
					logicValue = processor.getLogicValueByName(logicValueName);
					if(logicValue != null) {
						if(parts[1].contains("[")) {
							String part1 = parts[1];
							String index = part1.substring(part1.lastIndexOf("[") + 1, part1.lastIndexOf("]"));
							int i = Integer.parseInt(index);
							if(i >= logicValue.getValueCount())
								result += "LogicValue has no Index " + i + "\n";
							else {
								String valueString = parts[parts.length - 1];
								boolean value = valueString.matches("(?i)(true|on|1)(?-i)");
								if(valueString.matches("\\s*42\\s*")) {
									result += "Sie haben die Antwort auf die große Frage nach dem Leben\n" +
											  "  > dem Universum und allem anderen eingegeben\n" +
											  "  > UPDATE ALL";
									for(LogicValue lv : processor.getAllLogicValues().values()) {
										for(int valueNumber = 0 ; valueNumber < lv.getValueCount() - 1 ; valueNumber++) {
											lv.setValue(valueNumber, true);
										}
									}
									DigitalDisplay display0 = (DigitalDisplay) processor.getLogicValueByName("display0");
									display0.setSegments(true,true,true,true,true,true,false);
									DigitalDisplay display1 = (DigitalDisplay) processor.getLogicValueByName("display1");
									display1.setSegments(true,true,true,true,true,true,false);
									DigitalDisplay display2 = (DigitalDisplay) processor.getLogicValueByName("display2");
									display2.setSegments(false,true,true,false,false,true,true);
									DigitalDisplay display3 = (DigitalDisplay) processor.getLogicValueByName("display3");
									display3.setSegments(true,true,false,true,true,false,true);
								} else {
									logicValue.setValue(i, value);
									result += "LogicValue " + logicValue.getName() + "[" + i + "] was set to " + value;
								}
							}
						} else {
							result += "Update Result:\n";
							for (int i = 0; i < logicValue.getValueCount(); i++) {
								String valueString1 = parts[parts.length - 1];
								boolean value = valueString1.matches("(?i)(true|on|1)(?-i)");
								logicValue.setValue(i, value);
								result += "  > LogicValue " + logicValue.getName() + "[" + i + "] was set to " + value + "\n";
							}
						}
					} else {
						result += "The LogicValue \"" + logicValueName + " does not exist";
					}
					break;
				case "execute":
					logicValueName = command.split(" ")[1].split("\\.")[0];
					String methodName = command.substring(command.indexOf(".") + 1, command.indexOf("("));
					String parameters = command.substring(command.indexOf("(") + 1, command.indexOf(")"));

					List<Class> parameterTypes = new ArrayList<Class>();
					List<Object> parameterValues = new ArrayList<Object>();

					if (parameters != null && !parameters.isEmpty()) {
						for(String parameter : parameters.split(", ")) {
							String value = parameter.trim();
							if(value.matches("\\d+")) {
								parameterTypes.add(int.class);
								parameterValues.add(Integer.parseInt(value));
							}
							else
							if(value.matches("(?i)(true|false)(?-i)")) {
								parameterTypes.add(boolean.class);
								parameterValues.add(Boolean.parseBoolean(value));
							} else
							if(processor.getLogicValueByName(value) != null) {
								parameterTypes.add(LogicValue.class);
								parameterValues.add(processor.getLogicValueByName(value));
							} else {
								parameterTypes.add(String.class);
								parameterValues.add(value);
							}
						}
					}
					logicValue = processor.getLogicValueByName(logicValueName);
					Class clazz = logicValue.getClass();
					Method method = null;
					Class[] parameterTypesAsArray = new Class[0];
					try {
						method = clazz.getMethod(methodName,parameterTypes.toArray(parameterTypesAsArray));
						Object invocationResult = method.invoke(logicValue, parameterValues.toArray());
						result += "Execute \"" + methodName + "\" returns " + invocationResult;
					} catch (Exception e) {
						result += "Method \"" + methodName + "\" could not be found";
					}
					break;
				case "typeof":
					logicValueName = parts[1];
					logicValue = processor.getLogicValueByName(logicValueName);
					if(logicValue != null) {
						result += "\"" + logicValue.getName() + "\"" + " has the type " + logicValue.getClass().getSimpleName();
					} else {
						result += "The LogicValue \"" + logicValueName + " does not exist";
					}
					break;

			}
		} else {
			result += "Syntaxerror while processing the command, use the following commands: \n";
			for(String commandTemplate : commandTemplates) {
				result += "   > " + commandTemplate + "\n";
			}
		}
		console.appendText(result + "\n");
		console.moveTo(console.getText().length());
		//console.positionCaret(console.getText().length());
		console.setStyle(0, console.getLength(),"-fx-fill: lightgreen");
		history.add(command);
		historyIndex = history.size() - 1;
	}
	/**
	 * add the functions the the MenuItems
	 * */
	private void addMenuActions() {
		miSaveProgramAs.setOnAction(e->{
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(System.getProperty("user.home")));
			fc.getExtensionFilters().add(new ExtensionFilter("Programfiles", Arrays.asList(new String[]{"*.xml","*.prog"})));
			File saveFile = fc.showSaveDialog(null);
			if(!saveFile.exists())
				try {
					saveFile.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			if(saveFile != null & saveFile.isFile()){
				Memory mem = (Memory) processor.getLogicValueByName("memory");
				Decoder dec = (Decoder) processor.getLogicValueByName("decoder");

				mem.acceptMemory();
				Transformator transe = new Transformator(new Micro2Data("Autor", new Date(), "Ein Programm", mem.getProgram(), dec.getMicrocode()));

				try {
					Document xmlDoc = transe.getProgramData();

					OutputFormat format = new OutputFormat(xmlDoc);
					format.setLineWidth(65);
					format.setIndenting(true);
					format.setIndent(2);
					FileOutputStream out = new FileOutputStream(saveFile);
					XMLSerializer serializer = new XMLSerializer(out, format);
					serializer.serialize(xmlDoc);
					out.close();
					lastSavedProgramFile = saveFile;
				} catch (Exception ex) {
					logger.error("Fehler beim Schreiben der XML-Datei: ",ex);
				}
			}
		});
		miSaveProgram.setOnAction( e -> {
			if(lastSavedProgramFile != null) {
				Memory mem = (Memory) processor.getLogicValueByName("memory");
				Decoder dec = (Decoder) processor.getLogicValueByName("decoder");

				mem.acceptMemory();
				Transformator transe = new Transformator(new Micro2Data("Autor", new Date(), "Ein Programm", mem.getProgram(), dec.getMicrocode()));

				try {
					Document xmlDoc = transe.getProgramData();

					OutputFormat format = new OutputFormat(xmlDoc);
					format.setLineWidth(65);
					format.setIndenting(true);
					format.setIndent(2);
					FileOutputStream out = new FileOutputStream(lastSavedProgramFile);
					XMLSerializer serializer = new XMLSerializer(out, format);
					serializer.serialize(xmlDoc);
					out.close();
				} catch (Exception ex) {
					logger.error("Fehler beim Schreiben der XML-Datei: ",ex);
				}
			} else {
				miSaveProgramAs.fire();
			}
		});
		miOpenProgram.setOnAction(e->{
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(System.getProperty("user.home")));
			fc.getExtensionFilters().add(new ExtensionFilter("Programfiles", Arrays.asList(new String[]{"*.xml","*.prog"})));
			File openFile = fc.showOpenDialog(null);
			if(openFile != null & openFile.isFile()){
				try {
					Memory mem = (Memory) processor.getLogicValueByName("memory");
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document doc = builder.parse(openFile);
					Transformator transe = new Transformator(doc);
					mem.setProgram(transe.getData().getProgram());
					mem.acceptProgram();
				} catch (ParserConfigurationException e1) {
					e1.printStackTrace();
				} catch (SAXException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		miSaveMicrocodeAs.setOnAction( e -> {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(System.getProperty("user.home")));
			fc.getExtensionFilters().add(new ExtensionFilter("Microcodefiles", Arrays.asList(new String[]{"*.xml","*.microcode"})));
			File saveFile = fc.showSaveDialog(null);
			if(!saveFile.exists())
				try {
					saveFile.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			if(saveFile != null & saveFile.isFile()){
				Memory mem = (Memory) processor.getLogicValueByName("memory");
				Decoder dec = (Decoder) processor.getLogicValueByName("decoder");

				mem.acceptMemory();
				Transformator transe = new Transformator(new Micro2Data("Autor", new Date(), "Ein Programm", mem.getProgram(), dec.getMicrocode()));

				try {
					Document xmlDoc = transe.getMicrocodeData();

					OutputFormat format = new OutputFormat(xmlDoc);
					format.setLineWidth(65);
					format.setIndenting(true);
					format.setIndent(2);
					FileOutputStream out = new FileOutputStream(saveFile);
					XMLSerializer serializer = new XMLSerializer(out, format);
					serializer.serialize(xmlDoc);
					out.close();
					lastSavedMicrocodeFile = saveFile;
				} catch (Exception ex) {
					logger.error("Fehler beim Schreiben der XML-Datei: ",ex);
				}
			}
		});
		miSaveMicrocode.setOnAction( e -> {
			if(lastSavedMicrocodeFile != null) {
				Memory mem = (Memory) processor.getLogicValueByName("memory");
				Decoder dec = (Decoder) processor.getLogicValueByName("decoder");

				mem.acceptMemory();
				Transformator transe = new Transformator(new Micro2Data("Autor", new Date(), "Ein Programm", mem.getProgram(), dec.getMicrocode()));

				try {
					Document xmlDoc = transe.getMicrocodeData();

					OutputFormat format = new OutputFormat(xmlDoc);
					format.setLineWidth(65);
					format.setIndenting(true);
					format.setIndent(2);
					FileOutputStream out = new FileOutputStream(lastSavedMicrocodeFile);
					XMLSerializer serializer = new XMLSerializer(out, format);
					serializer.serialize(xmlDoc);
					out.close();
				} catch (Exception ex) {
					logger.error("Fehler beim Schreiben der XML-Datei: ",ex);
				}
			} else {
				miSaveMicrocodeAs.fire();
			}
		});
		miOpenMicrocode.setOnAction( e -> {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(System.getProperty("user.home")));
			fc.getExtensionFilters().add(new ExtensionFilter("Microcodefiles", Arrays.asList(new String[]{"*.xml","*.microcode"})));
			File openFile = fc.showOpenDialog(null);
			if(openFile != null & openFile.isFile()){
				try {
					Decoder decoder = (Decoder) processor.getLogicValueByName("decoder");
					Memory memory = (Memory) processor.getLogicValueByName("memory");

					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = factory.newDocumentBuilder();
					Document doc = builder.parse(openFile);
					memory.acceptMemory();
					Program program = memory.getProgram();
					Microcode microcode = decoder.getMicrocode();
					Transformator transe = new Transformator(new Micro2Data("",new Date(), "",program,microcode));
					transe.setMicrocodeData(doc);
					decoder.setMicrocode(transe.getData().getMicrocode());

				} catch (ParserConfigurationException e1) {
					e1.printStackTrace();
				} catch (SAXException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		miSaveAll.setOnAction( e -> {
			miSaveProgram.fire();
			miSaveMicrocode.fire();
		});
		miEditProgramFlow.setOnAction(e->{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("InstructionCycle.fxml"));
			Pane root = null;
			try {
				root = (Pane) loader.load();
			} catch (Exception e1) {
				logger.error("Fehler beim Laden des Programablaufs",e1);
			}
			InstructionCycleController controller = loader.getController();
			if(programFlowStage == null || !programFlowStage.isShowing()){
				controller.load(processor.getInstructionCycle());
				programFlowStage = new Stage();
				Scene scene = new Scene(root);
				programFlowStage.setScene(scene);
				programFlowStage.setTitle("Programmablauf");
				programFlowStage.getIcons().add(new Image(ThemeUtils.getResourcePart("symbol")));
				programFlowStage.show();
			}else
				programFlowStage.show();
		});
		miEditProgramm.setOnAction(e->{
			if(programStage == null || !programStage.isShowing()){
				programStage = new fmProgram(processor);
				programStage.setTitle("Programm bearbeiten");
				programStage.getIcons().add(new Image(ThemeUtils.getResourcePart("symbol")));
				programStage.show();
			}else
				programStage.show();
		});
		miEditMicrocode.setOnAction(e->{
			if(microcodeStage == null || !microcodeStage.isShowing()){
				microcodeStage = new fmMicrocode(processor);
				microcodeStage.setTitle("Microcode bearbeiten");
				microcodeStage.getIcons().add(new Image(ThemeUtils.getResourcePart("symbol")));
				microcodeStage.show();
			}else
				microcodeStage.show();
		});

		miPrintPrintProgram.setOnAction(e -> {});
		miPrintPrintSnapshot.setOnAction(e -> {});
		miPrintSaveSnapshotAsPicture.setOnAction(e -> {
			FileChooser fc = new FileChooser();
			fc.setInitialDirectory(new File(System.getProperty("user.home")));
			fc.getExtensionFilters().add(new ExtensionFilter("PNG", Arrays.asList(new String[]{"*.png" })));
			File saveFile = fc.showSaveDialog(null);
			if(saveFile != null && !saveFile.exists())
				try {
					saveFile.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			if(saveFile != null & saveFile.isFile()){
				try {
					currentSnapshot = mainPane.snapshot(null, null);

					BufferedImage image = SwingFXUtils.fromFXImage(currentSnapshot, null);
					FileOutputStream out = new FileOutputStream(saveFile);
					ImageIO.write(image, "png", out);
					out.close();
				} catch (IOException e1) {
					logger.error("Fehler beim Schreiben des Screenshots: ", e);
				}
			}


		});
		miAbout.setOnAction(e -> {
			if(aboutStage == null || !aboutStage.isShowing()){
				aboutStage = new fmAbout();
				aboutStage.setTitle("\u00DCber Micro2");
				aboutStage.show();
			} else
				aboutStage.show();
		});
		miDocumentation.setOnAction(e-> {
			try {
				Desktop.getDesktop().browse(new URI("https://wiki.zoe2.de/index.php/Micro"));
			} catch (Exception e1) {
				logger.error("Fehler beim Öffnen der Webseite");
				Alert al = new Alert(AlertType.ERROR);
				al.setHeaderText("Error");
				al.setContentText("Fehler beim Öffnen der Webseite: " + e1);
				al.show();
			}
		});
	}
	/**
	 * close all open stages and stop the running Server
	 * This method is executed if the application is closing
	 * */
	public void closeOtherStages(){
		if(programFlowStage != null)
			programFlowStage.close();
		if(programStage != null)
			programStage.close();
		if(loadFromServerStage != null)
			loadFromServerStage.close();
		if(microcodeStage != null)
			microcodeStage.close();
	}

}