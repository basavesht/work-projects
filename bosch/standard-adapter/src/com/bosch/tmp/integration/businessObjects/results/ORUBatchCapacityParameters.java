package com.bosch.tmp.integration.businessObjects.results;

public class ORUBatchCapacityParameters
{
	Integer resultsBatchCurrentCapacity = 0;
	Integer resultsBatchMaxCapacity = 0;
	Integer notesMaxCapacity = 0;
	Integer notesCurrentCapacity = 0;

	public Integer getResultsBatchCurrentCapacity() {
		return resultsBatchCurrentCapacity;
	}
	public void setResultsBatchCurrentCapacity(Integer resultsBatchCurrentCapacity) {
		this.resultsBatchCurrentCapacity = resultsBatchCurrentCapacity;
	}
	public Integer getResultsBatchMaxCapacity() {
		return resultsBatchMaxCapacity;
	}
	public void setResultsBatchMaxCapacity(Integer resultsBatchMaxCapacity) {
		this.resultsBatchMaxCapacity = resultsBatchMaxCapacity;
	}
	public Integer getNotesMaxCapacity() {
		return notesMaxCapacity;
	}
	public void setNotesMaxCapacity(Integer notesMaxCapacity) {
		this.notesMaxCapacity = notesMaxCapacity;
	}
	public Integer getNotesCurrentCapacity() {
		return notesCurrentCapacity;
	}
	public void setNotesCurrentCapacity(Integer notesCurrentCapacity) {
		this.notesCurrentCapacity = notesCurrentCapacity;
	}
}
