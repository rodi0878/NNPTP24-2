package cz.upce.fei.nnptp.zz.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Roman
 */
public record Password(int id, String password, HashMap<String, Parameter> parameters) {


    public Password(int id, String password) {
        this(id, password, new HashMap<>());
    }

    public boolean hasParameter(String title) {
        return parameters.containsKey(title);
    }

    public Parameter getParameter(String title) {
        return parameters.get(title);
    }

    public void addParameter(String title, Parameter parameter) {
        parameters.put(title, parameter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return id == password1.id && Objects.equals(password, password1.password) && Objects.equals(parameters, password1.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, parameters);
    }
}
