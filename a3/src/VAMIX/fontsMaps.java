package VAMIX;

import java.util.HashMap;
import java.util.Map;

public class fontsMaps {
	public static Map<String , String> fonts = new HashMap<String, String>();
	public static void createMap(){
		fonts.put("DroidSans", "/usr/share/fonts/truetype/droid/DroidSans.ttf");
		fonts.put("NanumGothic", "/usr/share/fonts/truetype/nanum/NanumGothic.ttf");
		fonts.put("eufm10", "/usr/share/fonts/truetype/lyx/eufm10.ttf");
		fonts.put("Loma", "/usr/share/fonts/truetype/tlwg/Loma.ttf");
		fonts.put("Waree", "/usr/share/fonts/truetype/tlwg/Waree.ttf");
		fonts.put("Phetsarath_OT", "/usr/share/fonts/truetype/lao/Phetsarath_OT.ttf");
		fonts.put("Padauk", "/usr/share/fonts/truetype/padauk/Padauk.ttf");
		fonts.put("FreeSans", "/usr/share/fonts/truetype/freefont/FreeSans.ttf");
		
	}
	public static String getfonts(String key){
		return fonts.get(key);
		
	}
}

