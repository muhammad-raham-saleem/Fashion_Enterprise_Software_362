package model;

import java.util.*;

public class HR {

    //Hash map that maps each staff's unique ID to their object.
    private Map<Integer, Staff> staffList = new HashMap<>();

    //Get every staff member in the HR system.
    public Collection<Staff> getStaff () {
        return staffList.values();
    }

    //Return a specific staff member by their unique ID.
    public Staff getStaffById (int id) {
        return staffList.get(id);
    }

    //Add staff member to map
    public void addStaff (Staff s) {
        staffList.put(s.getID(), s);
    }

}