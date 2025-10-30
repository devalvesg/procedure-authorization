package br.com.procedureauthorization.models;

import java.time.LocalDateTime;

public class AuthorizationRequest {
    private Integer id;
    private String procedureCode;
    private String patientName;
    private Integer patientAge;
    private String patientGender;
    private String justification;
    private String status;
    private LocalDateTime requestDate;

    public AuthorizationRequest() {
    }

    public AuthorizationRequest(Integer id, String procedureCode, String patientName, Integer patientAge, String patientGender, String justification, String status, LocalDateTime requestDate) {
        this.id = id;
        this.procedureCode = procedureCode;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientGender = patientGender;
        this.justification = justification;
        this.status = status;
        this.requestDate = requestDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Integer getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(Integer patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getJustification() {
        return this.justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    @Override
    public String toString() {
        return "AuthorizationRequest{" +
                "id=" + id +
                ", procedureCode='" + procedureCode + '\'' +
                ", patientName='" + patientName + '\'' +
                ", patientAge=" + patientAge +
                ", patientGender='" + patientGender + '\'' +
                ", justification='" + justification + '\'' +
                ", status='" + status + '\'' +
                ", requestDate=" + requestDate +
                '}';
    }
}
