package alekseyld.collegetimetableutils.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Alekseyld on 21.09.2017.
 */

public class Day {

    private int id;

    private String dateName;

    private Date date;

    private List<Lesson> dayLessons = new ArrayList<>();

    //Добавить Set преподавателей

    public String getDateName() {
        return dateName;
    }

    public Date getDate() {
        return date;
    }

    public Day setDate(Date date) {
        this.date = date;
        return this;
    }

    public Day setDateName(String dateName) {
        this.dateName = dateName;
        return this;
    }

    public int getId() {
        return id;
    }

    public Day setId(int id) {
        this.id = id;
        return this;
    }

    public List<Lesson> getDayLessons() {
        return dayLessons;
    }

    public Day setDayLessons(List<Lesson> dayLessons) {
        this.dayLessons = dayLessons;
        return this;
    }

    public String getDateFirstUpperCase(){
        if(dateName == null || dateName.isEmpty()) return "";//или return word;
        return dateName.substring(0, 1).toUpperCase() + dateName.substring(1).toLowerCase();
    }
}

