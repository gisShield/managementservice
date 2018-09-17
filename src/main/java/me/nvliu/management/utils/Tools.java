package me.nvliu.management.utils;

import java.util.Random;

/**
 * @Author:mvp
 * @ProjectName: php
 * @PacketName: com.nvliu.php.service.impl
 * @Description: 工具类
 * @Date: Create in 17:19 2017/11/23
 * @Modified By:
 */
public class Tools {

	/**
	 * 返回随机数
	 * 
	 * @param n 个数
	 * @return
	 */
	public static String random(int n) {
		if (n < 1 || n > 10) {
			throw new IllegalArgumentException("cannot random " + n + " bit number");
		}
		Random ran = new Random();
		if (n == 1) {
			return String.valueOf(ran.nextInt(10));
		}
		int bitField = 0;
		char[] chs = new char[n];
		for (int i = 0; i < n; i++) {
			while (true) {
				int k = ran.nextInt(10);
				if ((bitField & (1 << k)) == 0) {
					bitField |= 1 << k;
					chs[i] = (char) (k + '0');
					break;
				}
			}
		}
		return new String(chs);
	}

	/**
	 * 指定范围的随机数
	 * 
	 * @param min
	 *            最小值
	 * @param max
	 *            最大值
	 * @return
	 */
	public static int getRandomNum(int min, int max) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	/**
	 * 检测字符串是否不为空(null,"","null")
	 * 
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	/**
	 * 检测字符串是否为空(null,"","null")
	 * 
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s) || "null".equals(s);
	}
	
	/**
	 * 检测是否为数字
	 * @param s
	 * @return
	 */
	public static boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public static String acfunTime(String url){
		if (url !=null && !url.equals("")){
			String [] strs = url.split("/+");
			String str = strs[strs.length-1] ;
			return str.split("\\.")[0];
		}
		return null;
	}
	public static boolean notEmpty(Object s) {
		return s != null && !"".equals(s) && !"null".equals(s);
	}

	public static void main(String[] args) {
//		System.out.println(isNumber("22q3"));
		/*String url ="http://imgs.aixifan.com/content/2017_11_21/1513867554.jpg";
		String [] strs = url.split("/+");
		for (int i =0 ;i<strs.length;i++){
			System.out.println("strs[" + i + "] = " +strs[i]);
		}*/

		String time = String.valueOf( System.currentTimeMillis()/ '?') + "";
		System.out.println("time = [" + time + "]");
		String e = "room/673890?aid=androidhd1&cdn=ws&client_sys=android&time="+time;
		String http = "http://capi.douyucdn.cn/api/v1/";

		String auth = MD5Util.MD51(e+"Y237pxTx2In5ayGz").toLowerCase();

		System.out.println("\n" +
				"https://capi.douyucdn.cn/lapi/live/appGetPlayer/stream/20360?token=&rate=-1&cdn=&txdw=0&cptl=0101&csign="+auth+"&client_sys=android");

	}
}
