package pesko.orgasms.app.global;

import java.util.Arrays;
import java.util.List;

public class MIMETypeConstants {

    public static final String AUDIO_MPEG = "audio/mpeg";
    public static final String AUDIO_MP3 = "audio/mp3";
    public static final String VIDEO_MPEG = "video/mpeg";
    public static final String VIDEO_MP3 = "video/mp3";
    public static final String AUDIO_MP4 = "audio/mp4";
    public static final String VIDEO_MP4 = "video/mp4";
    public static  final String AUDIO_WAV = "audio/wav";

  public static List<String> getValidMimeTypes(){

        return Arrays.asList(AUDIO_MP3,AUDIO_MP4,AUDIO_MPEG,VIDEO_MP3,VIDEO_MP4,VIDEO_MPEG,AUDIO_WAV);
    }
}
