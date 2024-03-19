package io.Odyssey.net.discord;

public class Misc {
	public static String stripIngameFormat(String string) {
		/*string = string.toLowerCase();
		for (int i = 0; i < 31; i++) {
			if (i == 10) {
				System.out.println("Hey, we're  i = 10. Continuing.");
				continue;
			}
			System.out.println("i = "+i);
			
			if (string.contains("<img="+i+">")) {
				string = string.replaceAll("<img="+i+">", "");
			}
		}*/
			
		string = string.replaceAll("@red@", "");
		string = string.replaceAll("@gre@", "");
		string = string.replaceAll("@blu@", "");
		string = string.replaceAll("@yel@", "");
		string = string.replaceAll("@cya@", "");
		string = string.replaceAll("@mag@", "");
		string = string.replaceAll("@whi@", "");
		string = string.replaceAll("@lre@", "");
		string = string.replaceAll("@dre@", "");
		string = string.replaceAll("@bla@", "");
		string = string.replaceAll("@or1@", "");
		string = string.replaceAll("@or2@", "");
		string = string.replaceAll("@or3@", "");
		string = string.replaceAll("@gr1@", "");
		string = string.replaceAll("@gr2@", "");
		string = string.replaceAll("@gr3@", "");
		string = string.replaceAll("@red@", "");
		string = string.replaceAll("@gre@", "");
		string = string.replaceAll("@blu@", "");
		string = string.replaceAll("@yel@", "");
		string = string.replaceAll("@cya@", "");
		string = string.replaceAll("@mag@", "");
		string = string.replaceAll("@whi@", "");
		string = string.replaceAll("@lre@", "");
		string = string.replaceAll("@dre@", "");
		string = string.replaceAll("@bla@", "");
		string = string.replaceAll("@or1@", "");
		string = string.replaceAll("@or2@", "");
		string = string.replaceAll("@or3@", "");
		string = string.replaceAll("@gr1@", "");
		string = string.replaceAll("@gr2@", "");
		string = string.replaceAll("@gr3@", "");
		string = string.replaceAll("@cr1@", "");
		string = string.replaceAll("@cr2@", "");
		string = string.replaceAll("@cr3@", "");
		string = string.replaceAll("@dev@", "");
		string = string.replaceAll("@con@", "");
		string = string.replaceAll("@vet@", "");
		string = string.replaceAll("@mem@", "");
		string = string.replaceAll("@sup@", "");
		string = string.replaceAll("@cr10@", "");
		string = string.replaceAll("@cr18@", "");
		string = string.replaceAll("@cr19@", "");
		string = string.replaceAll("@cr20@", "");
		string = string.replaceAll("@str@","~~");
		string = string.replaceAll("@end@", "~~");
		string = string.replaceAll("<img=10>", ":information_source:");
		string = string.replaceAll("<img=101>", ":star:");
		string = string.replaceAll("<img=7>", ":moneybag:");
		string = string.replaceAll("@sta@", "");
		string = string.replaceAll("@com@", "");
		string = string.replaceAll("@unc@", "");
		string = string.replaceAll("@rar@", "");
		string = string.replaceAll("@epi@", "");
		string = string.replaceAll("@leg@", "");
		/* emojis */
		string = string.replace(":)", ":smiley:");
		string = string.replace(":(", ":frowning2:");
		string = string.replace(":|", ":neutral_face:");
		//string = string.replaceAll(":D", ":smile:");
		//string = string.replace(":d", ":smile:"); 
		//string = string.replace(":c", ":frowning:");
		//string = string.replace(":l", ":neutral_face:");
		//string = string.replace(":S", ":blush:");
		//string = string.replace(":s", ":blush:");
		//string = string.replace(":O", ":open_mouth:");
		//string = string.replace(":o", ":open_mouth:");
		string = string.replace(":0", ":open_mouth:");
		string = string.replace(":$", ":blush:");
		string = string.replace(";)", ":wink:");
		string = string.replace(":/", ":rolling_eyes:"); 
		string = string.replace("(y)", ":thumbsup:"); 
		string = string.replace("(Y)", ":thumbsup:");
		string = string.replace("(n)", ":thumbsdown:");
		string = string.replace("(N)", ":thumbsdown:");
		//string = string.replace(":p", ":stuck_out_tongue:");
		//string = string.replace(":P", ":stuck_out_tongue:");
		string = string.replace("<3", ":heart:");
		string = string.replace("(L)", ":heart_eyes:");
		string = string.replace(":'(", ":cry:");
		string = string.replace("(a)", ":angel:");
		string = string.replace("(A)", ":angel:");
		//string = string.replace("a q p", ":poop:");
		/* bye emojis */
		
		
		while (string.contains("<") && string.contains(">")) {
			int start = string.lastIndexOf("<");
			int end = string.lastIndexOf(">");
			int length = string.length();
			string = string.substring(0, start) + string.substring(end, length);
		}
		
		while (string.contains("<") && !string.contains(">")) {
			string = string.replaceAll("<", "");
		}
		
		while (string.contains(">") && !string.contains("<")) {
			string = string.replace(">", "");
		}
		
		while (string.startsWith(" ")) {
			string = string.substring(1, string.length());
		}
		
		string = string.replaceAll("@everyone", "@everybody");
		
		return string;
	}
}
