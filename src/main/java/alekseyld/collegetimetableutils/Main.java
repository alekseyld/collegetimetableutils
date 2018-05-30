package alekseyld.collegetimetableutils;

import alekseyld.collegetimetableutils.entity.Arguments;
import alekseyld.collegetimetableutils.utils.DataUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.SUNDAY;

/**
 * Created by Alekseyld on 07.04.2018.
 */
public class Main {

    public static void main(String[] args) {

        if (args.length == 0)
            return;

        Arguments arguments = parseArgs(args);

        new TimeTableProcessor(arguments).process();
    }

    private static Arguments parseArgs(String[] args) {

        Arguments arguments = new Arguments();

        arguments.group = args[0].toUpperCase();

        String[] param = args[1].split("");

        for (int i = 0; i < param.length; i++){
            switch (param[i]) {
                case "t":
                    arguments.json = false;
                    break;
                case "j":
                    arguments.json = true;
                    break;
                case "n":

                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.DAY_OF_MONTH, 1);
                    if (c.get(Calendar.DAY_OF_WEEK) == SUNDAY) {
                        c.add(Calendar.DAY_OF_MONTH, 1);
                    }

                    try {
                        arguments.date = DataUtils.simpleDateFormat.parse(DataUtils.simpleDateFormat.format(c.getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "d":
                    arguments.countDay = Integer.parseInt(param[i + 1]);
                    break;
                case "s":
                    arguments.save = true;
                    break;
                case "o":
                    arguments.save = false;
                    break;
                case "i":
                    arguments.fromSite = true;
                    break;
                case "c":
                    arguments.fromSite = false;
                    break;
                case "b":
                    String  type = param[i + 1];
                    if (type.equals("x")) {

                        arguments.dayName = args[2];
                    } else if (type.equals("a")){

                        try {
                            arguments.date = DataUtils.simpleDateFormat.parse(args[2]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    break;
            }
        }

        return arguments;
    }

    public static void printOutData(String data) {
        System.out.println(data);
    }

    public static void printOutData(int data) {
        System.out.println(data);
    }
}
