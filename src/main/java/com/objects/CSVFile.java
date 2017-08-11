/*package com.objects;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;


@Entity
public class CSVFile {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long fileid;
	@Column
	private String name;
	@Lob
	@Column
	private byte[] filedata;
	
	public long getFileid() {
		return fileid;
	}
	public void setFileid(long fileid) {
		this.fileid = fileid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getFiledata() {
		return filedata;
	}
	public void setFiledata(byte[] filedata) {
		this.filedata = filedata;
	}
	@Override
	public String toString() {
		return "CSVFile [pid=" + fileid + ", name=" + name + ", filedata=" + Arrays.toString(filedata) + "]";
	}
}*/