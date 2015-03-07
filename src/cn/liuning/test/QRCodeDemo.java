package cn.liuning.test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;

public class QRCodeDemo {
	/**
	 * 生成二维码图片
	 *  @param content 内容
	 *  @param imgType 图片类型
	 *  @param version 版本
	 *  @param pixel 模块像素值
	 *  @param imgPath 图片地址
	 */
	public void createQrcode(String content,String imgType,
			int version,int pixel,String imgPath){
		//确定二维码图片大小
		int imageSize = (17+4*version)*pixel+4;
		
		//图片流并进行基本设置
		BufferedImage bufImage = new BufferedImage(imageSize,imageSize,
				BufferedImage.TYPE_INT_BGR);
		Graphics2D graphics = bufImage.createGraphics();
		graphics.setBackground(Color.WHITE);
		graphics.clearRect(0, 0, imageSize, imageSize);
		graphics.setColor(Color.BLACK);
		
		//信息转换为二维数组
		boolean[][] codeOut = changeContent(content,version);
		
		//根据二维码数据对图片进行填充
		fillImage(graphics,pixel,codeOut);
		
		//图片输出
		try{
			ImageIO.write(bufImage, imgType, new File(imgPath));
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param content
	 * @param version
	 * @return
	 */
	private boolean[][] changeContent(String content,int version){
		boolean[][] codeOut = null;
		Qrcode qrcode = new Qrcode();
		
		//设置二维码的排错率，可选L(7%)、M(15%)、Q(25%)、H(30%),排错率越高可存储的信息越少
		qrcode.setQrcodeErrorCorrect('M');
		qrcode.setQrcodeEncodeMode('B');
		//设置二维码的尺寸,取值范围1-40,值越大尺寸越大，可存储的信息越大
		qrcode.setQrcodeVersion(version);
		//开始加密内容
		try {
			codeOut = qrcode.calQrcode(content.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return codeOut;
	}
	/**
	 * 对图片内容进行填充
	 * 
	 * @param graphics 画板
	 * @param codeOut 加密后的内容
	 */
	private void fillImage(Graphics2D graphics,int pixel,boolean[][] codeOut){
		//输出内容到二维码
		for(int i=0;i<codeOut.length;i++){
			for(int j=0;j<codeOut.length;j++){
				//填充
				if(codeOut[i][j]){
					//+2的原因是使生成的二维码有白色边框
					graphics.fillRect(i*pixel+2, j*pixel+2, pixel, pixel);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		String content = "http://www.baidu.com";
		String imgType = "png";
		Integer version = 5;
		int pixel=5;//模块像素值
		String imgPath = "D:/QRCodeDemo.png";
		//实现二维码
		QRCodeDemo demo = new QRCodeDemo();
		demo.createQrcode(content, imgType, version, pixel, imgPath);
		System.out.println("二维码生成完毕");
		
	}
}
