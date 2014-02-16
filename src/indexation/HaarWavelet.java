package indexation;


public class HaarWavelet extends Wavelet {

    private static double coeff = 2;

    @Override
    protected double[] forwardStep(double[] values) {
        int num_c = values.length / 2;
        
        double[] result = new double[num_c * 2];

        for (int i = 0, j = 0; i < values.length; i += 2, j++) {
            result[j] = (values[i] + values[i + 1]) / coeff;
            result[num_c + j] = (values[i] - values[i + 1]) / coeff;
        }

       

        return result;
    }
}
