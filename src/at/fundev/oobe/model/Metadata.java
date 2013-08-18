package at.fundev.oobe.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Metadata {
	private String path;
	
	private List<PostEntry> entries;

	private Metadata(String path) {
		this.path = path;
		this.entries = new ArrayList<>();
	}
	
	public List<PostEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<PostEntry> entries) {
		if(entries != null) {
			this.entries = entries;			
		} else {
			this.entries.clear();
		}
	}
	
	public void persist() throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter writer = new FileWriter(new File(path));
		writer.append(gson.toJson(this)).flush();
		writer.close();
	}
	
	public static Metadata fromFile(String path) {
		try {
			Metadata data = new Metadata(path);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Metadata file = gson.fromJson(new FileReader(path), Metadata.class);
			
			if(file == null) {
				return data;
			}
			
			data.setEntries(file.getEntries());
			return data;
		} catch (FileNotFoundException e) {
			return new Metadata(path);
		}
	};
}
