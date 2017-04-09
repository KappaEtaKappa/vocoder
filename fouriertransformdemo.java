import ca.uol.aig.fftpack.RealDoubleFFT;
import java.util.Arrays;

// Documentation for this package can be found in ./jfftpack/javadocs/index.html
// The package is located at ./ca/uol/aig/fftpack

public class fouriertransformdemo{
	public static void main(String[] args){
		// Have to initialize it with the size of the array 
		// (Wayne: I am not sure how Fourier Transform really works, I'm just calling the function)
		RealDoubleFFT fourierTransform = new RealDoubleFFT(7);
		double input[] = {1.0, 2.9, 5.5, 0.52, 55.43, 1.4, 23.6};

		// Input
		System.out.println("Input: ");
		System.out.println(Arrays.toString(input));

		// Transform input
		fourierTransform.ft(input);

		// Output
		System.out.println("Output: ");
		System.out.println(Arrays.toString(input));
	}
}