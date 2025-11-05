package hire;

public class HR {

    private int id;
    private String name;

    public void viewStaffList() {

    }

    public void hireStaff (Staff s) {

        

    }

    public void assignManager(Staff s, Manager m) {

        s.setManager(m);
        m.addManagee(s);

    }

}