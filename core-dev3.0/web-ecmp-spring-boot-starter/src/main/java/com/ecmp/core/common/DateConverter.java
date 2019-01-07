package com.ecmp.core.common;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 通用日期转换器
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2017/9/12 17:02
 */
public class DateConverter extends PropertyEditorSupport implements Converter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateConverter.class);

    private static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private static final String ISO_SHORT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private static final String DATETIME_PATTERN_NO_SECOND = "yyyy-MM-dd HH:mm";

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private static final String YEAR_MONTH_PATTERN = "yyyy-MM";

    private SimpleDateFormat datetimeFormat = new SimpleDateFormat(DATETIME_PATTERN);
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);

    public Object convert(Class type, Object value) {
        Object result = null;
        if (type == Date.class) {
            try {
                result = doConvertToDate(value);
            } catch (ParseException e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else if (type == java.sql.Date.class) {
            try {
                result = new java.sql.Date(doConvertToDate(value).getTime());
            } catch (ParseException e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else if (type == java.sql.Timestamp.class) {
            try {
                result = new java.sql.Timestamp(doConvertToDate(value).getTime());
            } catch (ParseException e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else if (type == String.class) {
            result = doConvertToString(value);
        }
        return result;
    }

    /**
     * Convert String to Date
     *
     * @throws ParseException
     */
    private Date doConvertToDate(Object value) throws ParseException {
        Date result = null;

        if (value instanceof String) {
            try {
                result = DateUtils.parseDate((String) value, ISO_DATE_TIME_FORMAT, ISO_SHORT_DATE_TIME_FORMAT,
                        DATETIME_PATTERN, DATETIME_PATTERN_NO_SECOND, DATE_PATTERN, YEAR_MONTH_PATTERN);
            } catch (ParseException e) {
                // all patterns failed, try a milliseconds constructor
                if (StringUtils.isNumeric((String) value)) {
                    try {
                        result = new Date(new Long((String) value));
                    } catch (Exception e1) {
                        LOGGER.error("Converting Date failed!");
                    }
                }
            }
        } else if (value instanceof Object[]) {
            // let's try to convert the first element only
            Object[] array = (Object[]) value;

            if (array.length >= 1) {
                value = array[0];
                result = doConvertToDate(value);
            }

        } else if (Date.class.isAssignableFrom(value.getClass())) {
            result = (Date) value;
        }
        return result;
    }

    /**
     * Convert Date to String
     */
    private String doConvertToString(Object value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN);
        String result = null;
        if (value instanceof Date) {
            result = simpleDateFormat.format(value);
        }
        return result;
    }


    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.isNotBlank(text)) {
            try {
                if (!text.contains(":") && text.length() == 10) {
                    setValue(this.dateFormat.parse(text));
                } else if (text.indexOf(":") > 0 && text.length() == 19) {
                    setValue(this.datetimeFormat.parse(text));
                } else if (text.indexOf(":") > 0 && text.length() == 21) {
                    text = text.replace(".0", "");
                    setValue(this.datetimeFormat.parse(text));
                } else {
                    throw new IllegalArgumentException("Could not parse date, date format is error ");
                }
            } catch (ParseException ex) {
                IllegalArgumentException iae = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
                iae.initCause(ex);
                throw iae;
            }
        } else {
            setValue(null);
        }
    }
}
