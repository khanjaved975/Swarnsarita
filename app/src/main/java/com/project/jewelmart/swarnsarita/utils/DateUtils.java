package com.project.jewelmart.swarnsarita.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.provider.Settings.System.DATE_FORMAT;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class DateUtils {

    public static Date addDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }

    public static Date addMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(MONTH, i);

        return cal.getTime();
    }

    public static Date addYear(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(YEAR, i);
        return cal.getTime();
    }

    public static String CalculateDaysBetweenDates(String startdate, String endDate) {
        String dates = "";
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date dateBefore = myFormat.parse(startdate);
            Date dateAfter = myFormat.parse(endDate);
            long difference = dateAfter.getTime() - dateBefore.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            int workDays = 0;
            int weekoff = 0;
//                    Date startDate= null;
//                    Date endDate= null;
//                Date startDate = sdf.parse(Day + "/" + month + "/" + year);
//                Date endDate = sdf.parse(strDay + "/" + month + "/" + year);

            Calendar startCal = Calendar.getInstance();
            startCal.setTime(dateBefore);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(dateAfter);

            do {
                //excluding start date
                startCal.add(Calendar.DAY_OF_MONTH, 1);
                if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    ++workDays;

                } else
                    weekoff++;
            } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());


            float daysBetween = (difference / (1000 * 60 * 60 * 24));

//            dates = String.valueOf(daysBetween);
            dates = String.valueOf(workDays);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dates;
    }


    public static String CalculateDaysCalenderDays(String startdate, String endDate) {
        String dates = "";
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date dateBefore = myFormat.parse(startdate);
            Date dateAfter = myFormat.parse(endDate);
            long difference = dateAfter.getTime() - dateBefore.getTime();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            int workDays = 0;
            int weekoff = 0;
//                    Date startDate= null;
//                    Date endDate= null;
//                Date startDate = sdf.parse(Day + "/" + month + "/" + year);
//                Date endDate = sdf.parse(strDay + "/" + month + "/" + year);

            Calendar startCal = Calendar.getInstance();
            startCal.setTime(dateBefore);

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(dateAfter);

            do {
                //excluding start date
                startCal.add(Calendar.DAY_OF_MONTH, 1);
                if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    ++workDays;

                } else
                    weekoff++;
            } while (startCal.getTimeInMillis() < endCal.getTimeInMillis());

            float daysBetween = (difference / (1000 * 60 * 60 * 24));

//            dates = String.valueOf(daysBetween);
            dates = String.valueOf(daysBetween);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dates;
    }

    public static String changeDateFormate(String sDate) {
        DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

        Date date = null;
        try {
            date = inputFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public static String changeDateToDesireFormate(String sDate, DateFormat inputFormat, DateFormat outputFormat) {
//        DateFormat inputFormat = new2 SimpleDateFormat("dd/MM/yyyy");
//        DateFormat outputFormat = new2 SimpleDateFormat("MM/dd/yyyy");

        if (!sDate.isEmpty()) {
            Date date = null;
            try {
                date = inputFormat.parse(sDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date != null) {
                String outputDateStr = outputFormat.format(date);
                return outputDateStr;
            } else {
                return sDate.replaceAll("\\'", "");
            }
        } else {
            return sDate;
        }
    }

    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    public static boolean compareFromToDate(String sipDate, String sipStartDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String str1 = sipDate;
            Date date1 = formatter.parse(str1);
            String str2 = sipStartDate;
            Date date2 = formatter.parse(str2);

            if (date1.compareTo(date2) < 0) {
                System.out.println("date2 is Greater than my date1");
                return true;
            } else {
                return false;
            }

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public static String getTodaysDate() {
        /*Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String year = calendar.get(Calendar.YEAR) + "";
        String month = calendar.get(Calendar.MONTH) + 1 + "";
        String Day = calendar.get(Calendar.DAY_OF_MONTH) + "";
        String strDate = month + "/" + Day + "/" + year;*/

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("MM/yyyy");
        String formattedDate = df.format(c);


        return formattedDate;
    }

    public static Date convertStringToDate(String date) {
        String dtStart = date;
        Date fromdate = null;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        try {
            fromdate = format.parse(dtStart);
            System.out.println(fromdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fromdate;
    }

    public static String convertDateToString(Date date) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String reportDate = df.format(date);
        return reportDate;
    }

    public static String removeDayFromDate(String sDate) {
        DateFormat inputFormat = new SimpleDateFormat("dd-MMM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("MMM-yyyy");

        Date date = null;
        try {
            date = inputFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        return outputDateStr;
    }

    public static String getSummaryDate(int numDays) {

        Date currentdate = Calendar.getInstance().getTime();
        SimpleDateFormat df_month = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date toDate = DateUtils.addDay(currentdate, numDays);
        String strToDate = df_month.format(toDate);
        String strFromdate = df_month.format(currentdate);

        return "Sent Date: " + strFromdate + "\nValid till: " + strToDate;
    }

    public static String getTodayDate() {
        Date currentdate = Calendar.getInstance().getTime();
        SimpleDateFormat df_month = new SimpleDateFormat("MM/dd/yyyy");

        String strFromdate = df_month.format(currentdate);
        return strFromdate;
    }

    public static String getFinacialFromDate(boolean isFromDate) {
        Date currentdate = Calendar.getInstance().getTime();
        SimpleDateFormat df_month = new SimpleDateFormat("MM/dd/yyyy");
        String strFromdate = "";

        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;

        if (isFromDate) {
            if (month < 3) {
                year = year - 2;
//            System.out.println("Financial Year : " + (year - 1) + "-" + year);
            } else {
                year = year - 1;
//            System.out.println("Financial Year : " + year + "-" + (year + 1));
            }
            strFromdate = "04/01/" + year;
        } else {
            if (month < 3) {
                year = year - 1;
            } else {

            }
            strFromdate = "03/31/" + year;
        }


//        strFromdate = "04/01/" + year;
        return strFromdate;
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }


    public static boolean getDateMaximumValidation(String dateto, String bdate, int max) {
        boolean isproceed = true;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatter1 = new SimpleDateFormat("MM-dd-yyyy");

        String todatestring[] = dateto.split("/");
        String bdatestring[] = bdate.split("-");

//                                int days = Integer.parseInt(calculatedDays);
        int years = Integer.parseInt(todatestring[2]) - Integer.parseInt(bdatestring[2]);
        int mpnths = Integer.parseInt(todatestring[1]) - Integer.parseInt(bdatestring[0]);
        int days = Integer.parseInt(todatestring[0]) - Integer.parseInt(bdatestring[1]);

        try {
            if (mpnths < 0) {
                years = years - 1;
                mpnths = 12 + mpnths;
            }
            if (years >= max) {
                if (years == max) {
                    if (mpnths > 0)
                        isproceed = false;
                    else {
                        if (days > 0)
                            isproceed = false;

                    }
                } else
                    isproceed = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isproceed;

    }

}
