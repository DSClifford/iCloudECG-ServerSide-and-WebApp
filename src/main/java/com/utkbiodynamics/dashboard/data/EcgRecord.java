package com.utkbiodynamics.dashboard.data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;



public class EcgRecord implements Serializable {
    private String id;
    private String userdefinedname = "";
    private String diagnosis = "";
    private String patientGender = ""; //
    private String patientName = ""; //
    private int patientAge;
    private String str_patientAge = ""; //
    private String source = "";
    private double samplingRate;
    private String str_samplingRate = "";
    private String relativeFilePath;
    private String qualityGrade;
    private String suspectedMisplacement;
    private String upldDate;
    private String record_notes = ""; //
    private List<Double> [] ecgData ;
    public String getId() {
        return id;
    }
    public void setId(String string) {
        this.id = string;
    }
    public String getDiagnosis() {
        return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public String getPatientGender() {
        return patientGender;
    }
    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public int getPatientAge() {
        return patientAge;
    }
    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }
    public String getRelativeFilePath() {
        return relativeFilePath;
    }
    public void setRelativeFilePath(String relativeFilePath) {
        this.relativeFilePath = relativeFilePath;
    }
    public double getSamplingRate() {
        return samplingRate;
    }
    public void setSamplingRate(double samplingRate) {
        this.samplingRate = samplingRate;
    }
    public List<Double> [] getEcgData() {
        return ecgData;
    }
    public void setEcgData(List<Double> [] ecgData) {
        this.ecgData = ecgData;
    }
    public String getQualityGrade() {
        return qualityGrade;
    }
    public void setQualityGrade(String qualityGrade) {
        this.qualityGrade = qualityGrade;
    }
    public String getSuspectedMisplacement() {
        return suspectedMisplacement;
    }
    public void setSuspectedMisplacement(String suspectedMisplacement) {
        this.suspectedMisplacement = suspectedMisplacement;
    }
    public String getStr_samplingRate() {
        return str_samplingRate;
    }
    public void setStr_samplingRate(String str_samplingRate) {
        this.str_samplingRate = str_samplingRate;
    }
    public String getStr_patientAge() {
        return str_patientAge;
    }
    public void setStr_patientAge(String str_patientAge) {
        this.str_patientAge = str_patientAge;
    }
    public String getUserdefinedname() {
        return userdefinedname;
    }
    public void setUserdefinedname(String userdefinedname) {
        this.userdefinedname = userdefinedname;
    }
	public String getupDate() {
		// TODO Auto-generated method stub
		return upldDate;
	}
	public void setupDate(Timestamp tstamp) {
		// TODO Auto-generated method stub
        String upDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(tstamp);
		this.upldDate = upDate;
	}
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
	public String getRecord_notes() {
		
		return record_notes;
	}

	public void setRecord_notes(String record_notes) {
		this.record_notes = record_notes;
	}
}
