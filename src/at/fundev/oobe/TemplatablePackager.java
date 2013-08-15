package at.fundev.oobe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TemplatablePackager extends Task {
	private static final String TITLE_NAME = "%TITLE%";
	
	private static final String AUTHOR_NAME = "%AUTHOR%";
	
	private static final String DATE_NAME = "%DATE%";
	
	private static final String CONTENT_NAME = "%CONTENT%";
	
	private String input;
	
	private String template;
	
	private String outputDir;
	
	private void processFile() throws FileNotFoundException, IOException {
		if(getInput() == null || getTemplate() == null) {
			return;
		}
		
		File inputFile = new File(getInput());
		File templateFile = new File(getTemplate());
		
		if(!inputFile.exists()) {
			throw new FileNotFoundException(String.format("The input file %s doesn't exist.", inputFile.getName()));
		}
		
		if(!templateFile.exists()) {
			throw new FileNotFoundException(String.format("The template file %s doesn't exist.", templateFile.getName()));
		}
		
		Document inputDoc = Jsoup.parse(inputFile, "UTF-8");
		Map<String, String> articleData = new HashMap<String, String>();
		
		Element contentElem = inputDoc.select("body").first();

		Element headerElem = contentElem.select(".maketitle").first();
		Element titleElem = headerElem.select(".titleHead").first();
		
		
		Element authorElem = headerElem.select(".author.cmr-12").first();
		Element dateElem = headerElem.select(".date").first();
		
		headerElem.remove();
		
		addIfNotNull(titleElem, TITLE_NAME, articleData);
		addIfNotNull(authorElem, AUTHOR_NAME, articleData);
		addIfNotNull(dateElem, DATE_NAME, articleData);
		
		fillOutTemplate(contentElem, articleData, inputFile);
	}
	
	private void addIfNotNull(Element value, String key, Map<String, String> parsedItems) {
		if(value == null) {
			return;
		}
		
		parsedItems.put(key, value.text());
	}
	
	private void fillOutTemplate(Element content, Map<String, String> articleData, File inputFile) throws IOException {
		String fileName = inputFile.getName();
		File outputFile = new File(getOutputDir(), fileName);
		
		if(content != null) {
			PrintWriter writer = new PrintWriter(new FileOutputStream(outputFile));
			String contentHtml = getTemplateText();
			
			for(String key : articleData.keySet()) {
				contentHtml = contentHtml.replace(key, articleData.get(key));
			}
			
			contentHtml = contentHtml.replace(CONTENT_NAME, content.html());
			
			writer.write(contentHtml);
			writer.flush();
			writer.close();
			
			System.out.println("Written to output File: " + outputFile);
		} else {
			throw new IOException("No content element found in template file.");
		}
	}
	
	private String getTemplateText() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(getTemplate())));
		StringBuffer buf = new StringBuffer();
		String cur = null;
		
		while((cur = reader.readLine()) != null) {
			buf.append(cur);
		}
		
		reader.close();
		return buf.toString();
	}
	
	public static void main(String[] args) {
		if(args.length < 3) {
			System.err.println("Usage: TemplatablePackager <input> <template> <outputDir>");
			System.exit(1);
		}
		
		String input = args[0];
		String template = args[1];
		String outputDir = args[2];
		
		TemplatablePackager templatePackager = new TemplatablePackager();
		templatePackager.setInput(input);
		templatePackager.setTemplate(template);
		templatePackager.setOutputDir(outputDir);
		
		try {
			templatePackager.processFile();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	@Override
	public void execute() throws BuildException {
		try {
			processFile();
		} catch(IOException e) {
			throw new BuildException(e);
		}
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
}
