package client;

import actions.Action;
import actions.RequestPersonAction;
import actions.RequestRentalAction;
import actions.UpdateRentalAction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by caoquan on 4/5/17.
 */
public class TenantClient extends Client {

    public void requestAllAvailableRentals() {
        RequestRentalAction requestRentalAction = new RequestRentalAction(RequestRentalAction.ALLAVAILABLE);
        doAction(requestRentalAction);
    }

    public void requestAllMonthlyRentBelow() {

        try {
            System.out.print("Please input the amount of monthly rent: ");
            int amount = Integer.parseInt(sc.next());
            RequestRentalAction requestRentalAction = new RequestRentalAction(RequestRentalAction.RENT, amount);
            doAction(requestRentalAction);
        } catch (Exception e) {
            System.out.println("Error: Please input an integer number");
        }


    }

    public void requestAllAvailableByNumRoom() {
        try {
            System.out.print("Please input the number of rooms: ");
            int numRooms = Integer.parseInt(sc.next());
            RequestRentalAction requestRentalAction = new RequestRentalAction(RequestRentalAction.ROOM, numRooms);
            doAction(requestRentalAction);
        } catch (Exception e) {
            System.out.println("Error: Please input an integer number");
        }
    }

    public void reserveRental() {
        try {
            System.out.print("Please input the id of rental you want to reserve: ");
            int rentalId = Integer.parseInt(sc.next());

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = null, endDate = null;
            try {
                System.out.print("Please input the start date (yyyy-MM-dd): ");
                String startDateString = sc.next();
                startDate = df.parse(startDateString);
                System.out.print("Please input the end date (yyyy-MM-dd): ");
                String endDateString = sc.next();
                endDate = df.parse(endDateString);
            } catch (Exception e) {
                System.out.println("Date is invalid. Please input using the format yyyy-MM-dd");
            }

            try {

            } catch (Exception e) {
                System.out.println("Date is invalid. Please input using the format yyyy-MM-dd");
            }
            Action reserveRentalAction = new UpdateRentalAction(UpdateRentalAction.RESERVE_RENTAL, rentalId,
                    ClientContext.getInstance().getLoggedInPerson().getId(), df.format(startDate) + "," + df.format(endDate));
            doAction(reserveRentalAction);
        } catch (Exception exception) {
            System.out.println("Error: Please input an integer number");
        }

    }

    public void requestListAllTenant() {
        RequestPersonAction requestPersonAction = new RequestPersonAction(RequestPersonAction.ALLTENANT);
        doAction(requestPersonAction);
    }

    public void requestTenantOfRental() {
        try {
            System.out.print("Please input the id of rental( you can get it from option 1): ");
            int rentalId = Integer.parseInt(sc.next());
            RequestRentalAction requestRentalAction = new RequestRentalAction(RequestRentalAction.TENANT, rentalId);
            doAction(requestRentalAction);
        } catch (Exception exception) {
            System.out.println("Error: Please input an integer number");
        }

    }


}
