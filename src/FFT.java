import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/******************************************************************************
 *  Compilation:  javac FFT.java
 *  Execution:    java FFT N
 *  Dependencies: Complex.java
 *
 *  Compute the FFT and inverse FFT of a length N complex sequence.
 *  Bare bones implementation that runs in O(N log N) time. Our goal
 *  is to optimize the clarity of the code, rather than performance.
 *
 *  Limitations
 *  -----------
 *   -  assumes N is a power of 2
 *
 *   -  not the most memory efficient algorithm (because it uses
 *      an object type for representing complex numbers and because
 *      it re-allocates memory for the subarray, instead of doing
 *      in-place or reusing a single temporary array)
 *  
 ******************************************************************************/

public class FFT {
    LinkedList<Complex> windowF3;
    LinkedList<Complex> windowF4;
    LinkedList<Complex> windowP7;
    LinkedList<Complex> windowFC6;
    LinkedList<Complex> windowF7;
    LinkedList<Complex> windowF8;
    LinkedList<Complex> windowT7;
    LinkedList<Complex> windowP8;
    LinkedList<Complex> windowFC5;
    LinkedList<Complex> windowAF4;
    LinkedList<Complex> windowT8;
    LinkedList<Complex> windowO2;
    LinkedList<Complex> windowO1;
    LinkedList<Complex> windowAF3;

    int windowSize = 64;
    int shiftSize = 8;

    ArrayList<LinkedList<Complex>> windows;

    public FFT(int[][] initialData) {
        windows = new ArrayList<LinkedList<Complex>>();

        windowF3 = new LinkedList<Complex>();
        windowF4 = new LinkedList<Complex>();
        windowP7 = new LinkedList<Complex>();
        windowFC6 = new LinkedList<Complex>();
        windowF7 = new LinkedList<Complex>();
        windowF8 = new LinkedList<Complex>();
        windowT7 = new LinkedList<Complex>();
        windowP8 = new LinkedList<Complex>();
        windowFC5 = new LinkedList<Complex>();
        windowAF4 = new LinkedList<Complex>();
        windowT8 = new LinkedList<Complex>();
        windowO2 = new LinkedList<Complex>();
        windowO1 = new LinkedList<Complex>();
        windowAF3 = new LinkedList<Complex>();

        windows.add(windowF3);
        windows.add(windowF4);
        windows.add(windowP7);
        windows.add(windowFC6);
        windows.add(windowF7);
        windows.add(windowF8);
        windows.add(windowT7);
        windows.add(windowP8);
        windows.add(windowFC5);
        windows.add(windowAF4);
        windows.add(windowT8);
        windows.add(windowO2);
        windows.add(windowO1);
        windows.add(windowAF3);

        for (int[] data : initialData) {
            for (int i = 0; i < data.length; i++) {
                Complex c = new Complex(data[i], 0);
                windows.get(i).add(c);
            }
        }
    }

    // adds the next 8 readings to their respective windows
    public void addData(int[][] newData) {
        for (int[] data : newData) {
            int counter = 0;
            for (int i : data) {
                Complex c = new Complex(i, 0);
                windows.get(counter).add(c);
                counter ++;
            }
        }

        // removes the first 8 readings from their windows
        for (LinkedList<Complex> window : windows) {
            for (int i = 0; i < shiftSize; i++) {
                window.pop(); // we can later return this data to the main program
            }
        }
    }

    public Complex[][] runFFT() {
        Complex[][] returnable = new Complex[14][5];
        int counter = 0;
        for (LinkedList<Complex> window : windows) {
            //show(window.toArray(new Complex[windowSize]), "x");

            // FFT of original data
            Complex[] y = fft(window.toArray(new Complex[windowSize]));

            /*
            For the new call to JSON parser, counter will be the node number (1-14) and for each node, there'll be
            4 complex numbers to add to.
             */
            returnable[counter] = Arrays.copyOfRange(y, 1, 6); // or whatever
            counter ++;
            //show(y, "y = fft(x)");

//            // take inverse FFT
//            Complex[] z = ifft((Complex[]) window.toArray());
//            show(z, "z = ifft(y)");
//
//            // circular convolution of x with itself
//            Complex[] c = cconvolve((Complex[]) window.toArray(), (Complex[]) window.toArray());
//            show(c, "c = cconvolve(x, x)");
//
//            // linear convolution of x with itself
//            Complex[] d = convolve((Complex[]) window.toArray(), (Complex[]) window.toArray());
//            show(d, "d = convolve(x, x)");
        }
        return returnable;
    }

    // compute the FFT of x[], assuming its length is a power of 2
    public Complex[] fft(Complex[] x) {
        int N = x.length;

        // base case
        if (N == 1) return new Complex[] { x[0] };

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0) { throw new RuntimeException("N is not a power of 2"); }

        // fft of even terms
        Complex[] even = new Complex[N/2];
        for (int k = 0; k < N/2; k++) {
            even[k] = x[2*k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd  = even;  // reuse the array
        for (int k = 0; k < N/2; k++) {
            odd[k] = x[2*k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N/2; k++) {
            double kth = -2 * k * Math.PI / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k]       = q[k].plus(wk.times(r[k]));
            y[k + N/2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }


    // compute the inverse FFT of x[], assuming its length is a power of 2
//    public Complex[] ifft(Complex[] x) {
//        int N = x.length;
//        Complex[] y = new Complex[N];
//
//        // take conjugate
//        for (int i = 0; i < N; i++) {
//            y[i] = x[i].conjugate();
//        }
//
//        // compute forward FFT
//        y = fft(y);
//
//        // take conjugate again
//        for (int i = 0; i < N; i++) {
//            y[i] = y[i].conjugate();
//        }
//
//        // divide by N
//        for (int i = 0; i < N; i++) {
//            y[i] = y[i].times(1.0 / N);
//        }
//
//        return y;
//
//    }
//
//    // compute the circular convolution of x and y
//    public Complex[] cconvolve(Complex[] x, Complex[] y) {
//
//        // should probably pad x and y with 0s so that they have same length
//        // and are powers of 2
//        if (x.length != y.length) { throw new RuntimeException("Dimensions don't agree"); }
//
//        int N = x.length;
//
//        // compute FFT of each sequence
//        Complex[] a = fft(x);
//        Complex[] b = fft(y);
//
//        // point-wise multiply
//        Complex[] c = new Complex[N];
//        for (int i = 0; i < N; i++) {
//            c[i] = a[i].times(b[i]);
//        }
//
//        // compute inverse FFT
//        return ifft(c);
//    }
//
//
//    // compute the linear convolution of x and y
//    public Complex[] convolve(Complex[] x, Complex[] y) {
//        Complex ZERO = new Complex(0, 0);
//
//        Complex[] a = new Complex[2*x.length];
//        for (int i = 0;        i <   x.length; i++) a[i] = x[i];
//        for (int i = x.length; i < 2*x.length; i++) a[i] = ZERO;
//
//        Complex[] b = new Complex[2*y.length];
//        for (int i = 0;        i <   y.length; i++) b[i] = y[i];
//        for (int i = y.length; i < 2*y.length; i++) b[i] = ZERO;
//
//        return cconvolve(a, b);
//    }

    // display an array of Complex numbers to standard output
    public void show(Complex[] x, String title) {

        System.out.println(title);
        System.out.println("-------------------");
        for (int i = 0; i < x.length; i++) {
            System.out.println(x[i]);
        }
        System.out.println();
    }


   /***************************************************************************
    *  Test client and sample execution
    *
    *  % java FFT 4
    *  x
    *  -------------------
    *  -0.03480425839330703
    *  0.07910192950176387
    *  0.7233322451735928
    *  0.1659819820667019
    *
    *  y = fft(x)
    *  -------------------
    *  0.9336118983487516
    *  -0.7581365035668999 + 0.08688005256493803i
    *  0.44344407521182005
    *  -0.7581365035668999 - 0.08688005256493803i
    *
    *  z = ifft(y)
    *  -------------------
    *  -0.03480425839330703
    *  0.07910192950176387 + 2.6599344570851287E-18i
    *  0.7233322451735928
    *  0.1659819820667019 - 2.6599344570851287E-18i
    *
    *  c = cconvolve(x, x)
    *  -------------------
    *  0.5506798633981853
    *  0.23461407150576394 - 4.033186818023279E-18i
    *  -0.016542951108772352
    *  0.10288019294318276 + 4.033186818023279E-18i
    *
    *  d = convolve(x, x)
    *  -------------------
    *  0.001211336402308083 - 3.122502256758253E-17i
    *  -0.005506167987577068 - 5.058885073636224E-17i
    *  -0.044092969479563274 + 2.1934338938072244E-18i
    *  0.10288019294318276 - 3.6147323062478115E-17i
    *  0.5494685269958772 + 3.122502256758253E-17i
    *  0.240120239493341 + 4.655566391833896E-17i
    *  0.02755001837079092 - 2.1934338938072244E-18i
    *  4.01805098805014E-17i
    *
    ***************************************************************************/



}
