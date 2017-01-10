package ca.uwaterloo.grouptalk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import static android.content.ContentValues.TAG;
import static ca.uwaterloo.grouptalk.GroupActivity.decodeSampledBitmapFromResource;

/**
 * Created by Wisdom H Jiang on 2016-11-05.
 */

public class GameFragment extends Fragment {
    TextView showStatusG, newG;
    ListView MessageListViewG;
    EditText textToGoG;
    Button sendG, joinG, quitG;
    ImageButton groupMemberG;
    String userId;
    boolean isGroupJoined;
    ConcurrentLinkedQueue<MessageService.Client> q;
    ArrayAdapter<String> adapter;
    ArrayList<String> listForView;
    ImageView icon_game;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_game,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isGroupJoined = false;
        q = new ConcurrentLinkedQueue<>();

        groupMemberG = (ImageButton) getActivity().findViewById(R.id.groupMemberG);
        groupMemberG.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.group_member1,45, 45));

        icon_game = (ImageView) getActivity().findViewById(R.id.icon_game);
        icon_game.setImageBitmap(decodeSampledBitmapFromResource(getResources(),R.drawable.game_fan_1,45, 45));

        MessageListViewG = (ListView) getActivity().findViewById(R.id.MessageListViewG);
        //MessageListViewG.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorAmber100));

        listForView = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,listForView);
        MessageListViewG.setAdapter(adapter);

        TalkActivity activity = (TalkActivity) getActivity();
        userId = activity.getUserId();
        showStatusG = (TextView) getActivity().findViewById(R.id.showStatusG);
        showStatusG.setText(getString(R.string.status_realTime, "down"));
        newG = (TextView) getActivity().findViewById(R.id.newG);

        textToGoG = (EditText) getActivity().findViewById(R.id.textToGoG);

        joinG = (Button) getActivity().findViewById(R.id.joinG);
        joinG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MessageService.Client mClient = getMessageClient();
                        try{
                            mClient.joinX(userId,"game");
                            q.offer(mClient);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showStatusG.setText(getString(R.string.status_realTime, "up"));
                                    isGroupJoined = true;
                                    Toast.makeText(getContext(), "Game group joined!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }catch (TException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Cannot join in Game group. Try again?", Toast.LENGTH_LONG).show();
                                }
                            });

                            Log.e(TAG, "Texception at Joinx ", e);
                        }
                        Thread pulseThread = new Thread(new PulseRunnable());  //right now only use one thread for this, can be more
                        pulseThread.start();
                    }
                }).start();
            }
        });

        quitG = (Button) getActivity().findViewById(R.id.quitG);
        quitG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MessageService.Client mClient = getMessageClient();
                        try{
                            mClient.quitX(userId,"game");
                            q.offer(mClient);
                            isGroupJoined = false;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showStatusG.setText(getString(R.string.status_realTime, "down"));
                                    Toast.makeText(getContext(), "You have quit from Game group!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }catch (TException e) {
                            Log.e(TAG, "Texception at quitx ", e);
                        }
                    }
                }).start();
            }
        });

        sendG = (Button) getActivity().findViewById(R.id.sendG);
        sendG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGroupJoined) {
                    Toast.makeText(getContext(), "Please join the group at first", Toast.LENGTH_LONG).show();
                    return;
                }
                String messageToBeSent = textToGoG.getText().toString();
                if (messageToBeSent.length() == 0) {
                    Toast.makeText(getContext(), "You cannot send empty message", Toast.LENGTH_LONG).show();
                    return;
                }
                textToGoG.setText("");
                showMessageInListViewStr(messageToBeSent);
                new Thread(new TransmitMessageRunnable(messageToBeSent)).start();
            }
        });

        groupMemberG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGroupJoined) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            MessageService.Client mClient = getMessageClient();
                            try{
                                Set<String> userReturn = mClient.checkUser("game");
                                q.offer(mClient);
                                StringBuilder sb = new StringBuilder();
                                sb.append(getString(R.string.userIn));
                                for (String user : userReturn) {
                                    sb.append(user);
                                    sb.append(", ");
                                }
                                String toastMes = sb.toString();
                                getActivity().runOnUiThread(new ShowUserRunnable(toastMes));
                                //Toast.makeText(getContext(), toastMes.substring(0, toastMes.length() - 1), Toast.LENGTH_SHORT).show();
                            }catch (TException e) {
                                Log.e(TAG, "Texception at checkUser", e);
                            }
                        }
                    }).start();
                }
                else {
                    Toast.makeText(getContext(), getString(R.string.notInGroup), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void showMessageInListViewStr(String messageX){  //show the message by current user itself
        String message = getString(R.string.my_message,messageX);
        listForView.add(message);
        adapter.notifyDataSetChanged();
        newG.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.colorBlue50));
    }

    private class TransmitMessageRunnable implements Runnable {
        String messageToBeSent;
        TransmitMessageRunnable(String messageToBeSent) {
            this.messageToBeSent = messageToBeSent;
        }
        @Override
        public void run(){
            MessageService.Client mClient = getMessageClient();
            try {
                List<String> messageReturn = mClient.transmitMessage(userId, "game", messageToBeSent);
                q.offer(mClient);
                int mSize = messageReturn.size();
                if (mSize == 0) {
                    return;
                }
                if (mSize == 1 || messageReturn.get(0).equals("Wrongxyz")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Please join the group at first", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    getActivity().runOnUiThread(new ShowMessageInListViewList(messageReturn));
                }
            }catch (TException e) {
                Log.e(TAG, "Texception at transmitMessage", e);   //when exception occurs, something needs to be down
                connectionBreak();
            }
        }
    }

    private class ShowUserRunnable implements Runnable{
        String showUser;
        ShowUserRunnable(String showUser){
            this.showUser = showUser;
        }

        @Override
        public void run(){
            Toast.makeText(getContext(), showUser.substring(0, showUser.length() - 2), Toast.LENGTH_LONG).show();
        }
    }

    private class ShowMessageInListViewList implements Runnable{ //show the message by other users, (userId, message)
        private List<String> messageReturn;
        ShowMessageInListViewList(List<String> messageReturn){
            this.messageReturn = messageReturn;
        }
        @Override
        public void run(){
            if (messageReturn.size() != 0) {
                for (int i = 0; i < messageReturn.size(); i = i + 2) {
                    String message = getString(R.string.other_message, messageReturn.get(i), messageReturn.get(i + 1));
                    listForView.add(message);
                }
                adapter.notifyDataSetChanged();
                newG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAmber300));
            }
        }
    }

    private class PulseRunnable implements Runnable {
        public void run(){
            while (isGroupJoined) {
               MessageService.Client mClient = getMessageClient();
                try {
                    List<String> messageReturn = mClient.checkPulse(userId, "game");
                    q.offer(mClient);
                    getActivity().runOnUiThread(new ShowMessageInListViewList(messageReturn));
                } catch (TException e) {
                    //Log.e(TAG, "Texception at transmitMessage", e);   //when exception occurs, something needs to be done
                    connectionBreak();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e(TAG, "InterruptedException", e);
                }
            }
        }
    }

    private void connectionBreak(){
        isGroupJoined = false;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showStatusG.setText(getString(R.string.status_realTime, "down"));
                Toast.makeText(getContext(), "Connection is Down!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private MessageService.Client getMessageClient(){
        MessageService.Client mClient;
        if (q.isEmpty()) {
            ClientX clientx = new ClientX();
            mClient = clientx.getThriftClient();
        }
        else {
            mClient = q.poll();
        }
        return mClient;
    }
}
