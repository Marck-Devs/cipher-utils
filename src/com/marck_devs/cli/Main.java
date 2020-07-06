package com.marck_devs.cli;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.function.Consumer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.marck_devs.engine.CipherUtils;
import com.marck_devs.engine.Generator;

public class Main {
	public static final String VERSION = "1.0.0";
	public static OutputStream out;

	private static void showVersion() {
		try {
			out.write(VERSION.getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void showHelp(Option[] opt) {
		String format = "%7s\t%-16s\t%s\n";
		StringBuilder msg = new StringBuilder();
		msg.append("Version:" + VERSION + "\n\n");
		msg.append("cipher-utils [OPTIONS]\n\n");
		for (Option op : opt) {
			msg.append(
					String.format(format, "-" + (char) op.getId() + ",", "--" + op.getLongOpt(), op.getDescription()));
		}
		System.out.println(msg.toString());

	}

	private static void showHelp(Collection<Option> opt) {
		String format = "%7s\t%-16s\t%s\n";
		StringBuilder msg = new StringBuilder();
		msg.append("Version: " + VERSION + "\n\n");
		msg.append("cipher-utils [OPTIONS]\n\n");
		for (Option op : opt) {
			msg.append(
					String.format(format, "-" + (char) op.getId() + ",", "--" + op.getLongOpt(), op.getDescription()));
		}
		System.out.println(msg.toString());

	}

	private static void genHelp() {
		StringBuilder msg = new StringBuilder("\n\ncipher-utils -p <length> -v <vectorNumber> [-s|b]\n");
		msg.append("Generate random password.\n");
		System.out.println(msg.toString());
	}

	private static void cipherHelp() {
		StringBuilder msg = new StringBuilder("\n\ncipher-utils -c [c|d] -p <password> -o <outFile> -f <inputFile>\n");
		msg.append("Cipher or decipher any file that set to input.\n");
		System.out.println(msg.toString());
	}

	public static void main(String[] args) {
		Options opt = new Options();
		opt.addOption("p", "gen-pass", true, "Generate a random password with the <lenght>");
		opt.addOption("c", "cipher", true, "Cipher otions, [d|c] => decipher | cipher the file");
		opt.addOption("f", "file", true, "File to cipher");
		opt.addOption("o", "out-file", true, "The file to save the output");
		opt.addOption("s", "special-chars", false,
				"Add special characters to the pass generator");
		opt.addOption("i", "span-vector", true, "Number that represent the kernel of the password");
		opt.addOption("b", "simbol", false, "Enable to use the simbols in password generator");
		opt.addOption("h", "help", false, "Show this help");
		opt.addOption("m", "manual", true, "Show specific help for command [cipher|pass-gen]");
		opt.addOption("v", "version", false, "Show version");

		try {
			CommandLine cmd = new DefaultParser().parse(opt, args);
			if (cmd.hasOption("o")) {
				out = new BufferedOutputStream(new FileOutputStream(new File(cmd.getOptionValue("o"))));
			} else {
				out = System.out;
			}
			if (cmd.hasOption("version")) {
				showVersion();
				return;
			}
			// cipher
			if (cmd.hasOption("c")) {
				if (!cmd.hasOption("f") || !cmd.hasOption("o") || !cmd.hasOption("p")) {
					cipherHelp();
					return;
				}
				String userOpt = cmd.getOptionValue("c");
				String inFile = cmd.getOptionValue("f");
				String outFile = cmd.getOptionValue("o");
				String pass = cmd.getOptionValue("p");
				// cipher
				if (userOpt.equals("c")) {
					byte[] d = CipherUtils.cipherFile(new File(inFile), CipherUtils.genAESKey(pass));
					FileOutputStream os = new FileOutputStream(outFile);
					os.write(d);
					os.close();
					return;
				}

				if (userOpt.equals("d")) {
					byte[] d = CipherUtils.decipherFile(new File(inFile), CipherUtils.genAESKey(pass));
					FileOutputStream os = new FileOutputStream(outFile);
					os.write(d);
					os.close();
					return;
				}
				return;
			}

			// pass generator
			if (cmd.hasOption("p")) {
				Generator.Builder builder = Generator.builder();
				builder.setLength(Integer.parseInt(cmd.getOptionValue("p")));
				builder.addPool(Generator.NUMBER_POOL);
				builder.addPool(Generator.MAIN_POOL);
				// check the pools
				if (cmd.hasOption("s"))
					builder.addPool(Generator.SPECIAL_CHAR_POOL);
				if (cmd.hasOption("b"))
					builder.addPool(Generator.SIMBOLS_POOL);
				if (cmd.hasOption("i"))
					builder.setVector(Long.parseLong(cmd.getOptionValue("i")));
				String pass = builder.build().gen();
				System.out.println(pass);
				return;
			}

			if (cmd.hasOption("h")) {
				showHelp(opt.getOptions());
				return;
			}

			if (cmd.hasOption("m")) {
				if (cmd.getOptionValue("m").equals("cipher"))
					cipherHelp();
				else if (cmd.getOptionValue("m").equals("pass-gen"))
					genHelp();

				showHelp(opt.getOptions());
				return;
			}

		} catch (Exception e) {
			if (e.getMessage() != null)
				System.out.println(e.getMessage());
			showHelp(opt.getOptions());
		}

	}

}
