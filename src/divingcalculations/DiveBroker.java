package divingcalculations;

import java.util.List;
import java.util.Scanner;

public class DiveBroker {
    private Scanner scanner = new Scanner(System.in);

    private String[] menuNames = {"Help", "MOD", "SMOD", "BM", "PP", "PPT", "EAD", "EADT"};

    /**
     * 获取用户输入的菜单名称， 如果用户输入异常，则提示无效，并让用户重新输入
     *
     * @return
     */
    public String getInputMenuName() {
        String input = scanner.next();

        for (int i = 0; i < menuNames.length; i++) {
            if (menuNames[i].equalsIgnoreCase(input)) {
                scanner.nextLine();
                return input;
            }
        }
        System.out.print("Invalid Option. Please reenter:");

        return getInputMenuName();
    }

    /**
     * 获取用户是否继续计算的input
     *
     * @return
     */
    public boolean getInputContinue() {
        String input = scanner.next();
        if (input.equalsIgnoreCase("y")) {
            return true;
        } else if (input.equalsIgnoreCase("n")) {
            return false;
        }

        System.out.print("Invalid Option. Please reenter:");
        scanner.nextLine();
        return getInputContinue();
    }

    /**
     * 获取用户输入的depth，如果用户输入异常，则提示无效，并让用户重新输入
     *
     * @return
     */
    public float getDepth() {
        String input = scanner.next();
        float depth;
        try {
            depth = Float.parseFloat(input);
        } catch (NumberFormatException e) {
            System.out.print("Invalid Option. Please reenter:");
            return getDepth();
        }

        scanner.nextLine();
        return depth;
    }

    /**
     * 获取用户输入的ppo，范围（1.1-1.6）如果用户输入异常，则提示无效，并让用户重新输入
     *
     * @return
     */
    public float getPressureOxygen() {
        String input = scanner.next();
        float pressure;
        try {
            pressure = Float.parseFloat(input);
            if (pressure > 1.6 || pressure < 1.1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.print("Invalid Option. Please reenter:");
            return getPressureOxygen();
        }

        scanner.nextLine();
        return pressure;
    }

    /**
     * 获取用户输入的fg，如果用户输入异常，则提示无效，并让用户重新输入
     *
     * @return
     */
    public int getPercentageOxygen() {
        String input = scanner.next();
        int percentage;
        try {
            percentage = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.print("Invalid Option. Please reenter:");
            return getPercentageOxygen();
        }

        scanner.nextLine();
        return percentage;
    }

    /**
     * 获取用户输入的范围值，以空格分割
     *
     * @param max 限制最大值
     * @param min 限制最小值
     * @return
     */
    public int[] getStartEndValues(int max, int min) {
        int[] result = new int[2];
        String input = scanner.nextLine();
        int start;
        int end;
        try {
            String[] data = input.split(" ");
            if (data.length != 2) {
                throw new NumberFormatException();
            }
            start = Integer.parseInt(data[0]);
            end = Integer.parseInt(data[1]);
            if (start >= end) {
                throw new NumberFormatException();
            } else if (start < min) {
                throw new NumberFormatException();
            } else if (end > max) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.print("Invalid Option. Please reenter:");
            return getStartEndValues(max, min);
        }

        result[0] = start;
        result[1] = end;

        return result;
    }
}



