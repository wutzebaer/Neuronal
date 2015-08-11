import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NeuronNetwork {
	public static void main(String[] args) {
		new NeuronNetwork();
	}

	final Integer layers = 1;
	final Integer hiddenNeuronPerLayer = 1;
	List<InputNeuron> inputNeurons = new ArrayList<InputNeuron>();
	List<OutputNeuron> outputNeurons = new ArrayList<OutputNeuron>();
	List<List<Neuron>> hiddenLayers = new ArrayList<List<Neuron>>();

	Random r = new Random(System.currentTimeMillis());

	public void train(int count) {
		for (int i = 0; i < count; i++) {
			fillTraining();
			updateOutput();
			learn();
		}
	}

	public int test(int count) {
		int correct = 0;
		Double error = 0d;
		for (int i = 0; i < count; i++) {
			fillTraining();
			updateOutput();
			Double calcTotalError = calcTotalError();
			error += Math.abs(calcTotalError);
			if (Math.abs(outputNeurons.get(0).desiredOutput - outputNeurons.get(0).output) < 0.1d) {
				correct++;
			}
		}
		System.out.println((error / (double) count) + " " + correct + " / " + count);
		return correct;
	}

	public NeuronNetwork() {

		System.out.println("staret");
		// add input neurons
		for (int i = 0; i < 1; i++) {
			inputNeurons.add(new InputNeuron(1d));
		}

		// add output neurons
		for (int i = 0; i < 1; i++) {
			outputNeurons.add(new OutputNeuron());
		}

		for (Integer layerIndex = 0; layerIndex < layers; layerIndex++) {
			ArrayList<Neuron> currentHiddenlayer = new ArrayList<Neuron>();
			// add input connections
			for (Integer hiddenNeuronInLayerIndex = 0; hiddenNeuronInLayerIndex < hiddenNeuronPerLayer; hiddenNeuronInLayerIndex++) {
				Neuron hiddenNeuron = new Neuron();
				// add first layer
				if (layerIndex == 0) {
					for (Neuron input : inputNeurons) {
						hiddenNeuron.addInput(input);
					}
				}
				// add inner layers
				else {
					for (Neuron input : hiddenLayers.get(layerIndex - 1)) {
						hiddenNeuron.addInput(input);
					}
				}
				currentHiddenlayer.add(hiddenNeuron);
			}
			hiddenLayers.add(currentHiddenlayer);
		}

		// add all neurons of last hidden layer to each input neuron
		for (Neuron out : outputNeurons) {
			for (Neuron hidden : hiddenLayers.get(hiddenLayers.size() - 1)) {
				out.addInput(hidden);
			}
		}

		for (int i = 0; i < 10000; i++) {
			train(100000);
			if (test(1000) == 1000) {
				test(10000);
				break;
			}
		}

		inputNeurons.get(0).output = 0.0d;

		updateOutput();

		System.out.println("OUTPUT " + outputNeurons.get(0).output);

	}

	public void fillTraining() {
		for (InputNeuron input : inputNeurons) {
			input.output = r.nextDouble();
		}

		if (inputNeurons.get(0).output > 0.7d) {
			outputNeurons.get(0).desiredOutput = 1d;
		} else {
			outputNeurons.get(0).desiredOutput = 0d;
		}

	}

	public Double calcTotalError() {
		Double error = 0d;
		for (OutputNeuron out : outputNeurons) {
			error += Math.pow(out.desiredOutput - out.output, 2);
		}
		error *= 0.5d;
		return error;
	}

	public void updateOutput() {
		for (List<Neuron> layer : hiddenLayers) {
			for (Neuron n : layer) {
				n.updateOutput();
			}
		}
		for (OutputNeuron n : outputNeurons) {
			n.updateOutput();
		}
	}

	public void learn() {
		for (List<Neuron> layer : hiddenLayers) {
			for (Neuron n : layer) {
				n.calcNewW();
			}
		}
		for (OutputNeuron n : outputNeurons) {
			n.calcNewW();
		}
		for (List<Neuron> layer : hiddenLayers) {
			for (Neuron n : layer) {
				n.applyNewW();
			}
		}
		for (OutputNeuron n : outputNeurons) {
			n.applyNewW();
		}
	}

}
