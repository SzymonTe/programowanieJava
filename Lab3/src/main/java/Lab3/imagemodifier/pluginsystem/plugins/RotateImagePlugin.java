package Lab3.imagemodifier.pluginsystem.plugins;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import Lab3.imagemodifier.pluginsystem.IImagePlugin;

public class RotateImagePlugin implements IImagePlugin {

	@Override
	public byte[] transformImage(byte[] imageBytes) {

		try {
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

			for (int i = 0; i < image.getWidth(); i++) {
				for (int j = 0; j < image.getHeight() / 2; j++) {
					int tmp = image.getRGB(i, j);
					image.setRGB(i, j, image.getRGB(i, image.getHeight() - j - 1));
					image.setRGB(i, image.getHeight() - j - 1, tmp);
				}
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			byte[] imageInByte = baos.toByteArray();

			baos.close();

			return imageInByte;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getName() {
		return "Rotate Image";
	}

}
