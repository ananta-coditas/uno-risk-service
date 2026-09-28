package com.genomebrain.backend.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Utility class containing methods to manipulate on Date Objects.
 */
@Service
public class DateConversionUtil {

    private final Logger log = LoggerFactory.getLogger(DateConversionUtil.class);

    /**
     * get the instant from string date with userTimeZone
     * @param date         string format MM/dd/yyyy
     * @param userTimeZone
     * @return Instant
     */
    public Instant getInstantDateFormat(String date, String userTimeZone) {
        //log.info("Entered into getInstantDateFormat method of DateConversionUtil to get instant Format of the passed date : {} and timezone {}", date, userTimeZone);
        Instant instant = null;
        TimeZone timeZone;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        if (userTimeZone != null) {
            timeZone = TimeZone.getTimeZone(userTimeZone);
        } else {
            timeZone = TimeZone.getTimeZone("US/Pacific");
        }
        try {
            formatter.setTimeZone(timeZone);
            Date startDate = formatter.parse(date);
            instant = startDate.toInstant();
        } catch (Exception e) {
            log.error("Exception in getInstantDateFormat method of DateConversionUtil with cause: ", e);
        }
        //log.info("Exit from getInstantDateFormat method of DateConversionUtil with instant: {}", instant);
        return instant;
    }

    public Instant getInstantDateForNewFormat(String date, String userTimeZone) {
        //log.info("Entered into getInstantDateFormat method of DateConversionUtil to get instant Format of the passed date : {} and timezone {}", date, userTimeZone);
        Instant instant = null;
        TimeZone timeZone;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        if (userTimeZone != null) {
            timeZone = TimeZone.getTimeZone(userTimeZone);
        } else {
            timeZone = TimeZone.getTimeZone("US/Pacific");
        }
        try {
            formatter.setTimeZone(timeZone);
            Date startDate = formatter.parse(date);
            instant = startDate.toInstant();
        } catch (Exception e) {
            log.error("Exception in getInstantDateFormat method of DateConversionUtil with cause: ", e);
        }
        //log.info("Exit from getInstantDateFormat method of DateConversionUtil with instant: {}", instant);
        return instant;
    }

    /**
     * get String date from Instant
     * with MM/dd/yyyy formate
     * @param date
     * @return String
     */
    public String getStringDateFormat(Instant date) {
        //log.info("Entered into getStringDateFormat method of DateConversionUtil with date:{}", date);
        String out = null;
        if (date != null) {
            Date dt = Date.from(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
            out = simpleDateFormat.format(dt);
        }
        //log.info("Exit frm getStringDateFormat method of DateConversionUtil with out:{}", out);
        return out;
    }

    /**
     * get String Time from Instant
     * @param date
     * @param format
     * @param userTimeZone
     * @return
     */
    public String getStringTimeFormat(Instant date, String format, String userTimeZone) {
        //log.info("Entered into getStringDateFormat method of DateConversionUtil with passed date: {}, format: {}, userTimeZone: {}", date, format, userTimeZone);
        String out = null;
        TimeZone timeZone;
        if (date != null) {
            Date dt = Date.from(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            if (userTimeZone != null) {
                timeZone = TimeZone.getTimeZone(userTimeZone);
            } else {
                timeZone = TimeZone.getTimeZone("US/Pacific");
            }
            simpleDateFormat.setTimeZone(timeZone);
            out = simpleDateFormat.format(dt);
        }
        //log.info("Exit from getStringDateFormat method of DateConversionUtil with passed out: {}", out);
        return out;
    }

    /**
     * get String time from Instant
     * with hh:mm a format
     * @param date
     * @return String
     */
    public String getStringTimeFormat(Instant date) {
        //log.info("Entered into getStringTimeFormat method of DateConversionUtil with date:{}", date);


        String out = null;
        if (date != null) {
            Date dt = Date.from(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
            out = simpleDateFormat.format(dt);
        }
        //log.info("Exit from getStringDateFormat method of DateConversionUtil with passed out: {}", out);
        return out;
    }

    /**
     * Return Instant Instance Of String Date
     *
     * @param date
     * @param format
     * @param userTimeZone
     * @return
     */
    public Instant getInstantDateFormat(String date, String format, String userTimeZone) {
        //log.info("Entered into getInstantDateFormat method of DateConversionUtil with passed date: {}, format: {}, userTimeZone: {}", date, format, userTimeZone);
        Instant instant = null;
        TimeZone timeZone;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if (userTimeZone != null) {
            timeZone = TimeZone.getTimeZone(userTimeZone);
        } else {
            timeZone = TimeZone.getTimeZone("US/Pacific");
        }
        try {
            formatter.setTimeZone(timeZone);
            Date startDate = formatter.parse(date);
            instant = startDate.toInstant();
        } catch (Exception e) {
            log.error("Exception in getInstantDateFormat method of DateConversionUtil with cause: ", e);
        }
        //log.info("Exit from getInstantDateFormat method of DateConversionUtil with passed instant: {}", instant);
        return instant;
    }

    /**
     * Formatted the Date Object as per Format
     *
     * @param date
     * @param format
     * @return string formated date
     */
    public String getStringDateFormat(Instant date, String format) {
        //log.info("Entered into getStringDateFormat method of DateConversionUtil with passed date: {}, format: {}", date, format);
        String out = null;
        if (date != null) {
            Date dt = Date.from(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            out = simpleDateFormat.format(dt);
        }
        //log.info("Exit from getStringDateFormat method of DateConversionUtil with passed out: {}", out);
        return out;
    }

    /**
     * convert Instant to specified string formate with
     * given userTimeZone
     * @param date
     * @param format
     * @param userTimeZone
     * @return converted date
     */
    public String getStringDateFormat(Instant date, String format, String userTimeZone) {
        //log.info("Entered into getStringDateFormat method of DateConversionUtil with passed date: {}, format: {}, userTimeZone: {}", date, format, userTimeZone);
        String out = null;
        TimeZone timeZone;
        if (date != null) {
            Date dt = Date.from(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            if (userTimeZone != null) {
                timeZone = TimeZone.getTimeZone(userTimeZone);
            } else {
                timeZone = TimeZone.getTimeZone("US/Pacific");
            }
            simpleDateFormat.setTimeZone(timeZone);
            out = simpleDateFormat.format(dt);
        }
        //log.info("Exit from getStringDateFormat method of DateConversionUtil with passed out: {}", out);
        return out;
    }

    /**
     * check if passed date is after the current
     * date
     * @param dateString
     * @param time
     * @param timeZone
     * @param dateFormatToParse
     * @return true if future else false
     */
    public Boolean isDateOfFuture(String dateString, String time, String timeZone, String dateFormatToParse) {
        //log.info("Entered into isDateOfFuture method of DateConversionUtil with passed dateString: {}, time: {}, userTimeZone: {}, dateFormatToParse: {} to validate if the passed date is of future", dateString, time, timeZone, dateFormatToParse);

        Boolean result = false;
        try {
            DateFormat inFormat = new SimpleDateFormat(dateFormatToParse);
            inFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
            String strDate = dateString.concat(" ").concat(time);
            Date date = inFormat.parse(strDate);

            Instant appointmentDateTime = date.toInstant();
            Instant now = Instant.now();
            if (appointmentDateTime.isAfter(now)) {
                result = true;
            }
        } catch (ParseException exc) {
            log.error("ParseException in isDateOfFuture method of DateConversionUtil while parsing the date and time with cause:{} ", exc.getMessage());
        }
        //log.info("Exit from isDateOfFuture method of DateConversionUtil with result: {}", result);
        return result;
    }

    /**
     *  Compare appointment dates to be of same day or future as per the time zone of the site
     * @param dateString
     * @param timeZone
     * @param dateFormatToParse
     * @return
     */
    public Boolean isAppointmentDateTodayOrFuture(String dateString, String timeZone, String dateFormatToParse) {
        Boolean result;
        ZoneId timezoneId = ZoneId.of(timeZone);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(timezoneId);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormatToParse);
        LocalDate zoneApptDate = LocalDate.parse(dateString, dateTimeFormatter);
        result = zonedDateTime.toLocalDate().equals(zoneApptDate) || zoneApptDate.isAfter(zonedDateTime.toLocalDate());
        return result;
    }


    public Instant convertAppointmentTimeToAppointmentInstantTimeForFileImport(Instant apptDate,String apptTime, String zone) {
        //log.info("Entered into convertAppointmentTimeToAppointmentInstantTime method of DateConversionUtil with appointmentDetails: {}, zone: {}", appointmentDetails, zone);
        Instant instantTime = null;

        String dateTimeFormat = "yyyy-MM-dd hh:mm a";
        if (!StringUtils.isEmpty(apptTime) && !StringUtils.isEmpty(apptDate)
            && !((apptTime.toLowerCase()).contains("nan"))) {
            try {
                String appointmentDate = getStringDateFormat(apptDate, "yyyy-MM-dd");
                String dateTimeString = appointmentDate.concat(" ").concat(apptTime);
                instantTime = getInstantDateFormat(dateTimeString, dateTimeFormat, zone);
            } catch (Exception exc) {
                log.error("Exception in convertAppointmentTimeToAppointmentInstantTime method of DateConversionUtil while Converting Appointment date/time into Instant with cause: {}", exc.getMessage());
            }
        } else {
            log.error("Invalid Appointment Date/Time in method convertAppointmentTimeToAppointmentInstantTimeForFileImport, appt time: {}", apptDate + " " + apptTime);
        }
        return instantTime;
    }

    /**
     * Check if newDateTime is After the Existing DateTime
     * @param existingDateTime
     * @param newDateTime
     * @param existingTimeZone
     * @param dateFormatToParse
     * @return true if after else false
     */
    public Boolean isNewDateTimePriorToExistingDateTime(String existingDateTime, String newDateTime, String existingTimeZone, String dateFormatToParse) {
        //log.info("Entered into isNewDateTimePriorToExistingDateTime method of DateConversionUtil to validate if New DateTime After Existing DateTime with existingDateTime: {}, newDateTime: {}, existingTimeZone: {}, dateFormatToParse:{}",
        //  existingDateTime, newDateTime, existingTimeZone, dateFormatToParse);
        Boolean result = false;
        try {
            DateFormat inFormat = new SimpleDateFormat(dateFormatToParse);
            inFormat.setTimeZone(TimeZone.getTimeZone(existingTimeZone));
            Date existingDate = inFormat.parse(existingDateTime);
            Instant existingAppointmentDateTime = existingDate.toInstant();

            Date newDate = inFormat.parse(newDateTime);
            Instant newAppointmentDateTime = newDate.toInstant();
            if (existingAppointmentDateTime.isAfter(newAppointmentDateTime))
                result = true;
        } catch (ParseException var11) {
            this.log.error("ParseException in isNewDateTimePriorToExistingDateTime method of DateConversionUtil while parsing the date and time with cause: {}", var11.getMessage());
        }
        //log.info("Exit from isNewDateTimePriorToExistingDateTime method of DateConversionUtil with result: {}", result);
        return result;
    }

    /**
     * Formatted the Date Object as per Format
     *
     * @param dateTime
     * @param format
     * @return string formated date
     */
    public Instant getInstantDateFormatFromString(String dateTime, String format) {
        //log.info("Entered into getInstantDateFormatFromString method of DateConversionUtil with passed date: {}, format: {}", dateTime, format);
        Instant out = null;
        if (!StringUtils.isEmpty(dateTime)) {
            Date date = null;
            try {
                date = new SimpleDateFormat(format).parse(dateTime);
            } catch (ParseException e) {
                this.log.error("ParseException in getInstantDateFormatFromString method of DateConversionUtil while parsing the date and time with cause: ", e);
            }
            out = date != null ? date.toInstant() : null;
        }
        //log.info("Exit from getInstantDateFormatFromString method of DateConversionUtil with passed out: {}", out);
        return out;
    }

    public Boolean isDateOfTodayorFuture(String dateString, String dateFormatToParse) {
        //log.info("Entered into isDateOfTodayorFuture method of DateConversionUtil with passed dateString: {},  dateFormatToParse: {} to validate if the passed date is of today or future", dateString,  dateFormatToParse);

        Boolean result = false;
        try {
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern(dateFormatToParse));
            if (date.isEqual(LocalDate.now()) || date.isAfter(LocalDate.now())) {
                result = true;
            }
        } catch (Exception exc) {
            log.error("ParseException in isDateOfTodayorFuture method of DateConversionUtil while parsing the date with cause: ", exc.getMessage());
        }
        //log.info("Exit from isDateOfTodayorFuture method of DateConversionUtil with result: {}", result);
        return result;
    }

    public String getDateInUserLanguage(String dateString, String languange) throws ParseException {

        String langFormattedDate = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
            Date date = sdf.parse(dateString);
            Locale locale = new Locale(languange);
            DateFormat dateformat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
            langFormattedDate = dateformat.format(date);
        } catch (ParseException e) {
            this.log.error("ParseException in getDateInUserLanguage method of DateConversionUtil while parsing the date in user language with cause: ", e);
        }
        return langFormattedDate;
    }

    public Instant getDateTimeInZone(Instant instant, String format, String providerTimeZone) {
        try {
            if (providerTimeZone != null) {

                String dateString = getStringTimeFormat(instant, format, providerTimeZone);
                instant = getInstantDateFormatFromString(dateString, format);
            }
        } catch (Exception e) {
            System.out.println("Exception in getDateTimeInZone method of DateConversionUtil: {}" + e);
        }

        return instant;
    }



    /**
     * convert Instant to specified string formate with timezone
     * given userTimeZone
     *
     * @param date
     * @param format
     * @param userTimeZone
     * @return converted date with timezone
     */
    public String getStringDateFormatWithTimeZone(Instant date, String format, String userTimeZone) {
        log.info("Entered into getStringDateFormatWithTimeZone method of DateConversionUtil with passed date: {}, format: {}, userTimeZone: {}", date, format, userTimeZone);
        String out = null;
        TimeZone timeZone;
        if (date != null) {
            Date dt = Date.from(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            if (userTimeZone != null) {
                timeZone = TimeZone.getTimeZone(userTimeZone);
            } else {
                timeZone = TimeZone.getTimeZone("US/Pacific");
            }
            simpleDateFormat.setTimeZone(timeZone);
            out = simpleDateFormat.format(dt) + "(" + userTimeZone + ")";
        }
        log.info("Exit from getStringDateFormatWithTimeZone method of DateConversionUtil with passed out: {}", out);
        return out;
    }

    /**
     * convert Instant to specified string formate with timezone
     * given userTimeZone
     *
     * @param date
     * @param format
     * @param userTimeZone
     * @return converted date with timezone
     */
    public String getDateWithTimeZone(String date, String format, String userTimeZone) {
        log.info("Entered into getStringDateFormatWithTimeZone method of DateConversionUtil with passed date: {}, format: {}, userTimeZone: {}", date, format, userTimeZone);
        String out = null;
        TimeZone timeZone;
        if (date != null) {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat(format);
            try {
                Date inputDate = inputDateFormat.parse(date);
                if (userTimeZone != null) {
                    timeZone = TimeZone.getTimeZone(userTimeZone);
                } else {
                    timeZone = TimeZone.getTimeZone("US/Pacific");
                }
                outputDateFormat.setTimeZone(timeZone);
                out = outputDateFormat.format(inputDate) + "(" + userTimeZone + ")";
            } catch (Exception e) {
                log.error("Exception into getStringDateFormatWithTimeZone method of DateConversionUtil while converting date into required format");
            }
        }
        log.info("Exit from getStringDateFormatWithTimeZone method of DateConversionUtil with passed out: {}", out);
        return out;
    }


    public String getStringDateFormatAndTimeZone(Instant date, String format, String userTimeZone) {
        log.info("Entered into getStringDateFormatWithTimeZone method of DateConversionUtil with passed date: {}, format: {}, userTimeZone: {}", date, format, userTimeZone);
        String out = null;
        TimeZone timeZone;
        if (date != null) {
            Date dt = Date.from(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            if (userTimeZone != null) {
                timeZone = TimeZone.getTimeZone(userTimeZone);
            } else {
                timeZone = TimeZone.getTimeZone("US/Pacific");
            }
            simpleDateFormat.setTimeZone(timeZone);
            out = simpleDateFormat.format(dt) + "(" + userTimeZone + ")";
        }
        log.info("Exit from getStringDateFormatWithTimeZone method of DateConversionUtil with passed out: {}", out);
        return out;
    }
}
