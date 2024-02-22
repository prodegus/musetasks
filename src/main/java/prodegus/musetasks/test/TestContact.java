package prodegus.musetasks.test;

import javafx.beans.property.SimpleStringProperty;

public class TestContact {
    private SimpleStringProperty id = new SimpleStringProperty();
    private SimpleStringProperty lastname = new SimpleStringProperty();
    private SimpleStringProperty firstname = new SimpleStringProperty();

    TestContact(String id, String lastname, String firstname) {
        this.setId(id);
        this.setLastname(lastname);
        this.setFirstname(firstname);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getLastname() {
        return lastname.get();
    }

    public SimpleStringProperty lastnameProperty() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    public String getFirstname() {
        return firstname.get();
    }

    public SimpleStringProperty firstnameProperty() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }
}
