package model;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();
    T findById(int id);
    T findByName(String name);
    T parseLine(String line);
}
