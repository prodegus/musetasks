package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleIntegerProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import static prodegus.musetasks.contacts.StudentModel.getStudentFromDB;
import static prodegus.musetasks.datastructures.Collections.mostCommon;

public class Parent extends Contact {

    private SimpleIntegerProperty childId1 = new SimpleIntegerProperty();
    private SimpleIntegerProperty childId2 = new SimpleIntegerProperty();
    private SimpleIntegerProperty childId3 = new SimpleIntegerProperty();
    private SimpleIntegerProperty childId4 = new SimpleIntegerProperty();
    private SimpleIntegerProperty childId5 = new SimpleIntegerProperty();

    public int getChildId1() {
        return childId1.get();
    }

    public SimpleIntegerProperty childId1Property() {
        return childId1;
    }

    public void setChildId1(int childId1) {
        this.childId1.set(childId1);
    }

    public int getChildId2() {
        return childId2.get();
    }

    public SimpleIntegerProperty childId2Property() {
        return childId2;
    }

    public void setChildId2(int childId2) {
        this.childId2.set(childId2);
    }

    public int getChildId3() {
        return childId3.get();
    }

    public SimpleIntegerProperty childId3Property() {
        return childId3;
    }

    public void setChildId3(int childId3) {
        this.childId3.set(childId3);
    }

    public int getChildId4() {
        return childId4.get();
    }

    public SimpleIntegerProperty childId4Property() {
        return childId4;
    }

    public void setChildId4(int childId4) {
        this.childId4.set(childId4);
    }

    public int getChildId5() {
        return childId5.get();
    }

    public SimpleIntegerProperty childId5Property() {
        return childId5;
    }

    public void setChildId5(int childId5) {
        this.childId5.set(childId5);
    }

    public Student child1() {
        if (this.getChildId1() == 0) return null;
        return getStudentFromDB(getChildId1());
    }

    public String child1name() {
        if (this.getChildId1() == 0) return "";
        return this.child1().name();
    }

    public List<Student> children() {
        List<Student> children = new ArrayList<>();
        if (this.child1() != null) children.add(this.child1());
        if (this.child2() != null) children.add(this.child2());
        if (this.child3() != null) children.add(this.child3());
        if (this.child4() != null) children.add(this.child4());
        if (this.child5() != null) children.add(this.child5());
        return children;
    }

    public Student child2() {
        if (this.getChildId2() == 0) return null;
        return getStudentFromDB(getChildId2());
    }

    public String child2name() {
        if (this.getChildId2() == 0) return "";
        return this.child2().name();
    }

    public Student child3() {
        if (this.getChildId3() == 0) return null;
        return getStudentFromDB(getChildId3());
    }

    public Student child4() {
        if (this.getChildId4() == 0) return null;
        return getStudentFromDB(getChildId4());
    }

    public Student child5() {
        if (this.getChildId5() == 0) return null;
        return getStudentFromDB(getChildId5());
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        super.setAttributes(rs);
        this.setChildId1(rs.getInt("childid1"));
        this.setChildId2(rs.getInt("childid2"));
        this.setChildId3(rs.getInt("childid3"));
        this.setChildId4(rs.getInt("childid4"));
        this.setChildId5(rs.getInt("childid5"));
    }

    public String valuesToSQLUpdateString() {
        StringBuilder sb = new StringBuilder(super.valuesToSQLUpdateString()).append(", ");
        sb.append("childid1 = ").append(this.getChildId1() == 0 ? "null" : this.getChildId1()).append(", ");
        sb.append("childid2 = ").append(this.getChildId2() == 0 ? "null" : this.getChildId2()).append(", ");
        sb.append("childid3 = ").append(this.getChildId3() == 0 ? "null" : this.getChildId3()).append(", ");
        sb.append("childid4 = ").append(this.getChildId4() == 0 ? "null" : this.getChildId4()).append(", ");
        sb.append("childid5 = ").append(this.getChildId5() == 0 ? "null" : this.getChildId5());
        return sb.toString();
    }

    public String sqlColumns() {
        StringJoiner columns = new StringJoiner(", ");
        columns.add(super.sqlColumns());
        if (this.getChildId1() != 0) columns.add("childid1");
        if (this.getChildId2() != 0) columns.add("childid2");
        if (this.getChildId3() != 0) columns.add("childid3");
        if (this.getChildId4() != 0) columns.add("childid4");
        if (this.getChildId5() != 0) columns.add("childid5");
        return columns.toString();
    }

    public String sqlValues() {
        StringJoiner values = new StringJoiner("', '", "'", "'").setEmptyValue("");
        StringJoiner result = new StringJoiner(", ").add(super.sqlValues());
        if (this.getChildId1() != 0) values.add(String.valueOf(this.getChildId1()));
        if (this.getChildId2() != 0) values.add(String.valueOf(this.getChildId2()));
        if (this.getChildId3() != 0) values.add(String.valueOf(this.getChildId3()));
        if (this.getChildId4() != 0) values.add(String.valueOf(this.getChildId4()));
        if (this.getChildId5() != 0) values.add(String.valueOf(this.getChildId5()));
        if (!values.toString().isEmpty()) result.add(values.toString());
        return result.toString();
    }

    public Teacher mainTeacher() {
        List<Student> children = this.children();
        List<Teacher> teachers = new ArrayList<>();

        for (Student child : children) {
            if (child.teacher1() != null) teachers.add(child.teacher1());
            if (child.teacher2() != null) teachers.add(child.teacher2());
            if (child.teacher3() != null) teachers.add(child.teacher3());
        }

        return mostCommon(teachers);
    }

    public String status() {
        return "";
    }

    public void addChildInDB(int childId) {
        if (this.hasChild(childId)) return;

        if (this.getChildId1() == 0) {
            ContactModel.updateContact(this, "childid1", childId);
            return;
        }
        if (this.getChildId2() == 0) {
            ContactModel.updateContact(this, "childid2", childId);
            return;
        }
        if (this.getChildId3() == 0) {
            ContactModel.updateContact(this, "childid3", childId);
            return;
        }
        if (this.getChildId4() == 0) {
            ContactModel.updateContact(this, "childid4", childId);
            return;
        }
        if (this.getChildId5() == 0) {
            ContactModel.updateContact(this, "childid5", childId);
        }
    }

    public boolean hasChild(int childId) {
        for (Student child : this.children()) {
            if (child.getId() == childId) return true;
        }
        return false;
    }
}
