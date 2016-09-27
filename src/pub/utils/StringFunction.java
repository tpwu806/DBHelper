package pub.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

public class StringFunction {
	static ResourceBundle res = ResourceBundle
			.getBundle("com.pansoft.pub.util.Language");
	static int TempKey = 0;
	static final int MaxKey = 999999;

	public static String convertFromStringArrayToStringBySymbol(
			String[] stringArray, char symbol) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < stringArray.length; ++i) {
			sb.append(stringArray[i]);
			sb.append(symbol);
		}
		return sb.toString();
	}

	public static String[] convertFromStringToStringArrayBySymbol(
			String string, String symbol) {
		Vector stringVector = convertFromStringToStringVectorBySymbol(string,
				symbol);
		String[] stringArray = new String[stringVector.size()];
		for (int i = 0; i < stringVector.size(); ++i)
			stringArray[i] = ((String) stringVector.elementAt(i));
		return stringArray;
	}

	public static Vector convertFromStringToStringVectorBySymbol(String string,
			String symbol) {
		StringTokenizer st = new StringTokenizer(string, symbol, true);
		Vector stringVector = new Vector();
		while (st.hasMoreElements()) {
			stringVector.addElement(st.nextElement());
		}
		return stringVector;
	}

	public static String convertFromStringVectorToStringBySymbol(
			Vector stringVector, char symbol) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < stringVector.size(); ++i) {
			sb.append(stringVector.elementAt(i));
			sb.append(symbol);
		}
		return sb.toString();
	}

	public static String defaultFormat(double value) {
		if (value < 0.01D) {
			return "&nbsp;";
		}
		NumberFormat nf = new DecimalFormat("###,###.00");
		return nf.format(value);
	}

	public static String fillString(String psStr, char psC, int psLen) {
		if (psStr.length() > psLen) {
			return psStr.substring(0, psLen);
		}
		char[] vcTemp = new char[psLen];
		for (int i = 0; i < psLen; ++i) {
			vcTemp[i] = psC;
		}
		String vsTemp = new String(vcTemp);
		String vsResult = psStr.concat(vsTemp);
		return vsResult.substring(0, psLen);
	}

	public static String formatValue(double value, int sep, char ch, int round) {
		if (Math.abs(Math.pow(10.0D, round) * value) < 1.0D) {
			return "0.00";
		}
		char[] chs = new char[sep + round + 2];
		chs[0] = ch;
		for (int i = 0; i < sep; ++i)
			chs[(i + 1)] = '#';
		if (round > 0)
			chs[(sep + 1)] = '.';
		for (int i = 1; i <= round; ++i)
			chs[(sep + 1 + i)] = '0';
		String str = new String(chs);
		NumberFormat nf = new DecimalFormat(str);
		return nf.format(value);
	}

	public static String GB2Uni(String original) {
		return original;
	}

	public static String Uni2GB(String original) {
		return original;
	}

	public static String generateID(String pre, int value, int num) {
		char[] chs = new char[num];
		for (int i = 0; i < num; ++i) {
			chs[(num - i - 1)] = (char) (48 + value % 10);
			value /= 10;
		}
		return pre.trim() + new String(chs);
	}

	public static String getTempStr() {
		return String.valueOf(System.currentTimeMillis());
	}

	public static boolean isIncludeString(String psStr, String psS) {
		int viPos = psStr.indexOf(psS);
		return (viPos >= 0);
	}

	public static String replaceString(String psStr, String psS, String psD) {
		int viPos = psStr.indexOf(psS);
		if (viPos < 0)
			return psStr;
		int viLength = psS.length();
		StringBuffer vsValue = new StringBuffer();
		while (viPos >= 0) {
			vsValue.append(psStr.substring(0, viPos));
			vsValue.append(psD);
			psStr = psStr.substring(viPos + viLength);
			viPos = psStr.indexOf(psS);
		}
		vsValue.append(psStr);
		return vsValue.toString();
	}

	public static String[] splitStringByToken(String vsStr, String symbol) {
		String[] vsString = { "", "" };

		int viPos1 = vsStr.indexOf(symbol);
		if (viPos1 < 0) {
			vsString[0] = vsStr;
			vsString[1] = "";
			return vsString;
		}
		vsString[0] = vsStr.substring(0, viPos1);
		vsString[1] = vsStr.substring(viPos1 + symbol.length(), vsStr.length());
		return vsString;
	}

	public static Vector convertFromStringToStringVectorByString(String string,
			String symbol) {
		Vector stringVector = new Vector();
		String vsStr = string.trim();
		String vsTemp = null;
		String[] st = null;
		while ((!(vsStr.equals(""))) && (isIncludeString(vsStr, symbol))) {
			st = splitStringByToken(vsStr, symbol);
			vsTemp = st[0].trim();
			if (!(vsTemp.equals("")))
				stringVector.addElement(vsTemp);
			vsStr = st[1].trim();
		}
		if (!(vsStr.equals("")))
			stringVector.addElement(vsStr);
		return stringVector;
	}

	public static String getTempStr(int length) {
		String str = String.valueOf(System.currentTimeMillis());
		return str.substring(str.length() - length);
	}

	public static synchronized String getTempKey(int length) {
		if (TempKey > 999999) {
			TempKey = 0;
		}
		return String.valueOf(TempKey++);
	}

	public static Vector convertFromStringToStringVectorByStringWithNull(
			String string, String symbol) {
		Vector stringVector = new Vector();
		String vsStr = string.trim();
		String vsTemp = null;
		String[] st = null;
		while ((!(vsStr.equals(""))) && (isIncludeString(vsStr, symbol))) {
			st = splitStringByToken(vsStr, symbol);
			vsTemp = st[0].trim();
			stringVector.addElement(vsTemp);
			vsStr = st[1].trim();
		}
		if (!(vsStr.equals("")))
			stringVector.addElement(vsStr);
		return stringVector;
	}

	public static String DelZeroForBM(String Bm, int Js, String Struct) {
		int Len = Bm.length();
		int Length = GetStructLength(Struct, Js);
		if (Len >= Length) {
			Bm = Bm.substring(0, Length);
		}
		return Bm;
	}

	public static String DelTagForBM(String Bm, int Js, String Struct) {
		int Len = Bm.length();
		int Length = GetStructLength(Struct, Js);
		if (Len >= Length) {
			Bm = Bm.substring(0, Length);
		}
		return Bm;
	}

	public static String FillZeroForBM(String Bm, String Struct) {
		return FillTagForBM(Bm, Struct, "0");
	}

	public static String FillTagForBM(String Bm, String Struct, String tag) {
		int Len = Bm.length();
		int Length = GetStructLength(Struct, 0);
		for (int i = 0; i < Length - Len; ++i) {
			Bm = Bm + tag;
		}
		return Bm;
	}

	public static int getJsByCodingStruct(String code, String struct) {
		int codeLength = code.length();
		int structLength = struct.length();
		int tempLength = 0;
		for (int js = 1; js <= structLength; ++js) {
			tempLength = GetStructLength(struct, js);
			if (codeLength == tempLength)
				return js;
		}
		return 0;
	}

	public static int GetStructLength(String Struct, int JS) {
		int Length = 0;
		if (JS == 0)
			JS = Struct.trim().length();
		for (int i = 0; i < JS; ++i) {
			String sub = Struct.substring(i, i + 1).trim();

			int ii = Integer.parseInt(sub, 36);

			Length += ii;
		}

		return Length;
	}

	public static String GetSubBMfromBM(String BM, String Struct, int SJS) {
		String Res = "";

		int Len = GetStructLength(Struct, SJS);
		if (BM.length() >= Len)
			Res = BM.substring(0, Len);
		return Res;
	}

	public static String Chars2Number(String aCol)
  {
    String strnum = "";
    long num = 0L;
    int i = 0;
    for (i = aCol.length() - 1; i >= 0; --i)
      //num = ()(num + Math.pow(26.0D, i) * (aCol.charAt(aCol.length() - 1 - i) - 'A' + 1));
      num = (long)(num + Math.pow(26.0D, i) * (aCol.charAt(aCol.length() - 1 - i) - 'A' + 1));
    strnum = String.valueOf(num);
    if (strnum == null) strnum = "";
    return strnum;
  }

	public static String FillZeroFromBegin(int num, int leng) {
		return FillTagFromBegin(num, leng, "0");
	}

	public static String FillTagFromBegin(int num, int leng, String Tag) {
		String Res = "";
		String tempStr1 = "";
		String tempStr2 = "";
		tempStr1 = String.valueOf(num);
		for (int i = 0; i < leng - tempStr1.length(); ++i) {
			tempStr2 = tempStr2 + Tag;
		}
		Res = tempStr2 + tempStr1;
		return Res;
	}

	public static String ClearBMZero(String BM, String BMStruct) {
		return ClearBMTag(BM, BMStruct, "0");
	}

	public static String ClearBMTag(String BM, String BMStruct, String Tag) {
		int JSLen = BMStruct.length();
		String bm = "";
		for (int i = 1; i <= JSLen; ++i) {
			String Tmp = BMStruct.substring(i - 1, i);
			int Len = Integer.parseInt(Tmp);
			String ZERO = RepeatChar(Tag, Len);
			if ((BM.startsWith(ZERO)) || (BM.length() < Len))
				break;
			bm = bm + BM.substring(0, Len);
			BM = BM.substring(Len);
		}

		return bm;
	}

	public static String RepeatChar(String c, int len) {
		StringBuffer Res = new StringBuffer();
		for (int i = 0; i < len; ++i) {
			Res.append(c);
		}
		return Res.toString();
	}

	public static String replaceStringAll(String srcString, String[] replace,
			String target) {
		if (replace != null) {
			int i = 0;
			for (int n = replace.length; i < n; ++i) {
				srcString = replaceString(srcString, replace[i], target);
			}
		}
		return srcString;
	}
}