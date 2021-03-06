package daos;

import models.Apartment;
import models.Person;
import models.Rental;
import utils.PersonType;
import utils.RentalStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caoquan on 4/4/17.
 */
public class RentalDAOImpl implements RentalDAO {

    private Connection connection;
    private PersonDAO personDAO;
    private ApartmentDAO apartmentDAO;

    public RentalDAOImpl(Connection connection, PersonDAO personDAO, ApartmentDAO apartmentDAO) {
        this.connection = connection;
        this.personDAO = personDAO;
        this.apartmentDAO = apartmentDAO;
    }

    public List<Rental> getRentalsFromStatement(PreparedStatement statement) {
        List<Rental> rentals = new ArrayList<>();
        ResultSet result = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            result = statement.executeQuery();
            while (result.next()) {
                Rental newRental = null;
                Apartment newApartment = apartmentDAO.findById(result.getInt("apartment_id")).get(0);

                if (result.getInt("tenant_id") == 0) {
                    newRental = new Rental(RentalStatus.valueOf(result.getString("status")), newApartment);
                } else {
                    Person newPerson = personDAO.findById(result.getInt("tenant_id")).get(0);
                    newRental = new Rental(RentalStatus.valueOf(result.getString("status")), newApartment, newPerson);
                }
                newRental.setId(result.getInt("id"));

                if (result.getString("start_date") != null) {
                    newRental.setStartDate(df.parse(result.getString("start_date")));
                }
                if (result.getString("end_date") != null) {
                    newRental.setEndDate(df.parse(result.getString("end_date")));
                }

                System.out.println(newRental);
                rentals.add(newRental);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rentals;
    }

    @Override
    public List<Rental> findAll() {
        String sql = "SELECT * FROM rental";
        List<Rental> rentals = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            rentals = getRentalsFromStatement(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }

    @Override
    public List<Rental> findAllAvailable() {
        String sql = "SELECT * FROM rental WHERE status = ?";
        List<Rental> rentals = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, RentalStatus.AVAILABLE.toString());

            rentals = getRentalsFromStatement(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }

    @Override
    public List<Rental> findAllBelow(int amount) {
        String sql = "SELECT * FROM rental JOIN apartment ON rental.apartment_id = apartment.id WHERE  monthly_rent < ?";
        List<Rental> rentals = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, amount);
            rentals = getRentalsFromStatement(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }

    @Override
    public List<Rental> findAllNumberOfRoom(int amount) {
        String sql = "SELECT rental.id as id, apartment_id, status, tenant_id, startDate, endDate " +
                "FROM rental JOIN apartment ON rental.apartment_id = apartment.id " +
                "WHERE apartment.num_rooms = ? AND rental.status = ?";
        List<Rental> rentals = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, amount);
            statement.setString(2, RentalStatus.AVAILABLE.toString());

            rentals = getRentalsFromStatement(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }

    @Override
    public List<Rental> findById(int id) {
        String sql = "SELECT * FROM rental WHERE  id = ?";
        List<Rental> rentals = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            rentals = getRentalsFromStatement(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }

    @Override
    public List<Rental> findByRenterId(int renterId) {
        String sql = "SELECT * FROM rental WHERE apartment_id in (select id from apartment where renter_id = ?)";
        List<Rental> rentals = null;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, renterId);
            rentals = getRentalsFromStatement(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rentals;
    }

    @Override
    public List<Person> findTenantOfRental(int rentalId) {
        String sql = "SELECT * FROM rental WHERE  id = ?";
        String sql2 = "SELECT * FROM person WHERE id = ?";
        List<Person> persons = new ArrayList<>();
        int tenantId = 0;

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, rentalId);

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                tenantId = result.getInt("tenant_id");
            }
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, tenantId);

            ResultSet result2 = statement2.executeQuery();

            while (result2.next()) {
                Person newPerson = new Person(result2.getInt("id"), result2.getString("email"), PersonType.valueOf(result2.getString("type")));
                persons.add(newPerson);
                System.out.println(newPerson);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return persons;
    }

    @Override
    public boolean reserveRental(Rental rental, int tenantId) {
        String sql = "UPDATE rental SET tenant_id = ?,status = ?, start_date=?, end_date=? WHERE id = ?";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, tenantId);
            statement.setString(2, RentalStatus.RENTING.toString());
            statement.setString(3, df.format(rental.getStartDate()));
            statement.setString(4, df.format(rental.getEndDate()));
            statement.setInt(5, rental.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean insertRental(Rental rental) {
        String sql = "INSERT INTO rental (apartment_id,tenant_id,status) VALUES (?,?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, rental.getApartment().getId());
            statement.setInt(2, rental.getTenant().getId());
            statement.setString(3, rental.getStatus().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean updateRental(Rental rental) {
        String sql = "UPDATE rental SET apartment_id = ?,tenant_id = ?,status = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, rental.getApartment().getId());
            statement.setInt(2, rental.getTenant().getId());
            statement.setString(3, rental.getStatus().toString());
            statement.setInt(4, rental.getId());

            statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean deleteRental(int rentalId) {
        String sql = "DELETE FROM rental WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, rentalId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }


}
