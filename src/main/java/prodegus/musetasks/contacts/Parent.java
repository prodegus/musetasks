package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleIntegerProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

import static prodegus.musetasks.contacts.StudentModel.getStudentFromDB;

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
        return getStudentFromDB(getChildId1());
    }

    public String child1name() {
        if (this.getChildId1() == 0) return "";
        return this.child1().name();
    }

    public Student child2() {
        return getStudentFromDB(getChildId2());
    }

    public String child2name() {
        if (this.getChildId2() == 0) return "";
        return this.child2().name();
    }

    public Student child3() {
        return getStudentFromDB(getChildId3());
    }

    public String child3name() {
        if (this.getChildId3() == 0) return "";
        return this.child3().name();
    }

    public Student child4() {
        return getStudentFromDB(getChildId4());
    }

    public String child4name() {
        if (this.getChildId4() == 0) return "";
        return this.child4().name();
    }

    public Student child5() {
        return getStudentFromDB(getChildId5());
    }

    public String child5name() {
        if (this.getChildId5() == 0) return "";
        return this.child5().name();
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        super.setAttributes(rs);
        this.setChildId1(rs.getInt(15));
        this.setChildId2(rs.getInt(16));
        this.setChildId3(rs.getInt(17));
        this.setChildId4(rs.getInt(18));
        this.setChildId5(rs.getInt(19));
    }

    public String valuesToSQLString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.valuesToSQLString());
        sb.append(", '");
        sb.append(this.getChildId1());
        sb.append("', '");
        sb.append(this.getChildId2());
        sb.append("', '");
        sb.append(this.getChildId3());
        sb.append("', '");
        sb.append(this.getChildId4());
        sb.append("', '");
        sb.append(this.getChildId5());
        sb.append("'");
        return sb.toString();
    }

}
