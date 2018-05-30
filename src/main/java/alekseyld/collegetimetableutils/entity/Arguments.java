package alekseyld.collegetimetableutils.entity;

import java.util.Date;

/**
 * Created by Alekseyld on 26.05.2018.
 */
public class Arguments {

    //Program param
    //"3 АПП-1" td7co - расписание 3 АПП-1 в текстовом виде на 7 дней из кэша в std out

    //Первый всегда группа
    public String group;

    //-t text = в текстовом представлении
    //-j json = в json представлении
    public boolean json = false;

    //-d1 day 1 = расписание на один день
    //-d7 day 7 = расписание на 7 дней
    public int countDay = 0;

    //-o out = вывести
    //-s save = сохранить в файл
    //При записи в файл возвращается строка с индексами дней изменений
    public boolean save = false;

    //i internet = из сайта
    //c cache = из кэша
    public boolean fromSite = true;

    //bx "Понедельник"
    public String dayName;

    //-n next = расписание на следующий день
    //ba "28.05.2018"
    public Date date;

}
