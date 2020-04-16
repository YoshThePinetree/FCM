package defo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

///////////////////////////////////////////////////////////////////////////////////
public class FCM {
	public void FuzzyCMeans(){	// FCM algorithm method
		File file = new File("C:\\JavaIO\\Input\\g2-2-30.txt");	// The file name
		int d = 2;	// the number of dimension of the data
		int data1[][] = DataRead(file,d);	// Load the data
		System.out.println();

		///////////////////////////////
		// Paramter Set & Initiation //
		///////////////////////////////
		int c=2;							// the number of clusters
		int n=data1.length;					// the number of elements
		int maxite=300;						// the number of maximum iteration
		double q=1.4;						// the fuzzifier parameter
		int rseed=1;						// random seed
		String dist="Euclid";				// the distance metric type
		double F [] = new double [maxite];	// Objective function value


		// data normalization
		double data[][] = new double[c][n];
//		data=NormStd.Normalization(data1);
		data=NormStd.Standardization(data1);


		Sfmt rnd = new Sfmt(rseed);
		//
		double U [][] = new double [n][c];	// the membership function
		for(int i=0; i<n; i++) {
			for(int j=0; j<c; j++) {
				U[i][j] = rnd.NextUnif();	// initiation by random number
			}
		}

		double Xctr [][] = new double [c][d];	// the coordinate of cluster center


		///////////////////
//******// FCM Main Loop //********************************************************
		///////////////////

		for(int ite=0; ite<maxite; ite++) {
			Xctr = CalcCtr(U,data,q);					// the cluster center update
			U = CalcMembership(data,Xctr,q,dist);		// the membership degree updata
			F[ite] = CalcObjFunc(U,data,Xctr,q,dist);	// Objective function update

			System.out.printf("Iteration: \t");
			System.out.printf("%d\t",ite+1);
			System.out.printf("OF Value: \t");
			System.out.printf("%.3f\n",F[ite]);
		}

//*********************************************************************************

		// Data output
		String Ufile = "C:\\JavaIO\\Output\\U.txt";
		DataWrite(Ufile,U);
		String Xfile = "C:\\JavaIO\\Output\\X.txt";
		DataWrite(Xfile,data);
		String Xctrfile = "C:\\JavaIO\\Output\\Xctr.txt";
		DataWrite(Xctrfile,Xctr);

	}

///////////////////////////////////////////////////////////////////////////////////

		/////////////
//******// Methods //**************************************************************
		/////////////
	//public int[][] DataRead(int n){
	public static int[][] DataRead(final File file, int d) {
        List<ArrayList<Integer>> lists = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < d; i++) {
            lists.add(new ArrayList<Integer>());
        }
        BufferedReader br = null;
        try {
            // Read the file, save data to List<Integer>
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                // Add nth integer to lists[n]
                List<Integer> ints = parse_line(line,d);
                for (int i = 0; i < d; i++) {
                    (lists.get(i)).add(ints.get(i));
                }
            }

            // convert lists to 2 Integer[]
            Integer[] array1 = lists.get(0).toArray(new Integer[lists.size()]);
            int n=array1.length;
            int data[][] = new int[n][d];

            int j=0;
            while(j<d) {
            	// convert lists to 2 Integer[]
                Integer[] array = lists.get(j).toArray(new Integer[lists.size()]);
	            for(int i=0; i<n; i++) {
	                	data[i][j]=array[i];
	            }

	            j++;
            }

            return data;

        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception ex) {
                // ignore error
            }
        }
        return null;
    }

    // parse 2 integers as a line of String
    private static List<Integer> parse_line(String line, int d) throws Exception {
        List<Integer> ans = new ArrayList<Integer>();
        StringTokenizer st = new StringTokenizer(line, " ");
        if (st.countTokens() != d) {
            throw new Exception("Bad line: [" + line + "]");
        }
        while (st.hasMoreElements()) {
            String s = st.nextToken();
            try {
                ans.add(Integer.parseInt(s));
            } catch (Exception ex) {
                throw new Exception("Bad Integer in " + "[" + line + "]. " + ex.getMessage());
            }
        }
        return ans;
    }

    private static void DataWrite(String file_name, double data[][]) {
    	int n = data.length;	// the number of rows
    	int m = data[0].length;	// the number of colmuns

    	try {
            PrintWriter pw = new PrintWriter(file_name);
            for(int i=0; i<n; i++) {
            	for(int j=0; j<m; j++) {
            		if(j<m-1) {
                		pw.format("%.3f\t", data[i][j]);
            		}else {
                		pw.format("%.3f\n", data[i][j]);
            		}
                }
            }

            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // cluster centroid calculation method
    private static double [][] CalcCtr(double U[][], double X[][], double q){
    	int n = X.length;		// the number of elements
       	int d = X[0].length;	// the number of dementions
    	int c = U[0].length;	// the number of clusters
    	double ctr [][] = new double [c][d];	// the cluster center
    	double nsum [] = new double[d];
    	double dsum [] = new double[d];

		for(int i=0; i<c; i++) {	    // loop for cluster
    		Arrays.fill(nsum,0);
    		Arrays.fill(dsum,0);

			for(int j=0; j<n; j++) {	// loop for element
				for(int k=0; k<d; k++) {	    // loop for demension
					dsum[k] = dsum[k] + Math.pow(U[j][i], q);
					nsum[k] = nsum[k] + (Math.pow(U[j][i], q)*X[j][k]);
				}
			}
			for(int k=0; k<d; k++) {	    // loop for demension
				ctr[i][k] = nsum[k]/dsum[k];
			}
		}

    	return ctr;

    }

    private static double [][] CalcMembership(double X[][], double ctr[][], double q, String dist){
    	int n = X.length;		// the number of elements
//    	int d = X[0].length;	// the number of dementions
    	int c = ctr.length;	// the number of clusters
    	double U [][] = new double [n][c];	// the membership function
    	double sum, num, den;

    	for(int i=0; i<n; i++) {
    		for(int j=0; j<c; j++) {
    			sum=0;
    			for(int k=0; k<c; k++) {
    				num = Dist2Points(X[i],ctr[j],dist);
    				den = Dist2Points(X[i],ctr[k],dist);
    				sum = sum + Math.pow(num/den,1/(q-1));
    			}
        		U[i][j] = Math.pow(sum,-1);
        	}
     	}

    	return U;
    }

    private static double CalcObjFunc(double U[][], double X[][], double ctr[][], double q, String dist) {
    	double F=0, sum=0;
		int n=U.length;			// the number of elements
		int c=U[0].length;		// the number of clusters

		for(int i = 0; i<n; i++) {
			for(int j = 0; j<c; j++) {
				sum = sum + (Math.pow(U[i][j],q)*(Dist2Points(X[i],ctr[j],dist)));
			}
		}
		F = sum;

    	return F;
    }

    private static double Dist2Points(double X[], double Y[], String dist) {
    	int d = X.length;
    	double D = 0;
//		int count=1;
		double xsum=0;

		switch (dist) {
		case "Euclid":	// Metric: Euclidean distance

			for(int i=0; i<d; i++) {
				xsum = xsum + Math.pow((X[i]-Y[i]),2);
			}
			D=Math.sqrt(xsum);

			break;
			/*
		case "SEuclid":	// Metric: Scaled Euclidean distance
			double[] X = new double[nd];
			double[] sigma = new double[nf];
			double val;

			StatCalc stat = new StatCalc();
			for(int i=0; i<nf; i++) {
				X = getCul(data,i);
				sigma[i] = stat.Var(X);
			}

			for(int i=0; i<nd; i++) {
				for(int j=count; j<nd; j++) {
					for(int k=0; k<nf; k++) {
						val = ((Math.pow((data[i][k]-data[j][k]),2)) / sigma[k]);
						if(Double.isNaN(val) == false) {
							xsum = xsum + val;
						}
					}
					D[i][j]=Math.sqrt(xsum);
					xsum = 0;
				}
				count = count+1;
			}
			D = Dissim.matUcopy2L(D);

			break;
		case "City":	// Metric: city block distance

			for(int i=0; i<nd; i++) {
				for(int j=count; j<nd; j++) {
					for(int k=0; k<nf; k++) {
						xsum = xsum + Math.abs(data[i][k]-data[j][k]);
					}
					D[i][j] = xsum;
					xsum = 0;
				}
				count = count+1;
			}
			D = Dissim.matUcopy2L(D);

			break;
		case "Chebyshev":	// Metric: Chebyshev distance
			double dmax = 0;
			double a = 0;

			for(int i=0; i<nd; i++) {
				for(int j=count; j<nd; j++) {
					for(int k=0; k<nf; k++) {
						a = Math.abs(data[i][k]-data[j][k]);
						if(dmax < a) {
							dmax = a;
						}
					}
					D[i][j] = dmax;
					dmax=0;
				}
				count=count+1;
			}
			D = Dissim.matUcopy2L(D);

			break;
		case "Mahalanobis":
			break;
		case "Cosine":
			break;
			*/
		}
		return D;
	}

//*********************************************************************************


}
