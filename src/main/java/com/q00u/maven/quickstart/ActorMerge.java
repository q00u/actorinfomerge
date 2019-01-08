package com.q00u.maven.quickstart;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.flipkart.zjsonpatch.JsonDiff;
import com.flipkart.zjsonpatch.JsonPatch;
import com.google.common.io.Resources;


public class ActorMerge 
{
	public class Settings {
		String original;
		String[] mods;
		String outputPath;
	}
	
	static public void diffCall() {
		ObjectMapper mapper = new ObjectMapper();

		Path settingsPath = Paths.get("./settings.json");
//		Path settingsPath = null;
//		try {
//			settingsPath = new File(Resources.getResource("./settings.json").toURI()).toPath();
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String jsonSettings = null;
		try {
			jsonSettings = new String(Files.readAllBytes(settingsPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonNode settings = null;
		try {
			settings = mapper.readTree(jsonSettings);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("SETTINGS:");
		System.out.println("Original: " + settings.get("original"));
		System.out.println("Mods:");
		JsonNode mods = settings.get("mods");
		for (int i = 1; i<=mods.size(); i++)
			System.out.println(" " + i + ": " + mods.get(Integer.toString(i)));
//		for (JsonNode m : settings.get("mods")) {
//			System.out.println("  " + m);
//		}
		System.out.println("Output Path: " + settings.get("output"));

		//Load original
		System.out.println("Loading original");
		Path originalPath = Paths.get(settings.get("original").asText());
		String jsonOriginal = null;
		try {
			jsonOriginal = new String(Files.readAllBytes(originalPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonNode original = null;
		try {
			original = mapper.readTree(jsonOriginal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonNode merged = original;
		
		//For each mod:
		
		for (int i = mods.size(); i>=1; i--) {
			System.out.println("Loading mod #"+i);
			Path modPath = Paths.get(mods.get(Integer.toString(i)).asText());
			String jsonMod = null;
			try {
				jsonMod = new String(Files.readAllBytes(modPath));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JsonNode mod = null;
			try {
				mod = mapper.readTree(jsonMod);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Scanning mod #"+i);
			JsonNode patch = JsonDiff.asJson(original, mod);
			//System.out.println("Changes: " + patch);
			System.out.println("Merging mod #"+i);
			merged = JsonPatch.apply(patch, merged);
		}
		
		//TESTING
//		System.out.println("TESTING");
//		Path targetPath = Paths.get("C:\\dev\\wiiumod\\warmer_doublet\\warmerdoubletlevel20dlcepona.sbyml.json");
//		String jsonTarget = null;
//		try {
//			jsonTarget = new String(Files.readAllBytes(targetPath));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		JsonNode target = null;
//		try {
//			target = mapper.readTree(jsonTarget);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		JsonNode test = JsonDiff.asJson(merged, target);
//		System.out.println("Results: " + test.toString());
		
		System.out.println("Writing merged file " + settings.get("output"));
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		try {
			writer.writeValue(new File(settings.get("output").asText()), merged);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done!");
	}
	
    public static void main( String[] args )
    {
		diffCall();
    }
}
