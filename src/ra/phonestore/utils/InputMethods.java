package ra.phonestore.utils;

import java.util.Scanner;

public class InputMethods {
    private static final Scanner scanner = new Scanner(System.in);

    public static String getString() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.err.println("Thông tin không được để trống. Vui lòng nhập lại!");
                continue;
            }
            return input;
        }
    }

    public static String getOptionalString() {
        return scanner.nextLine().trim();
    }

    public static int getInt() {
        while (true) {
            try {
                return Integer.parseInt(getString());
            } catch (NumberFormatException e) {
                System.err.println("Định dạng số nguyên không hợp lệ. Vui lòng nhập lại!");
            }
        }
    }

    public static double getDouble() {
        while (true) {
            try {
                return Double.parseDouble(getString());
            } catch (NumberFormatException e) {
                System.err.println("Định dạng số không hợp lệ. Vui lòng nhập lại!");
            }
        }
    }

    public static int getPositiveInt() {
        while (true) {
            int result = getInt();
            if (result > 0) return result;
            System.err.println("Giá trị phải là số nguyên dương (>0). Vui lòng nhập lại!");
        }
    }

    public static double getPositiveDouble() {
        while (true) {
            double result = getDouble();
            if (result > 0) return result;
            System.err.println("Giá trị phải lớn hơn 0. Vui lòng nhập lại!");
        }
    }

    public static boolean getBoolean() {
        while (true) {
            String input = getString().toLowerCase();
            if (input.equals("true") || input.equals("1") || input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("false") || input.equals("0") || input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.err.println("Invalid boolean input (true/false, y/n, 1/0).");
            }
        }
    }

    public static void pressEnterToContinue() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
