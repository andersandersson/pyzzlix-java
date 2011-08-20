package jsmug.audio;

import org.lwjgl.openal.AL10;

class OpenALUtils {
	public static String getALErrorString(int error) {
		String str = "";
		
		switch(error)
		    {
	        case AL10.AL_NO_ERROR:
	            str = new String("AL_NO_ERROR");
	            break;
			
			case AL10.AL_INVALID_NAME:
				str = new String("AL_INVALID_NAME");
				break;
			
			case AL10.AL_INVALID_ENUM:
				str = new String("AL_INVALID_ENUM");
				break;
			
			case AL10.AL_INVALID_VALUE:
				str = new String("AL_INVALID_VALUE");
				break;
			
			case AL10.AL_INVALID_OPERATION:
				str = new String("AL_INVALID_OPERATION");
				break;
			
			case AL10.AL_OUT_OF_MEMORY:
				str = new String("AL_OUT_OF_MEMORY");
			    break;
		};
		
		return str;
	}
}
