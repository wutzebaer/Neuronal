public class OutputNeuron extends Neuron {
	public Double desiredOutput = 1d;

	protected Double wieWirktSichDerOutputAufDenTotalErrorAus() {
		return -(desiredOutput - output);
	}

}
