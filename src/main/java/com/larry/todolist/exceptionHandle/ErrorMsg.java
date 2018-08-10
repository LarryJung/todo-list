package com.larry.todolist.exceptionHandle;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.Path;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
public class ErrorMsg {

    private String field;

    private Object value;

    private String message;

    // 페이지는 없음.
    private String documentation_url = "http://localhost:8080/api/errors";

    public ErrorMsg(Path field, Object value, String message) {
        this.field = field.toString();
        this.value = value;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorMsg errorMsg = (ErrorMsg) o;
        return Objects.equals(field, errorMsg.field) &&
                Objects.equals(value, errorMsg.value) &&
                Objects.equals(message, errorMsg.message) &&
                Objects.equals(documentation_url, errorMsg.documentation_url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(field, value, message, documentation_url);
    }

    @Override
    public String toString() {
        return "ErrorMsg{" +
                "field=" + field +
                ", value=" + value +
                ", message='" + message + '\'' +
                ", documentation_url='" + documentation_url + '\'' +
                '}';
    }

}
