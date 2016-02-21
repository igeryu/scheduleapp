
import domain.Person;
import domain.PersonDAO;
import forms.AddPersonFrame;
import java.util.ArrayList;
import util.DBConnectionPool;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author alanjohnson
 */
public class TestDriver {
    
    public static void main (String[] args) {
        PersonDAO personDao = new PersonDAO();
        
        //personDao.addPerson("Test01_First", "Test01_Last");
        //personDao.addPerson("Test02_First", "Test02_Last");
        
        ArrayList<Person> personList = personDao.getPeople();
        
//        for (Person p : personList) {
//            System.out.printf("\nPerson: \"%s %s\"", p.getFirstName(), p.getLastName());
//        }
        
        AddPersonFrame frame = new AddPersonFrame();
        frame.setVisible(true);
        
    }
    
}
