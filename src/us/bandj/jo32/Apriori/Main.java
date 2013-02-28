package us.bandj.jo32.Apriori;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import us.bandj.jo32.Apriori.Exceptions.InvalidRuleException;
import us.bandj.jo32.Apriori.Exceptions.NoRequiredItemSetException;
import us.bandj.jo32.Apriori.Exceptions.OutOfRangeException;

public class Main {

	// the main function of this app, enter the absolution path of the asso file
	// to run this app
	public static void main(String[] args) throws IOException,
			OutOfRangeException, InvalidRuleException,
			NoRequiredItemSetException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Please input the file path (absolute path):");
		String path = br.readLine();
		System.out
				.println("--- MinSupport is set to 0.1, and MinConfidence is set to 0.9 ---\n--- start proccessing ---");
		String filePath = path;
		Apriori apriori = new Apriori(filePath, 0.1, 0.9);
		apriori.load();
		for (Rule rule : apriori.getRules()) {
			String line = "";
			line += rule.toString() + " : ";
			line += "Support: " + rule.getWholeItemSet().getSupport() + " ; ";
			line += "Confidence: " + rule.getWholeItemSet().getSupport()
					/ rule.getLeftItemSet().getSupport() + " ;";
			System.out.println(line);
		}
		System.out.println("--- end proccessing ---");
	}
}
