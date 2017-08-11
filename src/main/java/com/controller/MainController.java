package com.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.objects.*;

@Controller
public class MainController {

	// webapp save directory
	// private static String UPLOADED_FOLDER =
	// "/usr/apache/apache-tomcat-8.5.14/uploads/";

	// local save directory
	// private static String UPLOADED_FOLDER =
	// "C:\\Users\\iribarneg\\Documents\\workspace-sts\\addressqualityvalidation\\src\\main\\resources\\uploads\\";
	// private static String CSV_FILE;
	/*
	 * @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public String singleFileUpload(@RequestParam("file")
	 * MultipartFile file, RedirectAttributes redirectAttributes) { List<Entry>
	 * response; try {
	 * 
	 * // Get the file and save it somewhere byte[] bytes = file.getBytes(); Path
	 * path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
	 * Files.write(path, bytes); CSV_FILE = UPLOADED_FOLDER +
	 * file.getOriginalFilename();
	 * 
	 * } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * response = parser(CSV_FILE);
	 * 
	 * Gson gson = new Gson(); String json = gson.toJson(response);
	 * 
	 * return json; }
	 */

	public List<Entry> parser(String filename) {

		String csvFile = filename;
		String line = "";
		String csvSplitBy = ",";
		List<Entry> results = new ArrayList<Entry>();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			int lineCount = 0;
			while ((line = br.readLine()) != null) {

				if (lineCount == 0) {
					lineCount++; // skip csv headers
				} else {
					// use comma as separator
					String[] row = line.split(csvSplitBy);

					// manage entries with pojo otherwise you lose order when sent to FE
					Entry entry = new Entry();
					entry.setFname(row[0]);
					entry.setLname(row[1]);
					entry.setCompany(row[2]);
					entry.setStreetAddress(row[3]);
					entry.setCity(row[4]);
					entry.setState(row[5]);
					entry.setZipCode(row[6]);

					results.add(entry);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	@ResponseBody
	public String blobFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		List<Entry> response;
		byte[] bytes = null;
		try {

			// Get the file and save it somewhere
			bytes = file.getBytes();

		} catch (IOException e) {
			e.printStackTrace();
		}

		response = blobParser(bytes);

		Gson gson = new Gson();
		String json = gson.toJson(response);

		return json;
	}

	public List<Entry> blobParser(byte[] filedata) {

		String line = "";
		String csvSplitBy = ",";
		List<Entry> results = new ArrayList<Entry>();

		InputStream is = null;
		BufferedReader br = null;
		try {
			is = new ByteArrayInputStream(filedata);
			br = new BufferedReader(new InputStreamReader(is));

			int lineCount = 0;
			while ((line = br.readLine()) != null) {

				if (lineCount == 0) {
					lineCount++; // skip csv headers
				} else {
					// use comma as separator
					String[] row = line.split(csvSplitBy);

					// manage entries with pojo otherwise you lose order when sent to FE
					Entry entry = new Entry();
					entry.setFname(row[0]);
					entry.setLname(row[1]);
					entry.setCompany(row[2]);
					entry.setStreetAddress(row[3]);
					entry.setCity(row[4]);
					entry.setState(row[5]);
					entry.setZipCode(row[6]);

					results.add(entry);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return results;
	}
}