package com.example.messages.messages.messages.message.ad_is_crazy;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;


public class recAdoptor extends RecyclerView.Adapter<recAdoptor.Viewholder> {
    private Context context;
    private ArrayList<String> smsnumber, smsbody;
    private RecyclerView recyclerView;
    private int lastp=-1;


    // Updated constructor to include the RecyclerView reference
    recAdoptor(Context context, ArrayList<String> smsnumber, ArrayList<String> smsbody, RecyclerView recyclerView) {
        this.context = context;
        this.smsnumber = smsnumber;
        this.smsbody = smsbody;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recv, parent, false);
        Viewholder viewholder = new Viewholder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.messagenum.setText(smsnumber.get(position));
        holder.messagebody.setText(smsbodymaker(smsbody.get(position),50));
        holder.img.setImageResource(R.drawable.img);


        //set animation
        setanim(holder.itemView,position);



    }

    @Override
    public int getItemCount() {
        return smsnumber.size();
    }

    // Method to add a new message and scroll to the top
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addNewMessage(String number, String body) {
        // Add the new message to your data structures
        int index = smsnumber.indexOf(number);

        if (index != -1) {
            // Number already exists, concatenate the body
            String existingBody = smsbody.get(index);



            String k=existingBody+"\n\n\n\n\nNEXT MESSAGE:\n"+body+"\n";
            smsbody.remove(index);
            smsnumber.remove(index);
            smsbody.add(0,k);
            smsnumber.add(0,number);
        } else {
            smsnumber.add(0, number);
            smsbody.add(0, body+"\n");
        }


        // Notify the adapter that the dataset has changed
        notifyDataSetChanged();

        // Scroll to the top (position 0)
        recyclerView.scrollToPosition(0);
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView messagenum, messagebody;
        ImageView img;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            messagenum = itemView.findViewById(R.id.textView);
            messagebody = itemView.findViewById(R.id.textView2);
            img = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Notify the click event with the number and body
                        String number = smsnumber.get(position);
                        String body = smsbody.get(position);
                        Intent intent=new Intent(context, replayANDview.class);
                        intent.putExtra("NUMBER1", number);
                        intent.putExtra("BODY1", body);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
    private String smsbodymaker(String a,int size){
        int r=a.lastIndexOf("NEXT MESSAGE:");
        if(r==-1) {
            if (a.length() > size) {
                return a.substring(0, size - 3) + "...";
            } else {
                return a;
            }
        }
        else{
            String h=a.substring(r+13);
                if (h.length() > size) {
                    return h.substring(0, size - 3) + "...";
                } else {
                    return h;
                }

        }
    }
    private void setanim(View v,int pos){
        if(pos>lastp) {
            Animation slidein = AnimationUtils.loadAnimation(context,R.anim.recyclarviewanim);
            v.startAnimation(slidein);
            lastp=pos;
        }
    }

}
