import java.io.*;
import java.util.*;

/**
 * Created by oscar on 4/4/16.
 */
public class TrainingData {

    private static int numberOfHz = 5;
    private int spectrumsReceived;
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

    private static String[] electrodeList =
            new String[]{AF3, F7, F3, FC5, T7, P7, O1, O2, P8, T8, FC6, F4, F8, AF4};
    private HashMap<String, double[]> electrodesAndTrainingValues;

    private double summation;
    private double sD;
    private double mu;

    /**
     * Constructor to initialize a brand new TD object
     */
    public TrainingData() {
        spectrumsReceived = 0;
        electrodesAndTrainingValues = new HashMap<>();
        for (String electrode : electrodeList) {
            electrodesAndTrainingValues.put(electrode, new double[numberOfHz]);
        }
    }

    /**
     * Constructor to make a TD object based off a previous training session
     * @param fileName is the name of the file to read in
     * @throws IOException
     */
    public TrainingData(String fileName) throws IOException {
        spectrumsReceived = 0;
        electrodesAndTrainingValues = new HashMap<>();
        for (String electrode : electrodeList) {
            electrodesAndTrainingValues.put(electrode, new double[numberOfHz]);
        }

        File file = new File(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            double[] weights = new double[5];
            for (int i = 1; i < values.length; i++) {
                weights[i-1] = Double.parseDouble(values[i]);
            }
            electrodesAndTrainingValues.put(values[0], weights);
        }
        //file.delete();
        reader.close();
    }

    /**
     * Averages the new set of complex numbers with the average already on record
     * @param powerSpectra the new set of Complex numbers
     */
    public void addNumbers(Complex[][] powerSpectra) {
        int electrodeCounter = 0;
        int hzCounter = 0;
        // first set of data
        if (spectrumsReceived == 0) {
            for (Complex[] spectrum : powerSpectra) {
                for (Complex c : spectrum) {
                    double input = complexToPower(c); //insert sqrt function or whatever here
                    electrodesAndTrainingValues.get(electrodeList[electrodeCounter])[hzCounter] = input;
                    hzCounter ++;
                }
                electrodeCounter ++;
                hzCounter = 0;
            }
        }
        // any subsequent set of data
        else {
            for (Complex[] spectrum : powerSpectra) {
                for (Complex c : spectrum) {
                    double input = complexToPower(c);
                    double current = electrodesAndTrainingValues.get(electrodeList[electrodeCounter])[hzCounter];
                    electrodesAndTrainingValues.get(electrodeList[electrodeCounter])[hzCounter] = (input + current) / 2;
                    hzCounter ++;
                }
                electrodeCounter ++;
                hzCounter = 0;
            }
        }
    }

    /**
     * Turns a Complex number into a power
     * @param c is the complex number
     * @return the power
     */
    public double complexToPower(Complex c) {
        double power = Math.pow(c.getReal(), 2.0) + Math.pow(c.getImaginary(), 2.0);
        return power;
    }

    /**
     * Makes a sequential list of all training data as doubles
     * @return an ArrayList of all training data in order of electrode, then hertz
     */
    public ArrayList<Double> returnDoubles() {
        ArrayList<Double> returnable = new ArrayList<>();
        for (String e : electrodeList) {
            double[] hertzes = electrodesAndTrainingValues.get(e);
            for (double d : hertzes) {
                returnable.add(d);
            }
        }
        return returnable;
    }

    /**
     * Writes the training data to a file to preserve it in-between training and test sessions
     * @return the name of the temp file
     */
    public String writeToTempFile() {
        Random r = new Random();
        String fileName = r.nextInt(100) + ".csv";
        try (FileWriter file = new FileWriter(fileName)) {
            for (Map.Entry e: electrodesAndTrainingValues.entrySet()) {
                file.write(e.getKey() + ",");
                for (double d : (double[]) e.getValue()) {
                    file.write(String.valueOf(d) + ",");
                }
                file.write("\n");
            }
            file.flush();
            file.close();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            return fileName;
        }
    }

    public void setSummationStandardDeviationAndMu(double[] allWeights) {
        double sum = 0;
        ArrayList<Double> values = returnDoubles();
        double[] weightedValues = new double[allWeights.length];
        double meanOfWeightedValues = 0;
        for (int i = 0; i < allWeights.length; i++) {
            double weightedValue = allWeights[i] * values.get(i);
            sum += weightedValue;
            weightedValues[i] = weightedValue;
            meanOfWeightedValues = (meanOfWeightedValues + weightedValue) / 2;
        }
        this.summation = sum;

        double[] weightedSquaredValues = new double[allWeights.length];
        double meanOfWeightedSquaredValues = 0;
        for (int i = 0; i < allWeights.length; i++) {
            meanOfWeightedSquaredValues = (meanOfWeightedSquaredValues + Math.pow(weightedValues[i] - meanOfWeightedValues, 2)) / 2;
        }
        this.sD = Math.sqrt(meanOfWeightedSquaredValues);

        this.mu = 100 / (2 * this.sD);
    }

    public double getMu() {
        return mu;
    }

    public double getsD() {
        return sD;
    }

    public double getSummation() {
        return summation;
    }
}
