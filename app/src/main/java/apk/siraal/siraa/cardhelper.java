package apk.siraal.siraa;


public  class  cardhelper {

         private int thumbnail,audio;
         private  int seekbarTime;
         private String surah;
         private String numOfSongs;




         public cardhelper(int thumbnail, String surah, int audio, String numOfSongs) {
             this.thumbnail = thumbnail;
             this.surah = surah;
             this.numOfSongs = numOfSongs;
             this.audio=audio;




         }



    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public  int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public String getSurah() {
        return surah;
    }

    public void setSurah(String surah) {
        this.surah = surah;
    }

    public String getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(String numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getSeekbarTime() {
        return seekbarTime;
    }



}


