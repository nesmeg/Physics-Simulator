package simulator.launcher;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.control.StateComparator;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.EpsilonEqualStatesBuilder;
import simulator.factories.Factory;
import simulator.factories.MassEqualStateBuilder;
import simulator.factories.MassLosingBodyBuilder;
import simulator.factories.MovingTowardsFixedPointBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoForceBuilder;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.ColorTheme;
import simulator.view.MainWindow;

public class Main {

	// default values for some parameters
	//
	private final static Double _dtimeDefaultValue = 2500.0;
	private final static String _forceLawsDefaultValue = "nlug";
	private final static String _stateComparatorDefaultValue = "epseq";
	private final static Integer _stepsDefaultValue = 150;
	private final static String _modeDefaultValue = "batch";

	// some attributes to stores values corresponding to command-line parameters
	//
	private static Double _dtime = null;
	private static String _inFile = null;
	private static JSONObject _forceLawsInfo = null;
	private static JSONObject _stateComparatorInfo = null;

	private static String _outFile = null;
	private static String _expFile = null;
	private static Integer _steps = null;
	private static String _mode = null;

	// factories
	private static Factory<Body> _bodyFactory;
	private static Factory<ForceLaws> _forceLawsFactory;
	private static Factory<StateComparator> _stateComparatorFactory;

	private static void init() {

		// initialize the bodies factory
		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new BasicBodyBuilder());
		bodyBuilders.add(new MassLosingBodyBuilder());
		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);

		// initialize the force laws factory
		ArrayList<Builder<ForceLaws>> forceLawsBuilders = new ArrayList<>();
		forceLawsBuilders.add(new NewtonUniversalGravitationBuilder());
		forceLawsBuilders.add(new MovingTowardsFixedPointBuilder());
		forceLawsBuilders.add(new NoForceBuilder());
		_forceLawsFactory = new BuilderBasedFactory<ForceLaws>(forceLawsBuilders);

		// initialize the state comparator
		ArrayList<Builder<StateComparator>> stateComparatorBuilders = new ArrayList<>();
		stateComparatorBuilders.add(new EpsilonEqualStatesBuilder());
		stateComparatorBuilders.add(new MassEqualStateBuilder());
		_stateComparatorFactory = new BuilderBasedFactory<StateComparator>(stateComparatorBuilders);
	}

	private static void parseArgs(String[] args) {

		Options cmdLineOptions = buildOptions(); // define the valid command line options

		CommandLineParser parser = new DefaultParser(); // parse the command line as provided in args

		try {
			CommandLine line = parser.parse(cmdLineOptions, args);

			parseHelpOption(line, cmdLineOptions);
			// add support of -o, -eo, and -s (define corresponding parse methods)
			parseOutFile(line); // added
			parseExpectedOutput(line); // added
			parseSteps(line); // added
			parseDeltaTimeOption(line);
			parseForceLawsOption(line);
			parseStateComparatorOption(line);
			parseModeOption(line); // added
			parseInFileOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// output (-o)
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg()
				.desc("Output file, where output is written. Default value: the standard output.").build());

		// expected output (-eo)
		cmdLineOptions.addOption(Option.builder("eo").longOpt("expected-output").hasArg()
				.desc("The expected output file. If not provided no comparison is applied").build());

		// steps (-s)
		cmdLineOptions.addOption(Option.builder("s").longOpt("ste").hasArg().desc(
				"An integer representing the number of simulation steps. Default value: " + _stepsDefaultValue + ".")
				.build());

		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc(
				"Execution Mode. Possible values: 'batch' (Batch mode), 'gui' (Graphical User Interface mode). Default value: 'batch'.")
				.build());

		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
				.build());

		// force laws
		cmdLineOptions.addOption(Option.builder("fl").longOpt("force-laws").hasArg()
				.desc("Force laws to be used in the simulator. Possible values: "
						+ factoryPossibleValues(_forceLawsFactory) + ". Default value: '" + _forceLawsDefaultValue
						+ "'.")
				.build());

		// gravity laws
		cmdLineOptions.addOption(Option.builder("cmp").longOpt("comparator").hasArg()
				.desc("State comparator to be used when comparing states. Possible values: "
						+ factoryPossibleValues(_stateComparatorFactory) + ". Default value: '"
						+ _stateComparatorDefaultValue + "'.")
				.build());

		return cmdLineOptions;
	}

	public static String factoryPossibleValues(Factory<?> factory) {
		if (factory == null)
			return "No values found (the factory is null)";

		String s = "";

		for (JSONObject fe : factory.getInfo()) {
			if (s.length() > 0) {
				s = s + ", ";
			}
			s = s + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
		}

		s = s + ". You can provide the 'data' json attaching :{...} to the tag, but without spaces.";
		return s;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_inFile == null && _mode == "batch") {
			throw new ParseException("In batch mode an input file of bodies is required");
		}
	}

	private static void parseOutFile(CommandLine line) {
		_outFile = line.getOptionValue("o");
	}

	private static void parseExpectedOutput(CommandLine line) {
		_expFile = line.getOptionValue("eo");
	}

	private static void parseSteps(CommandLine line) throws ParseException {
		String s = line.getOptionValue("s", _stepsDefaultValue.toString());
		try {
			_steps = Integer.parseInt(s);
			assert (_steps > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid steps value: " + s);
		}
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	private static JSONObject parseWRTFactory(String v, Factory<?> factory) {

		// the value of v is either a tag for the type, or a tag:data where data is a
		// JSON structure corresponding to the data of that type. We split this
		// information
		// into variables 'type' and 'data'
		//
		int i = v.indexOf(":");
		String type = null;
		String data = null;
		if (i != -1) {
			type = v.substring(0, i);
			data = v.substring(i + 1);
		} else {
			type = v;
			data = "{}";
		}

		// look if the type is supported by the factory
		boolean found = false;
		for (JSONObject fe : factory.getInfo()) {
			if (type.equals(fe.getString("type"))) {
				found = true;
				break;
			}
		}

		// build a corresponding JSON for that data, if found
		JSONObject jo = null;
		if (found) {
			jo = new JSONObject();
			jo.put("type", type);
			jo.put("data", new JSONObject(data));
		}
		return jo;

	}

	private static void parseForceLawsOption(CommandLine line) throws ParseException {
		String fl = line.getOptionValue("fl", _forceLawsDefaultValue);
		_forceLawsInfo = parseWRTFactory(fl, _forceLawsFactory);

		if (_forceLawsInfo == null)
			throw new ParseException("Invalid force laws: " + fl);
	}

	private static void parseStateComparatorOption(CommandLine line) throws ParseException {
		String scmp = line.getOptionValue("cmp", _stateComparatorDefaultValue);
		_stateComparatorInfo = parseWRTFactory(scmp, _stateComparatorFactory);

		if (_stateComparatorInfo == null)
			throw new ParseException("Invalid state comparator: " + scmp);
	}

	private static void parseModeOption(CommandLine line) throws ParseException {
		String mode = line.getOptionValue("m", _modeDefaultValue);

		if (mode.equals("batch") || mode.equals("gui"))
			_mode = mode;
		else
			throw new ParseException("Invalid mode: " + mode);
	}

	private static void startBatchMode() throws Exception {
		// Create a simulator
		ForceLaws forceLaws = _forceLawsFactory.createInstance(_forceLawsInfo);
		PhysicsSimulator simulator = new PhysicsSimulator(_dtime, forceLaws);

		// Create corresponding input/outout
		FileInputStream input = new FileInputStream(_inFile); // an input file is always going to be required so we dont
																// need a condition

		OutputStream output = null;
		if (_outFile != null) {
			output = new FileOutputStream(_outFile);
		}
		else {
			output = System.out;
		}

		FileInputStream expectedOutput = null;
		StateComparator stateComparator = null;
		if (_expFile != null) {
			expectedOutput = new FileInputStream(_expFile);

			// Creates state comparator (only needed if we have a not null _expFile)
			stateComparator = _stateComparatorFactory.createInstance(_stateComparatorInfo);
		}

		// Create a Controller
		Controller controller = new Controller(simulator, _forceLawsFactory, _bodyFactory);

		// Add the bodies to the simulator
		controller.loadBodies(input);

		// Start the simulator
		controller.run(_steps, output, expectedOutput, stateComparator);
	}

	private static void startGUIMode() throws FileNotFoundException, InvocationTargetException, InterruptedException {
		ColorTheme colorTheme = new ColorTheme();
		if (colorTheme.getTheme() == 1) { // dark theme
			UIManager.put( "control", new Color( 100, 100, 100) );
			UIManager.put( "info", new Color(128, 128, 128) );
			UIManager.put( "nimbusBase", new Color( 18, 30, 49) );
			UIManager.put( "nimbusAlertYellow", new Color( 248, 187, 0) );
			UIManager.put( "nimbusDisabledText", new Color( 128, 128, 128) );
			UIManager.put( "nimbusFocus", new Color(115,164,209) );
			UIManager.put( "nimbusGreen", new Color(176,179,50) );
			UIManager.put( "nimbusInfoBlue", new Color( 66, 139, 221) );
			UIManager.put( "nimbusLightBackground", new Color( 18, 30, 49) );
			UIManager.put( "nimbusOrange", new Color(191,98,4) );
			UIManager.put( "nimbusRed", new Color(169,46,34) );
			UIManager.put( "nimbusSelectedText", new Color( 255, 255, 255) );
			UIManager.put( "nimbusSelectionBackground", new Color( 104, 93, 156) );
			UIManager.put( "text", new Color( 230, 230, 230) );
			try {
				for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						javax.swing.UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (javax.swing.UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// else, light theme by default


		// Create a simulator
		ForceLaws forceLaws = _forceLawsFactory.createInstance(_forceLawsInfo);
		PhysicsSimulator simulator = new PhysicsSimulator(_dtime, forceLaws);

		// Create a Controller
		Controller controller = new Controller(simulator, _forceLawsFactory, _bodyFactory);

		// In GUI mode, input file is optional, so:
		if (_inFile != null) {
			FileInputStream input = new FileInputStream(_inFile);

			// Add the bodies to the simulator
			controller.loadBodies(input);
		}

		// Start the simulator
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				new MainWindow(controller);
			}
		});
	}

	private static void start(String[] args) throws Exception {
		parseArgs(args);

		if (_mode.equals("batch"))
			startBatchMode();
		else // we don't need to check _mode.equals("gui") because if it is not "batch" or
				// "gui", we don't arrive here
			startGUIMode();
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
