package prodegus.musetasks.workspace;

import javafx.scene.control.ListCell;
import prodegus.musetasks.contacts.Contact;

public class ContactListCell extends ListCell<Contact> {
    public ContactListCell() {}

    @Override protected void updateItem(Contact item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText(null);
        } else {
            setText(item.getLastname() + ", " + item.getFirstname());
        }

    }
}
//        Einsatz im Programm:

//        contactsAllListView.setItems(contacts);
//        contactsAllListView.setCellFactory(new Callback<ListView<Contact>, ListCell<Contact>>() {
//            @Override
//            public ListCell<Contact> call(ListView<Contact> list) {
//                return new ContactListCell();
//            }
//        });


