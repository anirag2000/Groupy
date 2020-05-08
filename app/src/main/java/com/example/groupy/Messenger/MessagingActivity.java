package com.example.groupy.Messenger;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groupy.ColourAdapter;
import com.example.groupy.R;
import com.example.groupy.Service.Client;
import com.example.groupy.Service.Data;
import com.example.groupy.Service.MyResponse;
import com.example.groupy.Service.Sender;
import com.example.groupy.Service.Token;
import com.example.groupy.User_details;
import com.example.groupy.calling.Apps;
import com.example.groupy.calling.IncommingCallActivity;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class MessagingActivity extends AppCompatActivity {
    DrawableViewConfig config;
    Uri downloadUrl;
    String final_uri;
    ImageView photo;
    ImageButton draw;
    ImageButton camera;
    StorageReference mStorageRef;
    Dialog dialog;
    static int left;
    String fileUrl;
    static int right;
    CircleImageView rimage;
    CircleImageView image;
    TextView text;
    Intent intent;
    DrawableView drawableView;
    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DatabaseReference temp;
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    LinearLayoutManager linearLayoutManager;
    ImageButton send;
    EditText message;
    int draw_toggle = 0;
    String token;
    String msg;
    String userid;
    String userpicurl;
    Timer timer;
    long DELAY;
    CircleImageView t_image;
    TextView t_text;
    APIService apiService;
    boolean notify = false;
    int colour_rec_toggle=0;

    ChatAdapter messageAdapter;
    RecyclerView recyclerView;
    List<Chat> texts = new ArrayList<>();

    public void updateToken(String token) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token obj = new Token(token);
        databaseReference.child(firebaseUser.getUid()).setValue(obj);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        config = new DrawableViewConfig();

ArrayList<String> colours= new ArrayList<>();
colours.add("A");
colours.add("B");








colours.add("#b203c8");
        colours.add("#3d84dc");
        colours.add("#386906");
        colours.add("#70ca1e");
        colours.add("#f4e130");
        colours.add("#f49a1f");colours.add("#cc0018");





        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        RecyclerView color_recycler= findViewById(R.id.colour_recyclerview);
        color_recycler.setLayoutManager(layoutManager);
        ColourAdapter adapter = new ColourAdapter(this, colours);
        color_recycler.setAdapter(adapter);
        color_recycler.setVisibility(View.GONE);






        left = 0;
        right = 0;
        t_image = findViewById(R.id.circleimage);
        t_text = findViewById(R.id.username);
        AlertDialog.Builder builder = new AlertDialog.Builder(MessagingActivity.this);
        builder.setCancelable(false); // if you want user to wait for some process to finish,
        builder.setView(R.layout.layout_loading_dialog);
        dialog = builder.create();
        ImageButton imageButton = findViewById(R.id.camera);
        imageButton.setOnClickListener(v -> {


            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(MessagingActivity.this);
        });

        draw = findViewById(R.id.imageView8);
        camera = findViewById(R.id.camera);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colour_rec_toggle=colour_rec_toggle+1;
                if(colour_rec_toggle%2==0)
                {
                    color_recycler.setVisibility(View.GONE);
                }
                else{
                    color_recycler.setVisibility(View.VISIBLE);
                }

            }
        });


        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessagingActivity.super.onBackPressed();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MessagingActivity.this, instanceIdResult -> {
            token = instanceIdResult.getToken();
            Log.e("This is the token", token);
            updateToken(token);
        });


        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        //initialize all the components on screen
        //rimage=findViewById(R.id.rimage);
        text = findViewById(R.id.username);
        send = findViewById(R.id.send);
        message = findViewById(R.id.typed_message);
        //image=findViewById(R.id.circleimage);
        Toast.makeText(MessagingActivity.this, Apps.USER_ID, Toast.LENGTH_LONG).show();


        //whats the receiver's details for the page load
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();


        recyclerView = findViewById(R.id.recyclerview);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);

        //loading the page
        userid = intent.getStringExtra("userid");
        reference = database.getReference("Users");
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User_details user = dataSnapshot.child(userid).getValue(User_details.class);
                //User_details user = dataSnapshot.child(currentuser).getValue(User_details.class);
                //  Log.e("this is walter",userid);
                // Log.e("this is the me",ruser.getName());

                //for the receiver
                image = findViewById(R.id.circleimage);
                text.setText(user.getName());
                image.setOnLongClickListener(new View.OnLongClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public boolean onLongClick(View v) {
                        if (Apps.callClient == null) {
                            Toast.makeText(MessagingActivity.this, "Sinch Client not connected", Toast.LENGTH_SHORT).show();

                        } else {
                            com.sinch.android.rtc.calling.Call currentcall = Apps.callClient.callUser(userid);
                            Intent callscreen = new Intent(MessagingActivity.this, IncommingCallActivity.class);
                            Pair[] pairs = new Pair[2];

                            pairs[0] = new Pair<View, String>(t_image, "imageTransition");
                            pairs[1] = new Pair<View, String>(t_text, "textTransition");

                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MessagingActivity.this, pairs);
                            callscreen.putExtra("photo", user.getPhotourl());
                            callscreen.putExtra("name", user.getName());
                            callscreen.putExtra("callid", currentcall.getCallId());
                            callscreen.putExtra("incomming", false);
                            callscreen.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            startActivity(callscreen, options.toBundle());

                        }
                        return false;
                    }
                });
                Glide.with(MessagingActivity.this).load(user.getPhotourl()).into(image);


                //for the sender
                // Log.e("this is the photourl",user.getPhotourl());
                //Glide.with(MessagingActivity.this).load(user.getPhotourl()).into(rimage);

                messageAdapter = new ChatAdapter(MessagingActivity.this, texts, user.getPhotourl(), currentuser);


                readmessages(firebaseUser.getUid(), userid, user.getPhotourl(), currentuser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("AddDetails").child(currentuser).child(userid).child("Typing").setValue("0");
        reference.child("AddDetails").child(currentuser).child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Typing").getValue(String.class).equals("1")) {
                    TextView typing = findViewById(R.id.typing);
                    typing.setVisibility(View.VISIBLE);
                }
                if (dataSnapshot.child("Typing").getValue(String.class).equals("0")) {
                    TextView typing = findViewById(R.id.typing);
                    typing.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        LinearLayout linearLayout = findViewById(R.id.linearLayout2);
        linearLayout.setBackgroundResource(R.drawable.red);
        reference = FirebaseDatabase.getInstance().getReference().child("online_statuses");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userid)) {
                    if (dataSnapshot.child(userid).getValue(String.class).equals("online")) {
                        linearLayout.setBackgroundResource(R.drawable.green);
                    } else {
                        linearLayout.setBackgroundResource(R.drawable.red);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        KeyboardVisibilityEvent.setEventListener(
                this,
                (KeyboardVisibilityEventListener) isOpen -> {
                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());


                });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        timer = new Timer();
        DELAY = 1000; // in ms
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {
                databaseReference.child("AddDetails").child(userid).child(currentuser).child("Typing").setValue("1");
                if (timer != null)
                    timer.cancel();
            }

            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is too short
                if (s.length() >= 0) {

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // TODO: do what you need here (refresh list)
                            databaseReference.child("AddDetails").child(userid).child(currentuser).child("Typing").setValue("0");

                        }

                    }, DELAY);
                }
            }
        });

        //send the message
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typedmessage = message.getText().toString();
                drawableView = findViewById(R.id.drawable_view);
                if (!typedmessage.isEmpty() || drawableView.getVisibility() == View.VISIBLE) {
                    notify = true;
                    try {
                        sendmessage(userid, firebaseUser.getUid(), typedmessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //keyboard closing after send

                } else {
                    Toast.makeText(MessagingActivity.this, "Oops, you can't send empty messages!", Toast.LENGTH_SHORT).show();
                }
                message.setText("");

            }
        });


    }


    void sendmessage(String reciever, String from, String message) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String date_string = dateFormat.format(date); //2016/11/16 12:08:43
        reference = database.getReference();
        message.trim();

        drawableView = findViewById(R.id.drawable_view);
        if (drawableView.getVisibility() == View.GONE) {
            if (message.trim().isEmpty()) {
                return;
            }
            Chat chat = new Chat(reciever, from, message, date_string, "text");

            reference.child("Chats").push().setValue(chat);


            msg = message;
        } else {
            drawableView.setVisibility(View.GONE);
            camera.setVisibility(View.VISIBLE);
            draw.setImageResource(R.drawable.draw);


            Bitmap drawn_image = drawableView.obtainBitmap();
            Canvas canvas = new Canvas(drawn_image);

            Paint paint = new Paint();
            Typeface type = ResourcesCompat.getFont(this,R.font.bad_script);
            paint.setTypeface(type);

            paint.setColor(Color.parseColor(Apps.colour)); // Text Color
            paint.setTextSize(80); // Text Size

            canvas.drawText(message, 50, drawn_image.getHeight()-50, paint);


            RandomString randomString = new RandomString();
            String random = randomString.generate();
            String filePath = "/storage/emulated/0/Download/" + random + ".jpg";


            File dest = new File(filePath);
            dialog.show();

            try {
                FileOutputStream out = new FileOutputStream(dest);
                drawn_image.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                Uri file = Uri.fromFile(new File(filePath));
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference riversRef = storageRef.child("images/rivers.jpg");

                riversRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                riversRef.getDownloadUrl().addOnSuccessListener(uri -> {

                                    final_uri = uri.toString();


                                    //photo.setImageURI(Result_uri);
                                    dialog.hide();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                    //reference.child("Users").child(firebaseUser.getUid()).child("photourl").setValue(final_uri);
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    Date date = new Date();
                                    String date_string = dateFormat.format(date); //2016/11/16 12:08:43
                                    reference = database.getReference();


                                    Chat chat = new Chat(userid, firebaseUser.getUid(), final_uri, date_string, "image");

                                    reference.child("Chats").push().setValue(chat);
                                    msg = "Has sent a doodle";
                                    File file = new File(filePath);
                                    file.delete();

                                    MessagingActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    drawableView.clear();
                                    dialog.hide();
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                dialog.hide();
                            }
                        });


            } catch (Exception e) {
                Toast.makeText(MessagingActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }


//
        }
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User_details user = dataSnapshot.getValue(User_details.class);
                if (notify) {
                    sendNotifiaction(reciever, user.getName(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void sendNotifiaction(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        DatabaseReference userpic = FirebaseDatabase.getInstance().getReference("Users");

        Query query = tokens.orderByKey().equalTo(receiver);

        intent = getIntent();
        String userid = intent.getStringExtra("userid");

        userpic.child(firebaseUser.getUid()).child("photourl").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userpicurl = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(firebaseUser.getUid(), userpicurl, message, username,
                            userid);

                    Sender sender = new Sender(data, token.getToken());


                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(MessagingActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }

                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void readmessages(final String sender, final String receiver, final String imageurl, String currentuser) {
        recyclerView.setAdapter(messageAdapter);
        reference = database.getReference("Chats");


        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);


                //if the receiver and sender are the same people we are talking to
                Log.e("sample message is ", "im here");

                if (chat.getSender().equals(sender) && chat.getReciever().equals(receiver)
                        || chat.getSender().equals(receiver) && chat.getReciever().equals(sender)) {
                    texts.add(chat);
                    //
                    messageAdapter.notifyDataSetChanged();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
                        }
                    }, 200);


                }


                //The RecyclerView is a new ViewGroup that is prepared to render any adapter-based view in a similar way.
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
        ActivityCompat.postponeEnterTransition(this);
        ActivityCompat.startPostponedEnterTransition(MessagingActivity.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

//


//


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            dialog.show();
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri Result_uri = result.getUri();
                RandomString randomString = new RandomString();
                String push = randomString.generate();
                StorageReference mstorage = FirebaseStorage.getInstance().getReference();
                StorageReference ref = mstorage.child(firebaseUser.getUid()).child("pictures").child(push + ".jpg");
                ref.putFile(Result_uri).addOnSuccessListener(taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadUrl = uri;
                    final_uri = uri.toString();

                    if (data != null) {
                        //photo.setImageURI(Result_uri);
                        dialog.hide();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        //reference.child("Users").child(firebaseUser.getUid()).child("photourl").setValue(final_uri);
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        String date_string = dateFormat.format(date); //2016/11/16 12:08:43
                        reference = database.getReference();


                        Chat chat = new Chat(userid, firebaseUser.getUid(), final_uri, date_string, "image");

                        reference.child("Chats").push().setValue(chat);


                    }
                })).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.hide();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        dialog.hide();
                    }
                });
            } else {
                dialog.hide();
            }
        } else {
            dialog.hide();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void open_draw()
    {
        draw_toggle = draw_toggle + 1;
        if (draw_toggle % 2 != 0) {
            drawableView = findViewById(R.id.drawable_view);
//            ConstraintLayout constraintLayout = findViewById(R.id.messaging);
//            int x = constraintLayout.getRight();
//            int y = constraintLayout.getBottom();
//
//            int startRadius = 0;
//            int endRadius = (int) Math.hypot(300, 300);
//
//            Animator anim = ViewAnimationUtils.createCircularReveal(drawableView, x, y, startRadius, endRadius);
//            anim.start();
            drawableView.setVisibility(View.VISIBLE);


            drawableView.setBackgroundColor(getResources().getColor(android.R.color.white));
            config.setStrokeColor(Color.parseColor(Apps.colour));
            //config.setShowCanvasBounds(true); // If the view is bigger than canvas, with this the user will see the bounds (Recommended)
            config.setStrokeWidth(10.0f);
            config.setMinZoom(1.0f);
            config.setMaxZoom(3.0f);
            config.setCanvasHeight(drawableView.getHeight()+120);
            config.setCanvasWidth(drawableView.getWidth());
            drawableView.setConfig(config);
            camera.setVisibility(View.GONE);
            draw.setImageResource(R.drawable.no_draw);
        } else {
            DrawableView drawableView = findViewById(R.id.drawable_view);
            drawableView.setVisibility(View.GONE);
            camera.setVisibility(View.VISIBLE);
            draw.setImageResource(R.drawable.draw);

        }
    }
    public void change_style(){
        message=findViewById(R.id.typed_message);
        Typeface type = ResourcesCompat.getFont(this,R.font.bad_script);
        message.setTypeface(type);

    }




class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewholder> {


    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    public static final int IMG_TYPE_LEFT = 2;
    public static final int IMG_TYPE_RIGHT = 3;
    String currentuserphoto;
    private Context mContext;
    private List<Chat> texts;
    private String imageurl;
    private String currentuser;

    FirebaseUser firebaseUser;


    public ChatAdapter(Context mContext, List<Chat> texts, String imageurl, String currentuser) {
        this.mContext = mContext;
        this.texts = texts;
        this.imageurl = imageurl;
        this.currentuser = currentuser;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new ChatAdapter.viewholder(view);
        } else if (viewType == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new ChatAdapter.viewholder(view);

        } else if (viewType == IMG_TYPE_LEFT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.image_left, parent, false);
            return new ChatAdapter.viewholder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.image_right, parent, false);
            return new ChatAdapter.viewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        final Chat chat = texts.get(position);
        if (chat.type.equals("text")) {
            holder.textmessage.setText(chat.getMessage().trim());
        } else {
            Glide.with(mContext).load(chat.getMessage()).into(holder.chat_image);
        }

        String time_unformatted = chat.getDate().split(" ")[1];
        String time_formatted[] = time_unformatted.split(":");
        holder.date.setText(time_formatted[0] + ":" + time_formatted[1]);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentuser);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentuserphoto = dataSnapshot.child("photourl").getValue(String.class);


                int type = getItemViewType(position);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("DP").child(currentuser);

                Log.e("This is right image", currentuserphoto);

                if (type == MSG_TYPE_RIGHT && MessagingActivity.right == 0) {
                    MessagingActivity.right = MessagingActivity.right + 1;
                    MessagingActivity.left = 0;
                    //Glide.with(mContext).load(currentuserphoto).into(holder.rimage);

                } else if ((type == MSG_TYPE_LEFT || type == IMG_TYPE_LEFT) && MessagingActivity.left == 0) {

                    MessagingActivity.left = MessagingActivity.left + 1;
                    MessagingActivity.right = 0;


                    Glide.with(mContext).load(imageurl).into(holder.image);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.date.setVisibility(View.VISIBLE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.date.setVisibility(View.GONE);
                    }
                }, 1000);


            }
        });
        if (getItemViewType(position) == MSG_TYPE_LEFT && position == getItemCount() - 1) {
            holder.itemView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.anim));
        } else if (getItemViewType(position) == MSG_TYPE_RIGHT && position == getItemCount() - 1) {
            holder.itemView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right));
        }

    }

    @Override
    public int getItemCount() {
        return texts.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (texts.get(position).getSender().equals(firebaseUser.getUid())) {

            if (texts.get(position).getType().equals("text")) {
                return MSG_TYPE_RIGHT;
            } else {
                return IMG_TYPE_RIGHT;
            }


        } else {

            if (texts.get(position).getType().equals("text")) {
                return MSG_TYPE_LEFT;
            } else {
                return IMG_TYPE_LEFT;
            }

        }


    }

    public class viewholder extends RecyclerView.ViewHolder {
        public CircleImageView image;
        public CircleImageView rimage;
        public TextView textmessage;
        public TextView date;
        public ImageView chat_image;

        public viewholder(View itemView) {
            super(itemView);
            textmessage = itemView.findViewById(R.id.textmessage);
            image = itemView.findViewById(R.id.circleimage);
            //image=itemView.findViewById(R.id.rimage);
            date = itemView.findViewById(R.id.date);
            date.setVisibility(View.GONE);
            chat_image = itemView.findViewById(R.id.image);

        }

    }
}
public void change_colour()
{
    drawableView = findViewById(R.id.drawable_view);
   config.setStrokeColor(Color.parseColor(Apps.colour));
   message.setTextColor(Color.parseColor(Apps.colour));
}


}

class RandomString {

    // function to generate a random string of length n
    static String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    String generate() {

        // Get the size n
        int n = 20;

        // Get and display the alphanumeric string
        return (RandomString
                .getAlphaNumericString(n));
    }
}





