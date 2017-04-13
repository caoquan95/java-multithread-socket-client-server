package controllers;

import daos.PersonDAO;
import models.Person;

import java.util.List;

/**
 * Created by dosontung on 4/9/17.
 */
public class PersonController extends Controller {
    private PersonDAO personDAO;

    public PersonController() {
        super();
        this.personDAO = this.daoFactory.getPersonDAO();
    }

    public String requestListAllTenant() {
        List<Person> tenants = this.personDAO.findAllTenant();
        return renderResult(tenants);
    }

    public String requestListAllTenantByRenter(String email) {
        List<Person> tenants = this.personDAO.findAllTenantByRenter(email);
        return renderResult(tenants);
    }

    public String createPerson(Person person) {
        if (this.personDAO.findByEmail(person.getEmail()).isEmpty()) {
            if (this.personDAO.insertPerson(person)) {
                int id = this.personDAO.findByEmail(person.getEmail()).get(0).getId();
                System.out.println("hello world " + id);
                return "success " + id;
            } else {
                return "Failed to create person";
            }
        } else {
            return "this email is existed";
        }

    }

    public String login(Person person) {
        if (this.personDAO.login(person.getEmail(), person.getPasswordHash()).isEmpty()) {
            return "Invalid Credential";
        } else {
            Person returnPerson = this.personDAO.findByEmail(person.getEmail()).get(0);
            return "success " + returnPerson.getId() + " " + returnPerson.getPersonType();
        }
    }
}
