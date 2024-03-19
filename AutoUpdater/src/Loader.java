import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.JFrame;

public class Loader extends JFrame {
        public static final String CACHE_NAME = "zodianXv1";
	public static final String CACHE_PATH = System.getProperty("user.home") + System.getProperty("file.separator") + CACHE_NAME + System.getProperty("file.separator");
        public static final String DIRECT_CLIENT_LINK = "DIRECT DOWNLOAD LINK GOES HERE";

	public static final void main(String[] args) {
		try {
			File file = new File(CACHE_PATH);
			if(!file.exists())
				file.mkdir();
			String fileName = CACHE_PATH + CACHE_NAME + ".jar";
			URL link = new URL(DIRECT_CLIENT_LINK);

			InputStream inStream = link.openStream();
			BufferedInputStream bufIn = new BufferedInputStream(inStream);

			File fileWrite = new File(fileName);
			OutputStream out= new FileOutputStream(fileWrite);
			BufferedOutputStream bufOut = new BufferedOutputStream(out);
			byte buffer[] = new byte[1024];
			
			while (true) {
				int nRead = bufIn.read(buffer, 0, buffer.length);
				if (nRead <= 0)
					break;
				bufOut.write(buffer, 0, nRead);
			}

			bufOut.flush();
			out.close();
			inStream.close();
			new ProcessBuilder("java", "-jar", fileName).start();
		} catch (Exception e) {
			javax.swing.JOptionPane.showConfirmDialog((java.awt.Component) null, e.getMessage(), "Error", javax.swing.JOptionPane.DEFAULT_OPTION);
		}
	}
}