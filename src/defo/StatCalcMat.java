package defo;

public class StatCalcMat {
	
	public int Min(int X[][]) {	// calculation of minimum value
		int min = X[0][0];
		for (int i = 0; i < X.length; i++) {
			for (int j = 0; j < X[0].length; j++) {
				int v = X[i][j];
				if (v < min) {
					min = v;
				}
			}
		}
		return min;
	}
	public int Max(int X[][]) {	// calculation of maximum value
		int max = X[0][0];
		for (int i = 0; i < X.length; i++) {
			for (int j = 0; j < X[0].length; j++) {
				int v = X[i][j];
				if (v > max) {
					max = v;
				}
			}

		}
		return max;
	}

	public double Mean(int X[][]) {	// calculation of mean value
		double mean;
		int nf = X.length;
		int nd = X[0].length;
		int n = nf*nd;			// the total number of the element
		int sum = 0;
		for (int i = 0; i < nf; i++) {
			for (int j = 0; j < nd; j++) {
				sum+=X[i][j];
			}
		}
		mean = sum/n;
		return mean;
	}

	public double Std(int X[][]) {	// calculation standard deviation
		double std;
		int nf = X.length;
		int nd = X[0].length;
		int n = nf*nd;			// the total number of the element
		double ssum = 0.0;
		double var;
		double mean;

		StatCalcMat stat = new StatCalcMat();
		mean=stat.Mean(X);

		for (int i = 0; i < nf; i++) {
			for (int j = 0; j < nd; j++) {
	            ssum += Math.pow((X[i][j] - mean),2);
			}
		}
		var = ssum / n;
		std = Math.sqrt(var);
		return std;
	}
}
