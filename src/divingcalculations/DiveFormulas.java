package divingcalculations;

/**
 * Write a description of class Formulas here.
 *
 * @author ()
 * @version (a version number or a date)
 */
public class DiveFormulas {

    public int calculateMOD(float ppo, int fg) {
        float p = ppo / (fg / 100.0f);
        int depth = (int) ((p - 1) * 10);
        return depth;
    }

    /**
     * @param ppo   the partial pressure of Oxygen (between 1.1 and 1.6 inclusive)
     * @param depth the depth of the dive (in metres)
     * @return Fraction of Oxygen (Best Mix)
     */
    public int calculateBM(float ppo, float depth) {
        float p = depth / 10 + 1;
        int bm = (int) ((ppo / p) * 100);
        return bm;
    }


    public int calculateEAD(float depth, int fg) {
        float p = depth / 10 + 1;
        float ataEDA = (1 - fg / 100f) * p / 0.79f;
        //2.999==>2
        //2.999==>3
        int depthEDA = Math.round((ataEDA - 1) * 10);
        return depthEDA;
    }


    public float calculatePP(float depth, int fg) {
        float p = depth / 10 + 1;
        return fg / 100f * p;
    }
}
