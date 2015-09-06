package com.bosch.tmp.integration.businessObjects.results;

import com.bosch.th.patientrecord.Device;
import com.bosch.th.patientrecord.Mode;
import com.bosch.th.patientrecord.VitalSignValue;

public class ClinicalObservation {
	
	private Mode collectionMode = null;
	private Device device = null;
	private VitalSignValue vitalSignValue = null;
	private String observationId = null;
	private String clinicalSessionRawXML = null;
	
	public String getClinicalSessionRawXML() {
		return clinicalSessionRawXML;
	}
	public void setClinicalSessionRawXML(String clinicalSessionRawXML) {
		this.clinicalSessionRawXML = clinicalSessionRawXML;
	}
	public Mode getCollectionMode() {
		return collectionMode;
	}
	public void setCollectionMode(Mode collectionMode) {
		this.collectionMode = collectionMode;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public VitalSignValue getVitalSignValue() {
		return vitalSignValue;
	}
	public void setVitalSignValue(VitalSignValue vitalSignValue) {
		this.vitalSignValue = vitalSignValue;
	}
	public String getObservationId() {
		return observationId;
	}
	public void setObservationId(String observationId) {
		this.observationId = observationId;
	}
}
