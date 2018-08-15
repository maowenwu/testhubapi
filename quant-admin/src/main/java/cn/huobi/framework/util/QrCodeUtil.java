package cn.huobi.framework.util;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrCodeUtil {
	private  int SIZE;
	private  int LOG_SIZE;
	private  String FORMAT;
	private final static String CHARSET = "utf-8";
	private final static Logger logger = LoggerFactory.getLogger(QrCodeUtil.class);

	private QrCodeUtil(int size,int logSize, String format) {
		this.SIZE = size;
		this.FORMAT = format;
		this.LOG_SIZE=logSize;
	}
	private QrCodeUtil(int size, String format) {
		this.SIZE = size;
		this.FORMAT = format;
		this.LOG_SIZE=Double.valueOf(SIZE*0.2).intValue();
	}

	private QrCodeUtil() {
		this.SIZE = 300;
		this.FORMAT = "jpg";
		this.LOG_SIZE=Double.valueOf(SIZE*0.2).intValue();
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content 内容
	 * @param imgPath LOGO地址
	 * @param destPath 存储地址
	 * @throws Exception
	 */
	public static void create(int size,int logSize,String format,String content, String imgPath, String destPath,boolean needCompress)  {
		try{
			new QrCodeUtil(size,logSize,format).encode(content, imgPath, destPath, needCompress);
		}catch(Exception e){
			logger.error("生成二维码失败");
		}
	}
	
	public static void create(String content, String imgPath, String destPath,boolean needCompress)  {
		try{
			new QrCodeUtil().encode(content, imgPath, destPath, needCompress);
		}catch(Exception e){
			logger.error("生成二维码失败");
		}
	}
	public static void create(String content, String imgPath, String destPath)  {
		QrCodeUtil.create(content, imgPath, destPath, true);
	}
	
	public static void create(String content, String destPath)  {
		QrCodeUtil.create(content, null, destPath, false);
	}
	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content 内容
	 * @param imgPath LOGO地址
	 * @param destPath 存放目录
	 * @param needCompress 是否压缩LOGO
	 * @throws Exception
	 */
	public void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
		BufferedImage image = createImage(content, imgPath, needCompress);
		mkdirs(destPath);
		ImageIO.write(image, FORMAT, new File(destPath));
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content 内容
	 * @param imgPath LOGO地址
	 * @param output  输出流
	 * @param needCompress  是否压缩LOGO
	 * @throws Exception
	 */
	public void encode(String content, String imgPath, OutputStream output, boolean needCompress)
			throws Exception {
		BufferedImage image = createImage(content, imgPath, needCompress);
		ImageIO.write(image, FORMAT, output);
	}

	/**
	 * 解析二维码
	 * 
	 * @param file 二维码图片
	 * @return
	 * @throws Exception
	 */
	public static String decode(File file) throws Exception {
		BufferedImage image = ImageIO.read(file);
		if (image == null) {
			return null;
		}
		BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Result result;
		Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
		hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
		result = new MultiFormatReader().decode(bitmap, hints);
		String resultStr = result.getText();
		return resultStr;
	}

	/**
	 * 解析二维码
	 * @param path
	 *            二维码图片地址
	 * @return
	 * @throws Exception
	 */
	public static String decode(String path) throws Exception {
		return QrCodeUtil.decode(new File(path));
	}
	
	private BufferedImage createImage(String content, String imgPath, boolean needCompress) throws Exception {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, SIZE, SIZE, hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		if (imgPath == null || "".equals(imgPath)) {
			return image;
		}
		// 插入图片
		insertImage(image, imgPath, needCompress);
		return image;
	}

	/**
	 * 插入LOGO
	 * @param source 二维码图片
	 * @param imgPath LOGO图片地址
	 * @param needCompress 是否压缩
	 * @throws Exception
	 */
	private void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
		File file = new File(imgPath);
		if (!file.exists()) {
			logger.error("" + imgPath + "   该文件不存在！");
			return;
		}
		Image src = ImageIO.read(new File(imgPath));
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if (needCompress) { // 压缩LOGO
			if (width > LOG_SIZE) {
				width = LOG_SIZE;
			}
			if (height > LOG_SIZE) {
				height = LOG_SIZE;
			}
			Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			src = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (SIZE - width) / 2;
		int y = (SIZE - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	/**
	 * 当创建上级目录
	 */
	public void mkdirs(String destPath) {
		File file = new File(destPath);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}
}
