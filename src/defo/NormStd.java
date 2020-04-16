package defo;

public class NormStd {
	public static double[][] Normalization(int X[][]) {
		int nf = X.length;		// the number of feartures
		int nd = X[0].length;	// the number of dementions
		double Y[][] = new double [nf][nd];
		double num, den;
		StatCalcMat stat = new StatCalcMat();
		int min = stat.Min(X);
		int max = stat.Max(X);

		for (int i = 0; i < nf; i++) {
			for (int j = 0; j < nd; j++) {
				num = X[i][j]-min;
				den = max-min;
				Y[i][j] = num / den;
			}
		}
		return Y;
	}

	public static double[][] Standardization(int X[][]) {
		int nf = X.length;		// the number of feartures
		int nd = X[0].length;	// the number of dementions
		double Y[][] = new double [nf][nd];
		double num, den;
		StatCalcMat stat = new StatCalcMat();
		double mean = stat.Mean(X);
		double std = stat.Std(X);

		for (int i = 0; i < nf; i++) {
			for (int j = 0; j < nd; j++) {
				num = X[i][j]-mean;
				den = std;
				Y[i][j] = num / den;
			}
		}
		return Y;
	}

}
