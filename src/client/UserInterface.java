package client;

/**
 * Created by caoquan on 4/7/17.
 */
public class UserInterface {
    public void showDelimiter() {
        System.out.println("===================================");
    }

    public void showChooseOptions(int start, int end) {
        StringBuilder stringBuilder = new StringBuilder("Please choose options ");
        for (int i = start; i < end; i++) {
            if (i == end - 1) {
                stringBuilder.append(end - 1 + " or " + end);
            } else {
                stringBuilder.append(i + ", ");
            }

        }
        System.out.print(stringBuilder.append(": "));
    }

    public void showMainMenu() {
        showDelimiter();
        System.out.println("Are you a Tenant or Renter");
        System.out.println("1. Tenant");
        System.out.println("2. Renter");
        System.out.println("3. Register a new account");
        System.out.println("4. Exit");
        showChooseOptions(1, 3);
    }

    public void showTenantMenu() {
        showDelimiter();
        System.out.println("1. Request Rentals by criteria.");
        System.out.println("2. Reserve a rental.");
        System.out.println("3. Back");
        showChooseOptions(1, 3);
    }

    public void showRenterMenu() {
        showDelimiter();
        System.out.println("1. List of apartments proposed by Renter.");
        System.out.println("2. List of tenants by Renter.");
        System.out.println("3. Tenant of a Rental.");
        System.out.println("4. Propose a new rental");
        System.out.println("5. Remove a rental");
        System.out.println("6. Back");
        showChooseOptions(1, 6);

    }

    public void showCriteriaMenu() {
        showDelimiter();
        System.out.println("1. All available rental");
        System.out.println("2. All available rental monthly below.");
        System.out.println("3. All available rental monthly with number of rooms.");
        System.out.println("4. Back");
        showChooseOptions(1, 4);
    }

}
