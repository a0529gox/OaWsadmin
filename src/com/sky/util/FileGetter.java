package com.sky.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.zip.ZipException;



public class FileGetter {

	public FileGetter() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) throws ZipException, IOException, URISyntaxException {
		
	}
	
	public static String getFile(String fileName) {
        URI exe = null;
        String path = null;

        try {
			exe = getFileURI(fileName);
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        if (exe != null) {
        	path = exe.getPath();
    		if (path.indexOf("\\") == 0 || path.indexOf("/") == 0) {
    			path = path.substring(1);
    		}
        }
        
        return path;
	}

	private static URI getFileURI(final String fileName)
			throws ZipException, IOException {
		URI fileURI = null;
		InputStream in = FileGetter.class.getClassLoader().getResourceAsStream("misc/" + fileName);
		fileURI = extract(in, fileName);

		return (fileURI);
	}

	private static URI extract(final InputStream in, final String fileName)
			throws IOException {
		
		final File tempFile;
		OutputStream fileStream;

		tempFile = File.createTempFile(
				Long.toString(System.currentTimeMillis())
				, fileName);
		
		tempFile.deleteOnExit();
		fileStream = null;

		try {
			final byte[] buf;
			int i;

			fileStream = new FileOutputStream(tempFile);
			buf = new byte[1024];
			i = 0;

			while ((i = in.read(buf)) != -1) {
				fileStream.write(buf, 0, i);
			}
		} finally {
			close(in);
			close(fileStream);
		}

		return (tempFile.toURI());
	}

	private static void close(final Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
