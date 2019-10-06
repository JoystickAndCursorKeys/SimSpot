package com.lodgia.genesys.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import com.lodgia.genesys.lib.Logger;

public class JarUtil {
	public static int BUFFER_SIZE = 10240;

	public static File[] listDirectory(String path, Logger l) {

		File[] listAll;
		int count;
		Integer listPointer;

		count = countFilesInDirectory(path);
		listAll = new File[count];
		listPointer = new Integer(0);

		listPointer = listDirectory(path, listAll, listPointer, l);

		return listAll;
	}

	private static int listDirectory(String path, File[] ListAll,
			Integer ListAllPtr, Logger l) {

		File root = new File(path);
		File[] list = root.listFiles();

		if (list == null)
			return ListAllPtr;

		for (File f : list) {
			if (f.isDirectory()) {
				l.debug("Dir:" + f.getPath());
				ListAllPtr = listDirectory(f.getPath(), ListAll, ListAllPtr, l );

			} else {
				l.debug("File:("+ListAllPtr+")" + f.getPath());
				ListAll[ListAllPtr] = f;
				ListAllPtr++;

			}
		}

		return ListAllPtr;
	}

	private static int countFilesInDirectory(String path) {

		File root = new File(path);
		File[] list = root.listFiles();

		int count = 0;

		if (list == null)
			return count;

		for (File f : list) {
			if (f.isDirectory()) {
				count += countFilesInDirectory(f.getPath());
			} else {
				count++;
			}
		}
		return count;
	}

	public static void create(String archiveFile, String directory, Logger l) {
		create(new File(archiveFile), new File(directory), false, l);
	}
	
	public static void create(File archiveFile, File directory, Logger l) {
		create(archiveFile, directory, false, l);
	}

	public static void create(File archiveFile, File directory,
			boolean startInside, Logger l) {
		File files[];
		files = listDirectory(directory.getPath(), l);

		if( startInside ) {
			create(archiveFile, files, directory.getPath().length() + 1, l);
		}
		else {
			create(archiveFile, files, 0, l);
		}
	}

	public static void create(File archiveFile, File[] filesToAdd, Logger l) {
		create( archiveFile, filesToAdd, 0, l);
	}
	
	private static void create(File archiveFile, File[] filesToAdd, int startPathAt, Logger l) {
		try {
			byte buffer[] = new byte[BUFFER_SIZE];
			// Open archive file
			FileOutputStream stream = new FileOutputStream(archiveFile);
			JarOutputStream out = new JarOutputStream(stream, new Manifest());

			for (int i = 0; i < filesToAdd.length; i++) {
				if (filesToAdd[i] == null || !filesToAdd[i].exists()
						|| filesToAdd[i].isDirectory())
					continue; // Just in case...
				l.debug("Adding " + filesToAdd[i].getPath());

				// Add archive entry
				JarEntry jarAdd = new JarEntry(filesToAdd[i].getPath().substring(startPathAt));
				jarAdd.setTime(filesToAdd[i].lastModified());
				out.putNextEntry(jarAdd);

				// Write file to archive
				FileInputStream in = new FileInputStream(filesToAdd[i]);
				while (true) {
					int nRead = in.read(buffer, 0, buffer.length);
					if (nRead <= 0)
						break;
					out.write(buffer, 0, nRead);
				}
				in.close();
			}

			out.close();
			stream.close();
			l.debug("Adding completed OK");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error: " + ex.getMessage());
		}
	}

}
