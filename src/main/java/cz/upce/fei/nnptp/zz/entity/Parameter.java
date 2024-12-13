package cz.upce.fei.nnptp.zz.entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 *
 * @author Roman
 */
public abstract class Parameter {

    public static class StandardizedParameters {
        public static final String TITLE = "title";
        public static final String EXPIRATION_DATETIME = "expiration-datetime";
        public static final String WEBSITE = "website";
        public static final String DESCRIPTION = "description";
    }

    public abstract String toJson();

    public abstract Object getValue();
    
    // TODO: add support for validation rules

    public static class TextParameter extends Parameter {
        private String value;

        public TextParameter(String value) {
            this.value = value;
        }

        public TextParameter() {
        }

        @Override
        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toJson() {
            return "\"" + value + "\"";
        }
    }

    public static class DateTimeParameter extends Parameter {
        private LocalDateTime value;

        public DateTimeParameter(LocalDateTime value) {
            this.value = value;
        }

        public DateTimeParameter() {
        }

        @Override
        public LocalDateTime getValue() {
            return value;
        }

        public void setValue(LocalDateTime value) {
            this.value = value;
        }

        @Override
        public String toJson() {
            return "\"" + (value != null ? value.toString() : "null") + "\"";
        }
    }

    public static class PasswordParameter extends Parameter {
        private String password;

        public PasswordParameter(String password) {
            this.password = password;
        }

        public PasswordParameter() {
        }

        @Override
        public String getValue() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toJson() {
            return "\"" + password + "\"";
        }
    }
}

