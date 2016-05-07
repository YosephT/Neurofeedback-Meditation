import javafx.scene.media.AudioClip;
import org.LiveGraph.LiveGraph;
import org.LiveGraph.settings.DataFileSettings;

import java.io.*;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
authors: Oscar Courchaine, Yoseph Tesfagaber
 */
public class Main {

    // Controls for non-programmers

    private static int minutesOfPhases = 8;

    private static double ignoreNumber = -.77777772;
    // Use these to overwrite all individual electrode frequency weights, set them to the ignore number to ignore them

    private static double _4HZ_WEIGHT = 0;
    private static double _6HZ_WEIGHT = 0;
    private static double _8HZ_WEIGHT = ignoreNumber;
    private static double _10HZ_WEIGHT = ignoreNumber;
    private static double _12HZ_WEIGHT = ignoreNumber;

    private static double[] hertzWeights = new double[]{_4HZ_WEIGHT, _6HZ_WEIGHT, _8HZ_WEIGHT, _10HZ_WEIGHT, _12HZ_WEIGHT};

    private static double F3_WEIGHT = ignoreNumber;
    private static double F4_WEIGHT = ignoreNumber;
    private static double P7_WEIGHT = ignoreNumber;
    private static double FC6_WEIGHT = ignoreNumber;
    private static double F7_WEIGHT = ignoreNumber;
    private static double F8_WEIGHT = ignoreNumber;
    private static double T7_WEIGHT = ignoreNumber;
    private static double P8_WEIGHT = ignoreNumber;
    private static double FC5_WEIGHT = ignoreNumber;
    private static double AF4_WEIGHT = ignoreNumber;
    private static double T8_WEIGHT = ignoreNumber;
    private static double O2_WEIGHT = ignoreNumber;
    private static double O1_WEIGHT = ignoreNumber;
    private static double AF3_WEIGHT = ignoreNumber;

    private static double[] electrodeWeights = new double[]{F3_WEIGHT, F4_WEIGHT, P7_WEIGHT, FC6_WEIGHT, F7_WEIGHT, F8_WEIGHT, T7_WEIGHT,
            P8_WEIGHT, FC5_WEIGHT, AF4_WEIGHT, T8_WEIGHT, O2_WEIGHT, O1_WEIGHT, AF3_WEIGHT};

    // Specify electrode-frequency relationship

    private static double trainingRangeLength = .6;
    private static double trainingThreshold = 1.3;
    private static int numberOfElectrodes = 14;
    private static int totalFrameSize = 64;
    private static int frameShiftSize = 8;
    private static int phaseLength = totalFrameSize * 2 * 60 * minutesOfPhases;


    // sample file format has each electrode one each line, with the bottom two
    // lines representing the reference nodes
    private static String inputFilePath = "data/sample_data.txt";
    private static String outputFilePath = "output/";
    private static String outputFileName = "sample_output[num]";
    private static String meanOutputFileName = "mean_output";

    // These are the names of each electrode, in the order they appear in the file
    private static String F3 = "F3";
    private static String F4 = "F4";
    private static String P7 = "P7";
    private static String FC6 = "FC6";
    private static String F7 = "F7";
    private static String F8 = "F8";
    private static String T7 = "T7";
    private static String P8 = "P8";
    private static String FC5 = "FC5";
    private static String AF4 = "AF4";
    private static String T8 = "T8";
    private static String O2 = "O2";
    private static String O1 = "O1";
    private static String AF3 = "AF3";

    private static String GYROX = "GYROX";
    private static String GYROY = "GYROY";

    private static String[] electrodeList =
            new String[]{AF3, F7, F3, FC5, T7, P7, O1, O2, P8, T8, FC6, F4, F8, AF4};

    private static String trainingSessionName = "#1";
    private static String testSessionName = "#2";
    private static String resultSessionName = "#3";

    // *********************************** Matt look here ********************************

    // Name of person on whom experiment is being conducted, the session name and the resultant training data file name
    private static String name = "MattGroth";
    private static String session = testSessionName;
    private static String trainingDataFileName = "70.csv"; // change this to the result of the first session's run

    public static void main(String[] args) throws IOException {
        double[] allWeights = setUpWeights();
        System.out.println("[LOG] Starting Program");
        Process p = startConnection();
        System.out.println("[LOG] Python Intake Started");
        outputResults(p, allWeights);
    }

    private static double[] setUpWeights() {
        double F3_4HZ_WEIGHT = 1.0;
        double F3_6HZ_WEIGHT = 1.0;
        double F3_8HZ_WEIGHT = 1.0;
        double F3_10HZ_WEIGHT = 1.0;
        double F3_12HZ_WEIGHT = 1.0;

        double F4_4HZ_WEIGHT = 1.0;
        double F4_6HZ_WEIGHT = 1.0;
        double F4_8HZ_WEIGHT = 1.0;
        double F4_10HZ_WEIGHT = 1.0;
        double F4_12HZ_WEIGHT = 1.0;

        double P7_4HZ_WEIGHT = 1.0;
        double P7_6HZ_WEIGHT = 1.0;
        double P7_8HZ_WEIGHT = 1.0;
        double P7_10HZ_WEIGHT = 1.0;
        double P7_12HZ_WEIGHT = 1.0;

        double FC6_4HZ_WEIGHT = 1.0;
        double FC6_6HZ_WEIGHT = 1.0;
        double FC6_8HZ_WEIGHT = 1.0;
        double FC6_10HZ_WEIGHT = 1.0;
        double FC6_12HZ_WEIGHT = 1.0;

        double F7_4HZ_WEIGHT = 1.0;
        double F7_6HZ_WEIGHT = 1.0;
        double F7_8HZ_WEIGHT = 1.0;
        double F7_10HZ_WEIGHT = 1.0;
        double F7_12HZ_WEIGHT = 1.0;

        double F8_4HZ_WEIGHT = 1.0;
        double F8_6HZ_WEIGHT = 1.0;
        double F8_8HZ_WEIGHT = 1.0;
        double F8_10HZ_WEIGHT = 1.0;
        double F8_12HZ_WEIGHT = 1.0;

        double T7_4HZ_WEIGHT = 1.0;
        double T7_6HZ_WEIGHT = 1.0;
        double T7_8HZ_WEIGHT = 1.0;
        double T7_10HZ_WEIGHT = 1.0;
        double T7_12HZ_WEIGHT = 1.0;

        double P8_4HZ_WEIGHT = 1.0;
        double P8_6HZ_WEIGHT = 1.0;
        double P8_8HZ_WEIGHT = 1.0;
        double P8_10HZ_WEIGHT = 1.0;
        double P8_12HZ_WEIGHT = 1.0;

        double FC5_4HZ_WEIGHT = 1.0;
        double FC5_6HZ_WEIGHT = 1.0;
        double FC5_8HZ_WEIGHT = 1.0;
        double FC5_10HZ_WEIGHT = 1.0;
        double FC5_12HZ_WEIGHT = 1.0;

        double AF4_4HZ_WEIGHT = 1.0;
        double AF4_6HZ_WEIGHT = 1.0;
        double AF4_8HZ_WEIGHT = 1.0;
        double AF4_10HZ_WEIGHT = 1.0;
        double AF4_12HZ_WEIGHT = 1.0;

        double T8_4HZ_WEIGHT = 1.0;
        double T8_6HZ_WEIGHT = 1.0;
        double T8_8HZ_WEIGHT = 1.0;
        double T8_10HZ_WEIGHT = 1.0;
        double T8_12HZ_WEIGHT = 1.0;

        double O2_4HZ_WEIGHT = 1.0;
        double O2_6HZ_WEIGHT = 1.0;
        double O2_8HZ_WEIGHT = 1.0;
        double O2_10HZ_WEIGHT = 1.0;
        double O2_12HZ_WEIGHT = 1.0;

        double O1_4HZ_WEIGHT = 1.0;
        double O1_6HZ_WEIGHT = 1.0;
        double O1_8HZ_WEIGHT = 1.0;
        double O1_10HZ_WEIGHT = 1.0;
        double O1_12HZ_WEIGHT = 1.0;

        double AF3_4HZ_WEIGHT = 1.0;
        double AF3_6HZ_WEIGHT = 1.0;
        double AF3_8HZ_WEIGHT = 1.0;
        double AF3_10HZ_WEIGHT = 1.0;
        double AF3_12HZ_WEIGHT = 1.0;

        double[] allWeights = new double[]{
                F3_4HZ_WEIGHT, F3_6HZ_WEIGHT, F3_8HZ_WEIGHT, F3_10HZ_WEIGHT, F3_12HZ_WEIGHT,
                F4_4HZ_WEIGHT, F4_6HZ_WEIGHT, F4_8HZ_WEIGHT, F4_10HZ_WEIGHT, F4_12HZ_WEIGHT,
                P7_4HZ_WEIGHT, P7_6HZ_WEIGHT, P7_8HZ_WEIGHT, P7_10HZ_WEIGHT, P7_12HZ_WEIGHT,
                FC6_4HZ_WEIGHT, FC6_6HZ_WEIGHT, FC6_8HZ_WEIGHT, FC6_10HZ_WEIGHT, FC6_12HZ_WEIGHT,
                F7_4HZ_WEIGHT, F7_6HZ_WEIGHT, F7_8HZ_WEIGHT, F7_10HZ_WEIGHT, F7_12HZ_WEIGHT,
                F8_4HZ_WEIGHT, F8_6HZ_WEIGHT, F8_8HZ_WEIGHT, F8_10HZ_WEIGHT, F8_12HZ_WEIGHT,
                T7_4HZ_WEIGHT, T7_6HZ_WEIGHT, T7_8HZ_WEIGHT, T7_10HZ_WEIGHT, T7_12HZ_WEIGHT,
                P8_4HZ_WEIGHT, P8_6HZ_WEIGHT, P8_8HZ_WEIGHT, P8_10HZ_WEIGHT, P8_12HZ_WEIGHT,
                FC5_4HZ_WEIGHT, FC5_6HZ_WEIGHT, FC5_8HZ_WEIGHT, FC5_10HZ_WEIGHT, FC5_12HZ_WEIGHT,
                AF4_4HZ_WEIGHT, AF4_6HZ_WEIGHT, AF4_8HZ_WEIGHT, AF4_10HZ_WEIGHT, AF4_12HZ_WEIGHT,
                T8_4HZ_WEIGHT, T8_6HZ_WEIGHT, T8_8HZ_WEIGHT, T8_10HZ_WEIGHT, T8_12HZ_WEIGHT,
                O2_4HZ_WEIGHT, O2_6HZ_WEIGHT, O2_8HZ_WEIGHT, O2_10HZ_WEIGHT, O2_12HZ_WEIGHT,
                O1_4HZ_WEIGHT, O1_6HZ_WEIGHT, O1_8HZ_WEIGHT, O1_10HZ_WEIGHT, O1_12HZ_WEIGHT,
                AF3_4HZ_WEIGHT, AF3_6HZ_WEIGHT, AF3_8HZ_WEIGHT, AF3_10HZ_WEIGHT, AF3_12HZ_WEIGHT};

        for (int j = 0; j < electrodeWeights.length; j++) {
            double ov = electrodeWeights[j];
            if (ov != ignoreNumber) {
                for (int i = (j*5); i < ((j*5)+5); i++) {
                    allWeights[i] = ov;
                }
            }
        }

        for (int j = 0; j < hertzWeights.length; j++) {
            double ov = hertzWeights[j];
            if (ov != ignoreNumber) {
                for (int i = j; i < allWeights.length; i+=5) {
                    allWeights[i] = ov;
                }
            }
        }
        return allWeights;
    }

    /**
     * Our 'main' method that needs a lot of refactoring for clarity
     * @param p Python process that grabs data from the EPOC
     * @param allWeights is the set of all the weights for the electrode/frequency pairs
     * @throws IOException because Java is the Nanny State of programming languages
     */
    private static void outputResults(Process p, double[] allWeights) throws IOException {
        BufferedReader dataInput = new BufferedReader(new InputStreamReader((p.getInputStream())));
        BufferedReader errorCatcher = new BufferedReader(new InputStreamReader((p.getErrorStream())));

        FFT myFFT = null;

        // 8 (64 to start) packets of 14 data points each: the size of a frame shift
        int[][] packets = new int[totalFrameSize][numberOfElectrodes];
        int[] packet = new int[numberOfElectrodes];
        String s = null;
        String e = null;
        int lineCounter = 0; // counts every line of input
        int tempPacketCounter = 0; // counts every full set of packets
        int totalPacketCounter = 0; // counts all packets
        boolean started = false;
        boolean initializing = true;
        TrainingData td = null;
        String soundFilePath = "white_noise.wav";
        AudioClip noise = new AudioClip(Paths.get(soundFilePath).toUri().toString());
        int changeVolumeLevelTime = 0;
        double newVol = 0.0;
        while ((s = dataInput.readLine()) != null) {
            if ((e = errorCatcher.readLine()) != null) { }
            lineCounter ++;

            // initialize session settings
            if (lineCounter == 1) {
                if (session.equals(trainingSessionName)) {
                    td = new TrainingData();
                }
                else if (session.equals(testSessionName)) {
                    td = new TrainingData(trainingDataFileName);
                    td.setSummationStandardDeviationAndMu(allWeights);
                }
            }

            // terminate with session settings
            if (totalPacketCounter == phaseLength) {
                if (session.equals(trainingSessionName)) {
                    System.out.println(td.writeToTempFile());
                }
                else if (session.equals(testSessionName)) {
                    noise.stop();
                }
                else {

                }
                break;
            }
            if (session.equals(testSessionName)) {
                noise.play();
            }

            // if stuff we can't filter out is still printing
            if (!started) {
                if (s.equals("Start")) {
                    System.out.println("[LOG] Data Intake Started");
                    started = true;
                    continue;
                }
            }

            // if only EEG data is expected from here on
            if (started) {
                // take in packet
                String[] readings = s.split(" ");
                for (int i = 0; i < numberOfElectrodes; i++) {
                    packet[i] = Integer.parseInt(readings[i]);
                }
                packets[tempPacketCounter] = packet;
                tempPacketCounter ++;
                totalPacketCounter ++;

                //initialization
                if (tempPacketCounter == 64 && initializing) {
                    myFFT = new FFT(packets);
                    Complex[][] powerSpectra = myFFT.runFFT();
                    initializeFileCsv(powerSpectra);
                    analyzeData(powerSpectra, totalPacketCounter, td, allWeights, noise, changeVolumeLevelTime);
                    // reset packets object to fit the window shift
                    packets = new int[frameShiftSize][numberOfElectrodes];
                    initializing = false;
                    tempPacketCounter = 0;
                    System.out.println("[LOG] FFT Initialized");
                }

                // window shift
                else if (tempPacketCounter == 8 && !initializing) {
                    myFFT.addData(packets);
                    Complex[][] powerSpectra = myFFT.runFFT();
                    addToCsv(powerSpectra);
                    double tempVol = analyzeData(powerSpectra, totalPacketCounter, td, allWeights, noise, changeVolumeLevelTime);
                    if (newVol == 0.0) {
                        newVol = tempVol;
                    }
                    else {
                        newVol = (newVol + tempVol) / 2;
                    }

                    if (changeVolumeLevelTime == 16 && session.equals(testSessionName)) { // ****** MATT you can increase the number 16 to slow the rate of volume change *****
                        noise.setVolume(newVol);
                        System.out.println(newVol);
                        newVol = 0.0;
                        changeVolumeLevelTime = 0;
                    }
                    else {
                        changeVolumeLevelTime ++;
                    }
                    tempPacketCounter = 0;
                }
            }
        }
    }

    /**
     * Adding our FFT results to the CSV
     * @param powerSpectra is the array of arrays containing the EPOC readings
     */
    private static void addToCsv(Complex[][] powerSpectra) {
        try (FileWriter file = new FileWriter(name + session + getDate() + ".csv", true)) {
            file.append(powerSpectraCSV(powerSpectra));
        } catch (IOException e){
            e.printStackTrace();
        }    }

    /**
     * Creates a file based on the global variables name and session which are set by the person running this experiment
     *
     * @param powerSpectra is the first group of FFT results
     */
    private static void initializeFileCsv(Complex[][] powerSpectra) {
        try (FileWriter file = new FileWriter(name + session + getDate() + ".csv")) {
            file.write(powerSpectraCSV(powerSpectra));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Turns the powerspectrum readings into a string. Each call of this method will convert all 14 nodes and the
     * corresponding 5 hertz readings into a single string
     * @param powerSpectra
     * @return A stringified version of the power spectra in the form "Node #2 | Hertz 3 : 2.12 4.3i | Hertz 5 : 2.1 3.i"
     */
    private static String powerSpectraCSV(Complex[][] powerSpectra) {
        String hertzNumber = "";
        String result = "";
        for (int i = 0; i < 14; i++){
            for (int j = 0; j < 5; j++){
                hertzNumber = hertzNumber + "hertz " + ((j*2) + 4) + ": " + powerSpectra[i][j].toString() + "|";
            }
            result += "Node " + electrodeList[i] + "|" + hertzNumber + "\n";
            hertzNumber = "";
        }
        return result;
    }

    /**
     * Takes care of seperating training data from test data and calling functions to analyze/categorize each
     * @param powerSpectra Current FFT results in need of analysis
     * @param packetNumber how many packets have been processed
     * @param td TrainingData object for storing and using training data
     * @param allWeights is the set of all the weights for the electrode/frequency pairs
     * @param noise is our sound object, we can manipulate it's volume wherever it is present
     * @param changeLevel
     */
    private static double analyzeData(Complex[][] powerSpectra, int packetNumber, TrainingData td, double[] allWeights, AudioClip noise, int changeLevel) {
        if (packetNumber <= phaseLength) {
            if (session.equals(trainingSessionName)) {
                td.addNumbers(powerSpectra);
            }
            else if (session.equals(testSessionName)) {
                double newVolume = runFunction(powerSpectra, td, allWeights);
                return newVolume;
            }
            else if (session.equals(resultSessionName)) {

            }
        }
        return 0.0;
    }

    /**
     * compartmentalizes Matt's Neuro function
     * @param powerSpectra is the latest series of FFT results
     * @param td is the TrainingData object used to run the equation against Training data
     * @param allWeights is the set of all the weights for the electrode/frequency pairs
     * @return our desired volume for neurofeedback white noise
     */
    private static double runFunction(Complex[][] powerSpectra, TrainingData td, double[] allWeights) {
        ArrayList<Double> trainingValues = td.returnDoubles();
        ArrayList<Double> testValues = getDoubles(powerSpectra, td);
        double summation = 0;
        for (int i = 0; i < allWeights.length; i++) {
            summation = allWeights[i] * testValues.get(i);
            //runnable += (allWeights[i] * (testValues.get(i) - (trainingThreshold * trainingValues.get(i))) * 100) / (trainingRangeLength * trainingValues.get(i)); (deprecated)
        }
        double mu = td.getMu();

        double volume = (mu * summation) - (mu * (td.getSummation() + td.getsD()));

        return volume * -.0001; // volume is between 0.0 and 1.0 so make that the case, right now it is negative because the equation return only negative numbers, if that changes, change this
    }

    /**
     * Just converts a series of complex numbers to their respictive powers as doubles
     * @param powerSpectra is the Complex numbers to convert
     * @param td is the TrainingData Object used to run the conversion function
     * @return double ArrayList of the powers of the Complex numbers
     */
    private static ArrayList<Double> getDoubles(Complex[][] powerSpectra, TrainingData td) {
        ArrayList<Double> returnable = new ArrayList<>();
        for (Complex[] spectrum : powerSpectra) {
            for (Complex c : spectrum) {
                double input = td.complexToPower(c);
                returnable.add(input);
            }
        }
        return returnable;
    }

    /**
     This function will establish a connection
     with the EEG via the emokit python command
     */
    private static Process startConnection() throws IOException {
        Process p = Runtime.getRuntime().exec("python emokit-master/python/example.py");
        return p;
    }

    /**
     * Date formatter
     * @return date as string. eg "20161129"
     */
    public static String getDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * clears our output files for new data (graphing)
     */
    private static void clearOutputData() {
        File folder = new File(outputFilePath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }

    /**
     * start the graphing Java apps
     */
    private static void startGraphing() {
        LiveGraph app = LiveGraph.application();
        app.execStandalone();
    }

    /**
     * maps the java graphing app to our desired graph data file(s)
     */
    private static void initializeSettings() {
        DataFileSettings dfs = new DataFileSettings();
        dfs.setDataFile(outputFilePath + meanOutputFileName + ".lgdat");
        dfs.setUpdateFrequency(1000);
        dfs.save("startup.lgdfs");
    }
}