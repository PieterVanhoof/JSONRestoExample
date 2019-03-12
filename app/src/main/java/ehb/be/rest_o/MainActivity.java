package ehb.be.rest_o;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;

import ehb.be.rest_o.model.MockRestoDataSource;
import ehb.be.rest_o.util.RestoAdapter;
import ehb.be.rest_o.util.RestoHandler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvRestos;
    private RestoHandler nRestoHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvRestos = findViewById(R.id.rc_resto);
        RestoAdapter nRestoAdapter = new RestoAdapter(MockRestoDataSource.getInstance().getRestos());
        rvRestos.setAdapter(nRestoAdapter);

        LinearLayoutManager nLayoutManager = new LinearLayoutManager(this);
        rvRestos.setLayoutManager(nLayoutManager);


        nRestoHandler = new RestoHandler(nRestoAdapter);

        downloadData();
    }

    private void downloadData() {
        Thread backThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("https://opendata.brussel.be/api/records/1.0/search/?dataset=eten-en-drinken&rows=80")
                            .get().build();

                    Response response = client.newCall(request).execute();

                    String responseBodyText = response.body() != null ? response.body().string() : null;

                    Message msg = new Message();
                    msg.obj = responseBodyText;
                    nRestoHandler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        backThread.start();
    }

}
