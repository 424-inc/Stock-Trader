import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

import yahoofinance.Stock;

public class Brain {
	private static MultiLayerNetwork net;
	private static int in_out_size=20;
	private static int hidden_layer_count =5;
	private static int growth_Rate=2;
	public static void startup() {
		File file = new File(Database.basedir+"/AI/Net.txt");
		if(file.exists()) {
			System.out.println("Loading Neural Network...");
			load();
		}else {
			System.out.println("Building Neural Network...");
			build();
		}
	}
	public static void save() {
		try {
			ModelSerializer.writeModel(net, Database.basedir+"/AI/Net.txt", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void load() {
		try {
			net = ModelSerializer.restoreMultiLayerNetwork(Database.basedir+"/AI/Net.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void build(){
		NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder();
		builder.seed(314);
		builder.biasInit(0);
		builder.miniBatch(true);
		builder.updater(new RmsProp(0.001));
		builder.weightInit(WeightInit.XAVIER);
		builder.gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue);
		builder.gradientNormalizationThreshold(0.5);
		
		ListBuilder listBuilder = builder.list();
		
		LSTM.Builder inputLayerBuilder = new LSTM.Builder();
		inputLayerBuilder.nIn(in_out_size);
		inputLayerBuilder.nOut(in_out_size*growth_Rate);
		inputLayerBuilder.activation(Activation.IDENTITY);
		listBuilder.layer(0, inputLayerBuilder.build());
		int previous_layer_out=in_out_size*growth_Rate;
		for(int i=1;i<=hidden_layer_count/2;i++) {
			LSTM.Builder hiddenLayerBuilder = new LSTM.Builder();
			hiddenLayerBuilder.nIn(previous_layer_out);
			hiddenLayerBuilder.nOut(previous_layer_out*growth_Rate);
			hiddenLayerBuilder.activation(Activation.TANH);
			listBuilder.layer(i, hiddenLayerBuilder.build());
			previous_layer_out*=growth_Rate;
		}
		if (!(hidden_layer_count % 2 == 0)) {
			LSTM.Builder hiddenLayerBuilder = new LSTM.Builder();
			hiddenLayerBuilder.nIn(previous_layer_out);
			hiddenLayerBuilder.nOut(previous_layer_out*growth_Rate);
			hiddenLayerBuilder.activation(Activation.IDENTITY);
			listBuilder.layer((int) (1.5+((hidden_layer_count/2))), hiddenLayerBuilder.build());
			previous_layer_out/=growth_Rate;
			for(int i=(int) ((hidden_layer_count/2)+2.5);i<=hidden_layer_count;i++) {
				LSTM.Builder hiddenLayerBuilder2 = new LSTM.Builder();
				hiddenLayerBuilder2.nIn(previous_layer_out);
				hiddenLayerBuilder2.nOut(previous_layer_out*growth_Rate);
				hiddenLayerBuilder2.activation(Activation.IDENTITY);
				listBuilder.layer(i, hiddenLayerBuilder2.build());
				previous_layer_out/=growth_Rate;
			}
		}else {
			for(int i=(int) ((hidden_layer_count/2)+1.5);i<=hidden_layer_count;i++) {
				LSTM.Builder hiddenLayerBuilder = new LSTM.Builder();
				hiddenLayerBuilder.nIn(previous_layer_out);
				hiddenLayerBuilder.nOut(previous_layer_out*growth_Rate);
				hiddenLayerBuilder.activation(Activation.IDENTITY);
				listBuilder.layer(i, hiddenLayerBuilder.build());
				previous_layer_out/=growth_Rate;
				System.out.println("--- "+i);
			}
		}
		RnnOutputLayer.Builder outputLayerBuilder = new RnnOutputLayer.Builder(LossFunction.MEAN_ABSOLUTE_ERROR);
		outputLayerBuilder.activation(Activation.LEAKYRELU);
		outputLayerBuilder.nIn(previous_layer_out);
		outputLayerBuilder.nOut(in_out_size);
		listBuilder.layer(hidden_layer_count+1, outputLayerBuilder.build());
		
		MultiLayerConfiguration conf = listBuilder.build();
	
		net = new MultiLayerNetwork(conf);
		net.init();
		net.setListeners(new ScoreIterationListener(1000));
		save();
	}
	static class Training{
		public static void load() {
			ArrayList<String> dataDirectories = File_Reading.FilesInDirectory("/Stock Data/BTC-USD");
			ArrayList<ArrayList<Double>> stocks = new ArrayList<ArrayList<Double>>();
			for(int i=0;i<dataDirectories.size();i++) {
				String dir = dataDirectories.get(i).replace(Database.basedir+"/", "").replace(".txt", "");
				stocks.add(Read_And_Write.File_Manipulation.AI.ReadStockFile(dir));
			}
			System.out.println(stocks);
		}
		public static void train() {
			
		}
	}
}
