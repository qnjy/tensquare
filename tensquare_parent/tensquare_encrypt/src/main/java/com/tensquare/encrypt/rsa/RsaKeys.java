package com.tensquare.encrypt.rsa;

/**
 * rsa加解密用的公钥和私钥
 * @author Administrator
 *
 */
public class RsaKeys {

	//生成秘钥对的方法可以参考这篇帖子
	//https://www.cnblogs.com/yucy/p/8962823.html

//	//服务器公钥
//	private static final String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6Dw9nwjBmDD/Ca1QnRGy"
//											 + "GjtLbF4CX2EGGS7iqwPToV2UUtTDDemq69P8E+WJ4n5W7Iln3pgK+32y19B4oT5q"
//											 + "iUwXbbEaAXPPZFmT5svPH6XxiQgsiaeZtwQjY61qDga6UH2mYGp0GbrP3i9TjPNt"
//											 + "IeSwUSaH2YZfwNgFWqj+y/0jjl8DUsN2tIFVSNpNTZNQ/VX4Dln0Z5DBPK1mSskd"
//											 + "N6uPUj9Ga/IKnwUIv+wL1VWjLNlUjcEHsUE+YE2FN03VnWDJ/VHs7UdHha4d/nUH"
//											 + "rZrJsKkauqnwJsYbijQU+a0HubwXB7BYMlKovikwNpdMS3+lBzjS5KIu6mRv1GoE"
//											 + "vQIDAQAB";
//
//	//服务器私钥(经过pkcs8格式处理)
//	private static final String serverPrvKeyPkcs8 = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDoPD2fCMGYMP8J"
//				 								 + "rVCdEbIaO0tsXgJfYQYZLuKrA9OhXZRS1MMN6arr0/wT5YniflbsiWfemAr7fbLX"
//				 								 + "0HihPmqJTBdtsRoBc89kWZPmy88fpfGJCCyJp5m3BCNjrWoOBrpQfaZganQZus/e"
//				 								 + "L1OM820h5LBRJofZhl/A2AVaqP7L/SOOXwNSw3a0gVVI2k1Nk1D9VfgOWfRnkME8"
//				 								 + "rWZKyR03q49SP0Zr8gqfBQi/7AvVVaMs2VSNwQexQT5gTYU3TdWdYMn9UeztR0eF"
//				 								 + "rh3+dQetmsmwqRq6qfAmxhuKNBT5rQe5vBcHsFgyUqi+KTA2l0xLf6UHONLkoi7q"
//				 								 + "ZG/UagS9AgMBAAECggEBANP72QvIBF8Vqld8+q7FLlu/cDN1BJlniReHsqQEFDOh"
//				 								 + "pfiN+ZZDix9FGz5WMiyqwlGbg1KuWqgBrzRMOTCGNt0oteIM3P4iZlblZZoww9nR"
//				 								 + "sc4xxeXJNQjYIC2mZ75x6bP7Xdl4ko3B9miLrqpksWNUypTopOysOc9f4FNHG326"
//				 								 + "0EMazVaXRCAIapTlcUpcwuRB1HT4N6iKL5Mzk3bzafLxfxbGCgTYiRQNeRyhXOnD"
//				 								 + "eJox64b5QkFjKn2G66B5RFZIQ+V+rOGsQElAMbW95jl0VoxUs6p5aNEe6jTgRzAT"
//				 								 + "kqM2v8As0GWi6yogQlsnR0WBn1ztggXTghQs2iDZ0YkCgYEA/LzC5Q8T15K2bM/N"
//				 								 + "K3ghIDBclB++Lw/xK1eONTXN+pBBqVQETtF3wxy6PiLV6PpJT/JIP27Q9VbtM9UF"
//				 								 + "3lepW6Z03VLqEVZo0fdVVyp8oHqv3I8Vo4JFPBDVxFiezygca/drtGMoce0wLWqu"
//				 								 + "bXlUmQlj+PTbXJMz4VTXuPl1cesCgYEA6zu5k1DsfPolcr3y7K9XpzkwBrT/L7LE"
//				 								 + "EiUGYIvgAkiIta2NDO/BIPdsq6OfkMdycAwkWFiGrJ7/VgU+hffIZwjZesr4HQuC"
//				 								 + "0APsqtUrk2yx+f33ZbrS39gcm/STDkVepeo1dsk2DMp7iCaxttYtMuqz3BNEwfRS"
//				 								 + "kIyKujP5kfcCgYEA1N2vUPm3/pNFLrR+26PcUp4o+2EY785/k7+0uMBOckFZ7GIl"
//				 								 + "FrV6J01k17zDaeyUHs+zZinRuTGzqzo6LSCsNdMnDtos5tleg6nLqRTRzuBGin/A"
//				 								 + "++xWn9aWFT+G0ne4KH9FqbLyd7IMJ9R4gR/1zseH+kFRGNGqmpi48MS61G0CgYBc"
//				 								 + "PBniwotH4cmHOSWkWohTAGBtcNDSghTRTIU4m//kxU4ddoRk+ylN5NZOYqTxXtLn"
//				 								 + "Tkt9/JAp5VoW/41peCOzCsxDkoxAzz+mkrNctKMWdjs+268Cy4Nd09475GU45khb"
//				 								 + "Y/88qV6xGz/evdVW7JniahbGByQhrMwm84R9yF1mNwKBgCIJZOFp9xV2997IY83S"
//				 								 + "habB/YSFbfZyojV+VFBRl4uc6OCpXdtSYzmsaRcMjN6Ikn7Mb9zgRHR8mPmtbVfj"
//				 								 + "B8W6V1H2KOPfn/LAM7Z0qw0MW4jimBhfhn4HY30AQ6GeImb2OqOuh3RBbeuuD+7m"
//				 								 + "LpFZC9zGggf9RK3PfqKeq30q";

	//服务器公钥
	private static final String serverPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0br0tU6rHjXQp1qQmfBs\n" +
			"q9HLmxmkz+jneObOG3pG/wx6WHOJtRsIQO8u01ING5wmo0P1Wl4M43QQFRef++Dl\n" +
			"gbPAmdKYc5yqTtqOV6U06BY3MYLYzsWUL141aa3rCT9Rh75JWNcFq7+T64MCdUCq\n" +
			"Ku4j6Qqzf3dDiGZCoXLy7B5Wo+Cgwl7j7f+qWsaCFQLJfLpOHO5UXa4/rRElhsMM\n" +
			"BKPV1mJv/wGCqGcHFJ3IMYnShBuTz1wG532+HeAwhqXZIJ8g6rRKzFVuuFVIZHyJ\n" +
			"4gAzyVhUUDHunf5AgEYU4WUcKMoncOKILwrpvZ61dEEnojv8AKLgHouEScqeftae\n" +
			"5QIDAQAB";

	//服务器私钥(经过pkcs8格式处理)
	private static final String serverPrvKeyPkcs8 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDRuvS1TqseNdCn\n" +
			"WpCZ8Gyr0cubGaTP6Od45s4bekb/DHpYc4m1GwhA7y7TUg0bnCajQ/VaXgzjdBAV\n" +
			"F5/74OWBs8CZ0phznKpO2o5XpTToFjcxgtjOxZQvXjVpresJP1GHvklY1wWrv5Pr\n" +
			"gwJ1QKoq7iPpCrN/d0OIZkKhcvLsHlaj4KDCXuPt/6paxoIVAsl8uk4c7lRdrj+t\n" +
			"ESWGwwwEo9XWYm//AYKoZwcUncgxidKEG5PPXAbnfb4d4DCGpdkgnyDqtErMVW64\n" +
			"VUhkfIniADPJWFRQMe6d/kCARhThZRwoyidw4ogvCum9nrV0QSeiO/wAouAei4RJ\n" +
			"yp5+1p7lAgMBAAECggEAcItChYiRisSWEzgmIHVD22/ZMoGwT2FL1TUFpPkY4ARy\n" +
			"IsTdQLIg27d+CSgeoJJKqZHuN3AAuo2tk6P51c/5WIB8+g5ogKGxQl9FwwKEQYaq\n" +
			"V4c/kLW1rzV+tBb/6RZxScZy1ZmNEXzcg6TpzMHGjwL7gjKb3+q/8pHtb3WfpdQo\n" +
			"YmOdeF7tsPjF+Wi5WmsdYO5kCpOSgYo0ueUX7XQKF8hs1eHtPfqhnCWdsV2rYxIp\n" +
			"lP/eux31AWRGPCLMAwM58k+4jw2ek16libmGtq/m8eKCOJVo94+5qSL97oBttgDq\n" +
			"v42ByVqxOTJB6ctR7YGMVxejs4QCCc4wSIWHo8yggQKBgQD7oCHlNrNDj4nKgvRN\n" +
			"shdlXJycq61s7ygipEjUtbekQEycjUtHpJHrgnGz2j7FPlCBFit5jmJkZqjRWmBU\n" +
			"dMEnEWiJy+PNtxEkhGFjpHCeKIF6p2u3q0EOtiX0KqTpspMaW6wCvdrkangt/fIC\n" +
			"C5N6+r8qBNYNpLAP8JdieNjV7wKBgQDVYF4QXVd9xzxng5+FMYD/aHlvVsJweqkg\n" +
			"CFyRcEmpXqT95fvhil0w9IXKypga7/sIAGNDhUAZEu71YSIoqh/iWp14YZ944gpW\n" +
			"9CJcmIPm0eKB9jFRT4FnhzMXy73ZBmpX/DpRuRykyawoyv4HFhODekS4R3L71q6K\n" +
			"T/TsDrIMawKBgQCpCQf8LWT7y99q9dIcp7jj1PU4onPcrESwEOEEVBbL9o4A1UJv\n" +
			"HHUSWwz7H2/vnU8DiNsny6j5faWHDkmMMurWzfEGMLwWJBcWCwBJqdwlw1xIA1Rv\n" +
			"jFImCeMxRbyHafk8Sb0iElyL+aJGc0Qyb4hhozTsi1cozyufaaao9jnkbQKBgFqe\n" +
			"f7Rhf/vJ9qxbqQ7PFGLkQtZ3OUK9xT/2bSHTV2VX7ahq2QtTUPqujneujOS70Jf4\n" +
			"Wi/d0ngTo2qQ4wn0HnBqXYA3rZuQHCYLruA2YaZQsurQ3OcUZffK4ncTr0ARSm6t\n" +
			"jzMP88tEXxpAA5yM0qK+9ZpxtbKg3SWeiX0LTLCbAoGAKdhvbGm5Iv6XkIXn80WE\n" +
			"BXVqWICz1JKEhDuVl6mTnhu322vNf/m4bu9XzyuSMFzRaJq/EBo+9j9ya6XhY9Am\n" +
			"m4Y6o37jCS8OPXjvNhama20KhBjIRVFkc2hn16jZ/X5ZqzCOR8ckT9tlnBuOQ7DF\n" +
			"1TrY64cRtfuouEZeMAPfglE=";

	public static String getServerPubKey() {
		return serverPubKey;
	}

	public static String getServerPrvKeyPkcs8() {
		return serverPrvKeyPkcs8;
	}
	
}
