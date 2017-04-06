# Neurofeedback-Meditation

The purpose of this Neuroscience/Computer Science study was to investigate the potential of a consumer electroencephalogram (EEG), the Emotiv EPOC, in neurofeedback (NFB) for meditation. We were seeking to discover whether
subjects getting real time feedback have significantly better results than subjects with sham feedback (a form of placebo) or no feedback.

The study was split into two general components; creation/analysis of the study and development of the software.  

## Software Dev

The project can be categorized into several categories. We've tried to give brief summaries of each below


### Emokit

Emokit was developed as an open source driver created to access the raw data stream from the EPOC. The original project can be located here (https://github.com/openyou/emokit). The related files found on our project are those that we have modified. Substantial work was done to make sure the individual nodes were identified and passed in from the Python to Java as strings.

### Data Analysis

The actual collection and analysis of the data is split ino three distinct categories. The study is meant to be run in three sessions; a training session, a "test" session and a "result" session. These correspond to three different "modes" of running the main method.

The training session creates a Training Data object that is saved externally and consumed by the 2nd session; the test session. During test session and the result session, there will feedback created via a sound file. Due to Github's file limitations, the file has currently been removed. The public link for the white noise audio file is here (https://drive.google.com/file/d/0Bx6fS9IJ2ZxmbkdyUTk2SGU4YTQ/view?usp=sharing) . Download and include this into the home folder of the project (~100mb file). 

Due to the manner of the study, the resulting data from all three sessions is currently exported as a CSV file while the session is being run. The CSV file can be imported into any statistical software (in our case Matlab) for further analysis. Delimiters include "|:" and the format of the data is as follows:

Node AF3|hertz 0: 119.855 + 56.143i|hertz 1: 77.123 + 94.123i|hertz 2: 26.412 + 103.123i|hertz 3: -17.0 + 85.123i|hertz 4: -38.123 + 51.123i|


### Fast-Fourier Transform & Complex Numbers

We utilized and modified the FFT algorithm developed by Princeton University (http://introcs.cs.princeton.edu/java/97data/FFT.java.html). Further explanation on how they work can be found here(http://introcs.cs.princeton.edu/java/97data/).

The complex numbers class was also developed by Princeton University and has not been modified at all. Both the class and explanations on how it functions can be found here (http://introcs.cs.princeton.edu/java/97data/Complex.java.html).

### Graphing

Preliminary graphing functions have been included in the project but have not been fully integrated. Due to sheer quantity of the input stream and raw size of the data; saving the data externally made more sense. Live graphing of the data would require creating a buffered stream of the eeg data directly to the LiveGraph process with two obvious adjustments; buffer/collapse the data signficantly and limit LiveGraph from refreshing no more then every few seconds.