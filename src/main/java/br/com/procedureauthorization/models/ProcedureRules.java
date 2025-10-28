package br.com.procedureauthorization.models;

public class ProcedureRules {
    private Integer id;
    private String code;
    private Integer age;
    private String gender;
    private Boolean isAuthorized;

    public ProcedureRules() {
    }

    public ProcedureRules(String code, Integer age, String gender, Boolean isAuthorized) {
        this.code = code;
        this.age = age;
        this.gender = gender;
        this.isAuthorized = isAuthorized;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getIsAuthorized() {
        return isAuthorized;
    }

    public void setIsAuthorized(Boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    @Override
    public String toString() {
        return "Procedure {" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", isAuthorized=" + isAuthorized +
                '}';
    }
}
