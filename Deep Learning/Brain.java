import org.datavec.api.records.reader.SequenceRecordReader;
import org.datavec.api.records.reader.impl.csv.CSVSequenceRecordReader;
import org.datavec.api.split.NumberedFileInputSplit;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.datasets.datavec.SequenceRecordReaderDataSetIterator;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerMinMaxScaler;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Brain {
	private static MultiLayerNetwork net;
	private static int in_out_size=20;
	private static int hidden_layer_count =3;
	private static int growth_Rate=2;
	private static double percent_training_data=75; // percent out of 100
	public static void startup() {
		build();
		/*
		File file = new File(Database.basedir+"/AI/Net.txt");
		if(file.exists()) {
			System.out.println("Loading Neural Network...");
			load();
		}else {
			System.out.println("Building Neural Network...");
			build();
		}
		*/
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
		builder.updater(new Adam(0.1));
		builder.weightInit(WeightInit.XAVIER);
		builder.gradientNormalization(GradientNormalization.RenormalizeL2PerParamType);
		builder.gradientNormalizationThreshold(0.5);
		
		ListBuilder listBuilder = builder.list();
		
		LSTM.Builder inputLayerBuilder = new LSTM.Builder();
		inputLayerBuilder.nIn(in_out_size);
		System.out.println("  ---  ");
		System.out.println(in_out_size);
		System.out.println(in_out_size*growth_Rate);
		inputLayerBuilder.nOut(in_out_size*growth_Rate);
		inputLayerBuilder.activation(Activation.IDENTITY);
		listBuilder.layer(0, inputLayerBuilder.build());
		int previous_layer_out=in_out_size*growth_Rate;
		for(int i=1;i<=hidden_layer_count/2;i++) {
			LSTM.Builder hiddenLayerBuilder = new LSTM.Builder();
			System.out.println("  ---  ");
			System.out.println(previous_layer_out);
			System.out.println(previous_layer_out*growth_Rate);
			hiddenLayerBuilder.nIn(previous_layer_out);
			hiddenLayerBuilder.nOut(previous_layer_out*growth_Rate);
			hiddenLayerBuilder.activation(Activation.TANH);
			listBuilder.layer(i, hiddenLayerBuilder.build());
			previous_layer_out*=growth_Rate;
		}
		if (!(hidden_layer_count % 2 == 0)) {
			LSTM.Builder hiddenLayerBuilder = new LSTM.Builder();
			System.out.println("  ---  ");
			System.out.println(previous_layer_out);
			System.out.println(previous_layer_out);
			hiddenLayerBuilder.nIn(previous_layer_out);
			hiddenLayerBuilder.nOut(previous_layer_out);
			hiddenLayerBuilder.activation(Activation.IDENTITY);
			listBuilder.layer((int) (1.5+((hidden_layer_count/2))), hiddenLayerBuilder.build());
			//previous_layer_out/=growth_Rate;
			for(int i=(int) ((hidden_layer_count/2)+2.5);i<=hidden_layer_count;i++) {
				LSTM.Builder hiddenLayerBuilder2 = new LSTM.Builder();
				System.out.println("  ---  ");
				System.out.println(previous_layer_out);
				System.out.println(previous_layer_out/growth_Rate);
				hiddenLayerBuilder2.nIn(previous_layer_out);
				hiddenLayerBuilder2.nOut(previous_layer_out/growth_Rate);
				hiddenLayerBuilder2.activation(Activation.IDENTITY);
				listBuilder.layer(i, hiddenLayerBuilder2.build());
				previous_layer_out/=growth_Rate;
			}
		}else {
			for(int i=(int) ((hidden_layer_count/2)+1.5);i<=hidden_layer_count;i++) {
				LSTM.Builder hiddenLayerBuilder = new LSTM.Builder();
				System.out.println("  ---  ");
				System.out.println(previous_layer_out);
				System.out.println(previous_layer_out/growth_Rate);
				hiddenLayerBuilder.nIn(previous_layer_out);
				hiddenLayerBuilder.nOut(previous_layer_out/growth_Rate);
				hiddenLayerBuilder.activation(Activation.IDENTITY);
				listBuilder.layer(i, hiddenLayerBuilder.build());
				previous_layer_out/=growth_Rate;
			}
		}
		RnnOutputLayer.Builder outputLayerBuilder = new RnnOutputLayer.Builder(LossFunctions.LossFunction.MSE);
		outputLayerBuilder.activation(Activation.LEAKYRELU);
		System.out.println("  ---  ");
		System.out.println(previous_layer_out);
		System.out.println(in_out_size);
		outputLayerBuilder.nIn(previous_layer_out);
		outputLayerBuilder.nOut(in_out_size);
		listBuilder.layer(hidden_layer_count+1, outputLayerBuilder.build());
		
		MultiLayerConfiguration conf = listBuilder.build();
	
		net = new MultiLayerNetwork(conf);
		net.init();
		/*
		 UIServer uiServer = UIServer.getInstance();

		    //Configure where the network information (gradients, score vs. time etc) is to be stored. Here: store in memory.
		    StatsStorage statsStorage = new InMemoryStatsStorage();         //Alternative: new FileStatsStorage(File), for saving and loading later

		    //Attach the StatsStorage instance to the UI: this allows the contents of the StatsStorage to be visualized
		    uiServer.attach(statsStorage);

		    //Then add the StatsListener to collect this information from the network, as it trains
		    net.setListeners(new StatsListener(statsStorage));
		    */
		net.setListeners(new ScoreIterationListener(5));
		save();
	}
	static class Training{
		public static void load() {
			ArrayList<String> folders = File_Reading.FoldersInDirectory(Database.basedir+"/Stock Data/BTC-USD/Data");
			ArrayList<ArrayList<Double>> stocks = new ArrayList<ArrayList<Double>>();
			//loading all data into arraylist
			for(int i=0;i<folders.size();i++) {
				String dir1 = folders.get(i);
				ArrayList<String> files = File_Reading.FilesInDirectory(dir1);
				ArrayList<Double> batch = new ArrayList<Double>();
				for(int ie=0;ie<files.size();ie++) {
					String dir = files.get(ie).replace(Database.basedir, "").replace(".txt", "");
					if(!dir.contains("DS_Store")) {
						ArrayList<Double> minibatch = Read_And_Write.AI.Read_Array(dir);
						batch.addAll(minibatch);
					}
					//System.out.println(dir);
				}
				//System.out.println("---\n---");
				stocks.add(batch);
			}
			//placing arraylist into CSV files
			double percent =percent_training_data/100;
			int trainingAmount = (int) (percent*stocks.size());
			int testingAmount = (int) (stocks.size()-trainingAmount);
			System.out.println(trainingAmount+"   "+testingAmount+" = "+(testingAmount+trainingAmount));
			//Training Data
			for(int i=0;i<trainingAmount;i++) {
				ArrayList<Double> data = stocks.get(i);
				ArrayList<ArrayList<Double>> minidata = new ArrayList<ArrayList<Double>>();
				for(int ie=0;ie<data.size();ie+=20) {
					ArrayList<Double> minidata2 = new ArrayList<Double>();
					for(int iee=ie;iee<ie+20;iee++) {
						minidata2.add(data.get(iee));
					}
					minidata.add(minidata2);
				}
				
				ArrayList<ArrayList<Double>> minifeat = new ArrayList<ArrayList<Double>>();
				minifeat.addAll(minidata);
				minifeat.remove(minifeat.size()-1);
				ArrayList<ArrayList<Double>> minilab = new ArrayList<ArrayList<Double>>();
				minilab.addAll(minidata);
				minilab.remove(0);
				
				CSV.MakeCSV("Train/Labels",Integer.toString(i), minilab);
				CSV.MakeCSV("Train/Features", Integer.toString(i), minifeat);
				
			}
			for(int i=trainingAmount;i<stocks.size();i++) {
				ArrayList<Double> data = stocks.get(i);
				ArrayList<ArrayList<Double>> minidata = new ArrayList<ArrayList<Double>>();
				for(int ie=0;ie<data.size();ie+=20) {
					ArrayList<Double> minidata2 = new ArrayList<Double>();
					for(int iee=ie;iee<ie+20;iee++) {
						minidata2.add(data.get(iee));
					}
					minidata.add(minidata2);
				}
				
				ArrayList<ArrayList<Double>> minifeat = new ArrayList<ArrayList<Double>>();
				minifeat.addAll(minidata);
				minifeat.remove(minifeat.size()-1);
				ArrayList<ArrayList<Double>> minilab = new ArrayList<ArrayList<Double>>();
				minilab.addAll(minidata);
				minifeat.remove(0);
				
				CSV.MakeCSV("Test/Labels",Integer.toString(i-trainingAmount), minilab);
				CSV.MakeCSV("Test/Features", Integer.toString(i-trainingAmount), minifeat);
			}
				try {
					train();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}
		public static void train() throws IOException, InterruptedException {
	        int miniBatchSize = 10;

	        // ----- Load the training data -----
	        SequenceRecordReader trainFeatures = new CSVSequenceRecordReader();
	        trainFeatures.initialize(new NumberedFileInputSplit("/Users/logan42474/Desktop/StockTrader/AI/TrainingData/Train/Features/%d.csv", 85, 86));
	        SequenceRecordReader trainLabels = new CSVSequenceRecordReader();
	        trainLabels.initialize(new NumberedFileInputSplit("/Users/logan42474/Desktop/StockTrader/AI/TrainingData/Train/Labels/%d.csv", 85, 86));

	        DataSetIterator trainDataIter = new SequenceRecordReaderDataSetIterator(trainFeatures, trainLabels, miniBatchSize, -1, true, SequenceRecordReaderDataSetIterator.AlignmentMode.EQUAL_LENGTH);

	        //Normalize the training data
	        
	        NormalizerMinMaxScaler normalizer = new NormalizerMinMaxScaler(0, 1);
	        normalizer.fitLabel(true);
	        normalizer.fit(trainDataIter);              //Collect training data statistics
	        trainDataIter.reset();
	        trainDataIter.setPreProcessor(normalizer);
	        
	        SequenceRecordReader testFeatures = new CSVSequenceRecordReader();
	        testFeatures.initialize(new NumberedFileInputSplit("/Users/logan42474/Desktop/StockTrader/AI/TrainingData/Test/Features/%d.csv", 0, 0));
	        SequenceRecordReader testLabels = new CSVSequenceRecordReader();
	        testLabels.initialize(new NumberedFileInputSplit("/Users/logan42474/Desktop/StockTrader/AI/TrainingData/Test/Labels/%d.csv", 0, 0));

	        DataSetIterator testDataIter = new SequenceRecordReaderDataSetIterator(testFeatures, testLabels, miniBatchSize, -1, true, SequenceRecordReaderDataSetIterator.AlignmentMode.ALIGN_END);

	       trainDataIter.setPreProcessor(normalizer);
	       testDataIter.setPreProcessor(normalizer);
	        
	        DataSet t = testDataIter.next();
            INDArray features = t.getFeatures();
            INDArray predicted = net.output(features,false);
            System.out.println(predicted);
	       for(int i=0;i<500;i++) {
	    	   net.fit(trainDataIter);
       		trainDataIter.reset();
	       }
	       INDArray predicted2 = net.output(features);
           System.out.println(predicted2);
		}
	}
}
