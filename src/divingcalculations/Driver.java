package divingcalculations;

/**
 * Write a description of class Driver here.
 *
 * @author ()
 * @version (a version number or a date)
 */
public class Driver {

    private boolean isExit = false; //标识是否退出，1.用户选择7，置为false，2.用户选择不继续计算，置为false

    private DiveBroker diveBroker;
    private DiveFormulas diveFormulas;

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.start();
    }

    public void start() {
        diveBroker = new DiveBroker();
        diveFormulas = new DiveFormulas();
        do {
            System.out.print("Which calculation do you wish to perform (Help/MOD/SMOD/BM/PP/PPT/EAD/EADT)?");
            String menuName = diveBroker.getInputMenuName();
            toMenu(menuName);
        } while (!isExit);

    }

    private void toMenu(String menuName) {
        if (menuName.equalsIgnoreCase("help")){
            showHelp();
        }else if (menuName.equalsIgnoreCase("mod")){
            toCalculateMOD();
        }else if (menuName.equalsIgnoreCase("smod")){
            toCalculateSMOD();
        }else if (menuName.equalsIgnoreCase("bm")){
            toCalculateBM();
        }else if (menuName.equalsIgnoreCase("pp")){
            toCalculatePP();
        }else if (menuName.equalsIgnoreCase("ead")){
            toCalculateEAD();
        }else if (menuName.equalsIgnoreCase("ppt")){
            toGeneratePPT();
        }else if (menuName.equalsIgnoreCase("eadt")){
            toGenerateEADT();
        }

        showContinue();
    }

    private void toGenerateEADT() {
        System.out.println("Generating Equivalent Air Depths Table");
        System.out.print("Enter a start and end percentage of Oxygen: ");
        int[] percentages = diveBroker.getStartEndValues(50, 18);
        System.out.print("Enter a start and end depth (in metres): ");
        int[] depths = diveBroker.getStartEndValues(70, 3);
        System.out.println("Equivalent Air Depth Table for " + percentages[0] + " to " + percentages[1] + " percent Oxygen " +
                "and depths of " + depths[0] + " to " + depths[1] + " metres");
        System.out.println("=================================================================================");

        StringBuilder header = new StringBuilder();
        header.append(String.format("%-4s", "\\"));//s->string
        for (int i = percentages[0]; i <= percentages[1]; i++) {
            header.append(String.format("%-4d", i));//d->int
        }
        System.out.println(header);
        System.out.println("");
        for (int i = depths[0]; i <= depths[1]; i = i + 3) {
            StringBuilder line = new StringBuilder();
            line.append(String.format("%-4d", i));
            for (int k = percentages[0]; k <= percentages[1]; k++) {
                int res = diveFormulas.calculateEAD(i, k);
                    line.append(String.format("%-4d", res));
            }
            System.out.println(line);
        }
    }

    private void toGeneratePPT() {
        System.out.println("Generating Partial Pressures Table");
        System.out.print("Enter a start and end percentage of Oxygen: ");
        int[] percentages = diveBroker.getStartEndValues(50, 18);
        System.out.print("Enter a start and end depth (in metres): ");
        int[] depths = diveBroker.getStartEndValues(70, 3);
        System.out.println("Partial Pressures Table for " + percentages[0] + " to " + percentages[1] + " percent Oxygen " +
                "and depths of " + depths[0] + " to " + depths[1] + " metres");
        System.out.println("=================================================================================");

        StringBuilder header = new StringBuilder();
        header.append(String.format("%-4s", "\\"));
        for (int i = percentages[0]; i <= percentages[1]; i++) {
            header.append(String.format("%-8d", i));
        }
        System.out.println(header);
        System.out.println("");
        for (int i = depths[0]; i <= depths[1]; i = i + 3) {
            StringBuilder line = new StringBuilder();
            line.append(String.format("%-4d", i));
            for (int k = percentages[0]; k <= percentages[1]; k++) {
                float res = diveFormulas.calculatePP(i, k);
                if (res > 1.6f) {
                    line.append(String.format("%-8s", ""));
                } else {
                    line.append(String.format("%-8.2f", res));
                }
            }
            System.out.println(line);
        }

    }

    private void toCalculateEAD() {
        System.out.println("Calculating the Equivalent Air Depth");
        System.out.print("Enter the depth of the dive (in metres): ");
        float depth = diveBroker.getDepth();
        System.out.print("Enter the percentage of Oxygen: ");
        int fg = diveBroker.getPercentageOxygen();
        System.out.println("Equivalent Air Depth for a dive with " + fg + "% O2 to a depth of "
                + depth
                + " metres is " + diveFormulas.calculateEAD(depth, fg) + " metres");
    }


    private void toCalculatePP() {
        System.out.println("Calculating the Partial Pressure");
        System.out.print("Enter the depth of the dive (in metres): ");
        float depth = diveBroker.getDepth();
        System.out.print("Enter the percentage of Oxygen: ");
        int fg = diveBroker.getPercentageOxygen();
        System.out.println("The partial pressure of Oxygen for a dive to " + depth + " metres with a percentage of Oxygen of "
                + fg + "% is " + diveFormulas.calculatePP(depth, fg) + " ata");
    }

    private void toCalculateBM() {
        System.out.println("Calculating the Best Mix");
        System.out.print("Enter the partial pressure of Oxygen (between 1.1 and 1.6 inclusive): ");
        float ppo = diveBroker.getPressureOxygen();
        System.out.print("Enter the depth of the dive (in metres): ");
        float depth = diveBroker.getDepth();
        System.out.println("Best mix for a dive to " + depth + "m is " + diveFormulas.calculateBM(ppo, depth) + "% O2");
    }

    private void toCalculateSMOD() {
        System.out.println("Calculating the MOD for the standard 1.4 partial pressure");
        System.out.print("Enter the percentage of Oxygen: ");
        int fg = diveBroker.getPercentageOxygen();
        System.out.println("Maximum operating depth for a dive with " + fg + "% O2 with a partial pressure of 1.4 is " +
                diveFormulas.calculateMOD(1.4f, fg) + " metres");
    }

    private void toCalculateMOD() {
        System.out.println("Calculating the MOD");
        System.out.print("Enter the percentage of Oxygen: ");
        int fg = diveBroker.getPercentageOxygen();
        System.out.print("Enter the partial pressure of Oxygen (between 1.1 and 1.6 inclusive): ");
        float ppo = diveBroker.getPressureOxygen();
        System.out.println("Maximum operating depth for a dive with " + fg + "% O2 with a partial pressure of " +
                ppo + " is " + diveFormulas.calculateMOD(ppo, fg) + " metres");
    }


    /**
     * 显示提示用户是否继续计算
     */
    private void showContinue() {
        System.out.print("\nWould you like to perform another calculation (y/n)? ");
        boolean isContinue = diveBroker.getInputContinue();
        if (!isContinue) {
            isExit = true;
        }
    }


    /**
     * 显示帮助信息
     */
    private void showHelp() {
        System.out.println("You can select from the following choices: \n" +
                "1. HELP prints this message.\n" +
                "2. MOD  (Maximum Operating Depth) for a supplied percentage of Oxygen and partial pressure.\n" +
                "3. SMOD (Standard Maximum Operating Depth) for a supplied percentage of Oxygen and a standard 1.4 partial pressure.\n" +
                "4. BM   (Best Mix) for a dive with a supplied partial pressure and depth (in metres).\n" +
                "5. PP   (Partial Pressure) for a supplied percentage of Oxygen and depth (in metres).\n" +
                "6. EAD  (Equivalent Air Depth) for a supplied percentage of Oxygen and depth (in metres).");
    }

}



