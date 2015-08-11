import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;

public class Neuron {
	public static Double rate = 1d;
	public HashSet<Neuron> inputs = new HashSet<Neuron>();
	public HashSet<Neuron> outputs = new HashSet<Neuron>();
	public HashMap<Neuron, Double> weights = new HashMap<Neuron, Double>();
	public HashMap<Neuron, Double> newWeights = new HashMap<Neuron, Double>();
	public Double bias = 0d;
	public Double output = 0d;
	static Random r = new Random(System.currentTimeMillis());

	/**
	 * add input with default weight of 0.5
	 * 
	 * @param i
	 */
	public void addInput(Neuron i) {
		addInput(i, r.nextDouble() * 0.1d);
	}

	public void addInput(Neuron i, Double weight) {
		inputs.add(i);
		weights.put(i, weight);
		i.outputs.add(this);
	}

	public void updateOutput() {
		Double sum = bias;
		for (Neuron input : inputs) {
			sum += input.output * weights.get(input);
		}
		output = logistic(sum);
	}

	public static Double logistic(Double sum) {
		return 1d / (1d + Math.exp(-sum));
	}

	protected void calcNewW() {

		// wie wirkt sich der gesamtinput der neurone auf den output der neurone aus?
		// => output der neurone * (1-output der neurone)
		Double wert2 = wieWirktSichDerInputAufDenOutputAus();

		// wie wirkt sich der output der neurone auf den total error aus?
		Double wert3 = wieWirktSichDerOutputAufDenTotalErrorAus();

		for (Entry<Neuron, Double> connection : weights.entrySet()) {
			Neuron input = connection.getKey();
			Double weight = connection.getValue();

			// wie wirkt sich das w auf den input der neurone aus?
			// => output der quellneurone
			Double wert1 = input.output;

			Double result = wert1 * wert2 * wert3;
			newWeights.put(input, weight - rate * result);
		}

		bias -= wert3 * rate;

	}

	protected Double wieWirktSichDerOutputAufDenTotalErrorAus() {
		// => ist zunächst die summe für alle zielneuronen => wie wirkt dich der output auf den fehler der zielneurone aus
		Double wert3 = 0d;
		for (Neuron out : outputs) {
			// ==> wie wirkt sich der netzinput der nächsten neurone auf den fehlerwert der nächsten neurone aus
			// 1. wie wirkt sich der input auf den output aus => output der neurone * (1-output der neurone)
			Double wert3_a_a = out.wieWirktSichDerInputAufDenOutputAus();
			// 2. wie wirkt sich der output auf den fehler aus
			Double wert3_a_b = out.wieWirktSichDerOutputAufDenTotalErrorAus();
			// => rekursoin bis zur out neurone, und dort
			// -(EXPECTED-OUTPUT)

			// ==mal

			// ==> wie wirkt sich der output der neurone auf dern input der nächsten neurone aus => das aktuelle w
			Double wert3_b = out.weights.get(this);

			wert3 += wert3_a_a * wert3_a_b * wert3_b;

		}
		return wert3;
	}

	private double wieWirktSichDerInputAufDenOutputAus() {
		return output * (1d - output);
	}

	public void applyNewW() {
		weights = newWeights;

	}

}
