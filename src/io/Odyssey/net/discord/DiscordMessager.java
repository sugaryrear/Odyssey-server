package io.Odyssey.net.discord;

import java.awt.Color;
import java.net.URI;

import org.json.JSONObject;

import io.Odyssey.net.discord.Misc;

import io.Odyssey.net.discord.DiscordMessage;
import io.Odyssey.net.discord.WebhookClient;
import io.Odyssey.net.discord.WebhookClientBuilder;
import io.Odyssey.net.discord.DiscordEmbed;


public class DiscordMessager extends JSONObject { 

	public static boolean active = true;

	private static String announcementhook = "https://discord.com/api/webhooks/1149125651350167765/7pMQm-Ax7VC70K-950WMznLaZxXW9LlzdL0WVQNuYMILg9a4JlvFNFbRZUwfKgxN7lOe";
	private static String newplayers = "https://discord.com/api/webhooks/942760940573712384/-i8D0u1HB0UCOSu8VvPvsZFBXzXkeenF0rcUXFprdH7PE_ZpefIw6cAq4SfgsWf_kP2-";
	private static String lootations = "https://discord.com/api/webhooks/1149123545478205480/q9eGWwjrxPTM39ApZNTdVEzTchfg01irg7IlNKCQjEF6UZbXFKCGLXuSLt9ErR2hwlC9";
	private static String tradelogs = "https://discord.com/api/webhooks/889713268422213723/HVHnYVzlKOfBsQl4nutfVA0KzLLDmmt76MGIREFBhpkeeFQc1VTNBgeTzTYfWpqAkUDg";
	private static String vote = "https://discord.com/api/webhooks/942762745206538261/KQKN2Rsaz7QKITSwAWERDlZv3zUWHRDkzBozMp4Jx4Wi4cNitJVNDGn9f8w4c_ipBcO5";
	private static String donate = "https://discord.com/api/webhooks/942761812858904616/wK_-cYJImDVVGHP51_2XYfF7EZXx_8nVliN-omqs6GirGh0tmiF2IxpU1HTZx4Mlk_4D";
	private static String logs = "https://discord.com/api/webhooks/942768195364864050/JQS9q5Vs96uvwILgCLdEQJgeLsclaqH1W_7RdTBC7TKNazhP552FPWt5lQtS2m67kLGD";

	public static void sendLogs(String msg){

		try {

			String webhook = logs;

			WebhookClient client = new WebhookClientBuilder()
					.withURI(new URI(webhook))
					.build(); // Create the webhook client

			new DiscordEmbed.Builder()
					.withTitle("Odyssey") // The title of the embed element
					.withURL("http://Odyssey.com") // The URL of the embed element
					.withColor(Color.GREEN) // The color of the embed. You can leave this at null for no color
					.withDescription("Remember, you can mute any specific channel by clicking the bell in the top right of Discord.") // The description of the embed object
					.build();

			DiscordMessage message = new DiscordMessage.Builder(Misc.stripIngameFormat(msg)) // The content of the message
					// .withEmbed("") // Add our embed object
					.withUsername("LIVE FEED") // Override the username of the bot
					.build(); // Build the message

			//.withTitle("Odyssey") // The title of the embed element

			client.sendPayload(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sendVote(String msg){

		try {

			String webhook = vote;

			WebhookClient client = new WebhookClientBuilder()
					.withURI(new URI(webhook))
					.build(); // Create the webhook client

			new DiscordEmbed.Builder()
					.withTitle("Odyssey") // The title of the embed element
					.withURL("http://Odyssey.com") // The URL of the embed element
					.withColor(Color.GREEN) // The color of the embed. You can leave this at null for no color
					.withDescription("Remember, you can mute any specific channel by clicking the bell in the top right of Discord.") // The description of the embed object
					.build();

			DiscordMessage message = new DiscordMessage.Builder(Misc.stripIngameFormat(msg)) // The content of the message
					// .withEmbed("") // Add our embed object
					.withUsername("LIVE FEED") // Override the username of the bot
					.build(); // Build the message

			//.withTitle("Odyssey") // The title of the embed element

			client.sendPayload(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sendDonate(String msg){

		try {

			String webhook = donate;

			WebhookClient client = new WebhookClientBuilder()
					.withURI(new URI(webhook))
					.build(); // Create the webhook client

			new DiscordEmbed.Builder()
					.withTitle("Odyssey") // The title of the embed element
					.withURL("http://Odyssey.com") // The URL of the embed element
					.withColor(Color.GREEN) // The color of the embed. You can leave this at null for no color
					.withDescription("Remember, you can mute any specific channel by clicking the bell in the top right of Discord.") // The description of the embed object
					.build();

			DiscordMessage message = new DiscordMessage.Builder(Misc.stripIngameFormat(msg)) // The content of the message
					// .withEmbed("") // Add our embed object
					.withUsername("LIVE FEED") // Override the username of the bot
					.build(); // Build the message

			//.withTitle("Odyssey") // The title of the embed element

			client.sendPayload(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendBoxsAnnouncement(String msg) {
		try {

			String webhook = vote;

			WebhookClient client = new WebhookClientBuilder()
					.withURI(new URI(webhook))
					.build(); // Create the webhook client

			new DiscordEmbed.Builder()
					.withTitle("Odyssey") // The title of the embed element
					.withURL("http://Odyssey.com") // The URL of the embed element
					.withColor(Color.GREEN) // The color of the embed. You can leave this at null for no color
					.withDescription("Remember, you can mute any specific channel by clicking the bell in the top right of Discord.") // The description of the embed object
					.build();

			DiscordMessage message = new DiscordMessage.Builder(Misc.stripIngameFormat(msg)) // The content of the message
					// .withEmbed("") // Add our embed object
					.withUsername("LIVE FEED") // Override the username of the bot
					.build(); // Build the message

			//.withTitle("Odyssey") // The title of the embed element

			client.sendPayload(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public static void sendTradeLogs(String msg) {
		try {

			String webhook = tradelogs;

			WebhookClient client = new WebhookClientBuilder()
					.withURI(new URI(webhook))
					.build(); // Create the webhook client

			new DiscordEmbed.Builder()
					.withTitle("Odyssey") // The title of the embed element
					.withURL("http://Odyssey.com") // The URL of the embed element
					.withColor(Color.GREEN) // The color of the embed. You can leave this at null for no color
					.withDescription("Remember, you can mute any specific channel by clicking the bell in the top right of Discord.") // The description of the embed object
					.build();

			DiscordMessage message = new DiscordMessage.Builder(Misc.stripIngameFormat(msg)) // The content of the message
					// .withEmbed("") // Add our embed object
					.withUsername("LIVE FEED") // Override the username of the bot
					.build(); // Build the message

			//.withTitle("Odyssey") // The title of the embed element

			client.sendPayload(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendAnnouncement(String msg) {
		try {
			
			String webhook = announcementhook;
			
			WebhookClient client = new WebhookClientBuilder()
				    .withURI(new URI(webhook))
				    .build(); // Create the webhook client
			
			new DiscordEmbed.Builder()
				    .withTitle("Odyssey") // The title of the embed element
				    .withURL("http://Odyssey.com") // The URL of the embed element
				    .withColor(Color.GREEN) // The color of the embed. You can leave this at null for no color
				    .withDescription("Remember, you can mute any specific channel by clicking the bell in the top right of Discord.") // The description of the embed object
				    .build();
			
			DiscordMessage message = new DiscordMessage.Builder(Misc.stripIngameFormat(msg)) // The content of the message
				   // .withEmbed("") // Add our embed object
				    .withUsername("LIVE FEED") // Override the username of the bot
				    .build(); // Build the message
			
		    //.withTitle("Odyssey") // The title of the embed element
	
			client.sendPayload(message);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void sendLootations(String msg) {
		try {
			
			String webhook = lootations;
			
			WebhookClient client = new WebhookClientBuilder()
				    .withURI(new URI(webhook))
				    .build(); // Create the webhook client
			
			new DiscordEmbed.Builder()
		    .withTitle("Odyssey") // The title of the embed element
		    .withURL("http://Odyssey.com") // The URL of the embed element
		    .withColor(Color.GREEN) // The color of the embed. You can leave this at null for no color
		    .withDescription("Remember, you can mute any specific channel by clicking the bell in the top right of Discord.") // The description of the embed object
		     .build();
			
			DiscordMessage message = new DiscordMessage.Builder(Misc.stripIngameFormat(msg)) // The content of the message
				    //.withEmbed(embed) // Add our embed object
				    .withUsername("LIVE FEED") // Override the username of the bot
				    .build(); // Build the message
			
			client.sendPayload(message);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sendNewPlayers(String msg) {
		try {
			
			String webhook = newplayers;
			
			WebhookClient client = new WebhookClientBuilder()
				    .withURI(new URI(webhook))
				    .build(); // Create the webhook client
			
			new DiscordEmbed.Builder()
		    .withTitle("Odyssey") // The title of the embed element
		    .withURL("http://Odyssey.com") // The URL of the embed element
		    .withColor(Color.GREEN) // The color of the embed. You can leave this at null for no color
		    .withDescription("Remember, you can mute any specific channel by clicking the bell in the top right of Discord.") // The description of the embed object
		    .build();
			
			DiscordMessage message = new DiscordMessage.Builder(Misc.stripIngameFormat(msg)) // The content of the message
				    //.withEmbed(embed) // Add our embed object
				    .withUsername("LIVE FEED") // Override the username of the bot
				    .build(); // Build the message
			
			client.sendPayload(message);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	



}
