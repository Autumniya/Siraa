package apk.siraal.siraa;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class albumadapter extends RecyclerView.Adapter<albumadapter.cardViewHolder> {

    private ArrayList<cardhelper> mcardalbums;
    private List<cardhelper> originalItems;
    int audio;
    NotificationManager notificationManager;
    Context context;


    public albumadapter (ArrayList<cardhelper> cardalbums) {
        mcardalbums = cardalbums;
        this.originalItems = new ArrayList<>();


        originalItems.addAll(mcardalbums);
    }



    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design, parent, false);
        cardViewHolder cvh= new cardViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {
        cardhelper currentItem = mcardalbums.get(position);
        holder.image.setImageResource(currentItem.getThumbnail());
        holder.count.setText(currentItem.getNumOfSongs());
        holder.surah.setText(currentItem.getSurah());
        holder.seekbarTime.setProgress(mcardalbums.get(position).getSeekbarTime());

        holder.setItem(mcardalbums.get(position));

    }



    @Override
    public int getItemCount() {
        return mcardalbums.size();
    }




    public static class cardViewHolder extends RecyclerView.ViewHolder {

        public SeekBar seekbarTime;
        Context context;
        private cardhelper mItem;
        private static MediaPlayer mMediaplayer;
        public ImageView btnPlay,btnPause,image;
        NotificationManager notificationManager;
        Notification status;
        public static Notification notification;
        static FloatingActionButton fab,fab2;
        private RecyclerView mcardrecycler;
        ArrayList<cardhelper> cardalbums = new ArrayList<>();
        private ArrayList<cardhelper> mcardalbums;
        cardhelper cardhelper;
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        private  final String LOG_TAG = "NotificationService";


        public TextView count,surah,textDuration,textPosition;
        public Handler mHandler = new Handler();
        boolean flags;
        private double startTime = 0;
        private double finalTime = 0;
        public void setItem(cardhelper item){
            this.mItem=item;
        }
        @SuppressLint("ResourceType")
        public cardViewHolder(View itemView) {
            super(itemView);

            image= itemView.findViewById(R.id.thumbnail);
            count= itemView.findViewById(R.id.card_count);
            surah= itemView.findViewById(R.id.card_surah);
            textDuration = itemView.findViewById(R.id.textTotalTime);
            textPosition = itemView.findViewById(R.id.textCurrentTime);
            btnPlay = itemView.findViewById(R.id.buttonPlay);
            btnPause=itemView.findViewById(R.drawable.ic_baseline_pause_24);
            fab=itemView.findViewById(R.id.fab);
            fab2=itemView.findViewById(R.id.fab2);
            seekbarTime= itemView.findViewById(R.id.playerSeekbar);
            btnPlay.setOnClickListener(new View.OnClickListener() {


                @SuppressLint({"DefaultLocale", "ClickableViewAccessibility"})
                @Override
                public void onClick(View v) {
                    if(flags){
                        final MediaPlayer mMediaplayer =new MediaPlayer();
                        flags=false;
                    }

                    if(mMediaplayer!=null && mMediaplayer.isPlaying()){
                        mHandler.removeCallbacks(UpdateSongTime);
                        mMediaplayer.pause();
                        btnPlay.setImageResource(R.drawable.ic_play);
                        finalTime = mMediaplayer.getDuration();
                        startTime = mMediaplayer.getCurrentPosition();
                        flags=true;


                    }else{

                        mMediaplayer= MediaPlayer.create(v.getContext(),mItem.getAudio());

                        mMediaplayer.start();
                        mMediaplayer.seekTo((int)startTime);
                        btnPlay.setImageResource(R.drawable.ic_baseline_pause_24);


                    }




                    seekbarTime.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                           if (event.getAction() == MotionEvent.ACTION_UP){
                               startTime = seekbarTime.getProgress();
                               seekbarTime.setProgress((int) startTime);
                               mMediaplayer.seekTo((int) startTime);
                           }
                           

                            return false;
                        }
                    });

                    mMediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            seekbarTime.setProgress(0);
                            btnPlay.setImageResource(R.drawable.ic_play);
                            textPosition.setText(R.string.zero);
                            textDuration.setText(R.string.fatihaend);
                            mMediaplayer.reset();
                            textDuration.setText(String.format("%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                                    finalTime)))
                            );



                        }
                    });
                    seekbarTime.setProgress((int)startTime);
                    mHandler.postDelayed(UpdateSongTime, 100);


                }


            });

        }
        public void startservice(Intent serviceIntent) {
            serviceIntent  = new Intent(itemView.getContext(), NotificationService.class);
            serviceIntent.setAction(NotificationService.PLAY_ACTION);
            startservice(serviceIntent);

        }

        private Runnable UpdateSongTime = new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                if(mMediaplayer!=null && mMediaplayer.isPlaying()){
                    seekbarTime.setProgress((int) startTime);
                    seekbarTime.setMax((int) finalTime);
                    mHandler.postDelayed(this, 100);

                }
                startTime = mMediaplayer.getCurrentPosition();
                finalTime = mMediaplayer.getDuration();
                textPosition.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                        toMinutes((long) startTime)))
                );

                textDuration.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime)))
                );


            };
        };


    }

    public void filteri(String newText) {
        if (newText.length() == 0) {
            mcardalbums.clear();
            mcardalbums.addAll(originalItems);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mcardalbums.clear();
                List<cardhelper> collect = originalItems.stream()
                        .filter(i -> i.getSurah().toLowerCase().contains(newText))
                        .collect(Collectors.toList());

                mcardalbums.clear();
                mcardalbums.addAll(collect);
            } else {
                mcardalbums.clear();
                for (cardhelper i : originalItems) {
                    if (i.getSurah().toLowerCase().contains(newText)) {
                        mcardalbums.add(i);
                    }
                }
            }

        }
        notifyDataSetChanged();


    }


}