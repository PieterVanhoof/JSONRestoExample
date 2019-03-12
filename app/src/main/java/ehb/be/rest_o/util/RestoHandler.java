package ehb.be.rest_o.util;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ehb.be.rest_o.model.MockRestoDataSource;
import ehb.be.rest_o.model.Resto;

public class RestoHandler extends Handler {

    private RestoAdapter nRestoAdapter;

    public RestoHandler(RestoAdapter nRestoAdapter) {
        this.nRestoAdapter = nRestoAdapter;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        String data = (String) msg.obj;
        try {
            JSONObject rootObject = new JSONObject(data);

            JSONArray records = rootObject.getJSONArray("records");
            int nrOfRecords = records.length();
            int index = 0;

            while (index<nrOfRecords){

                JSONObject currentRecord = records.getJSONObject(index);
                JSONObject fields = currentRecord.getJSONObject("fields");

                String adres = (fields.getString("adres") !=null)?fields.getString("adres") : "geen adres";
                String naam = fields.getString("naam");

                Resto currentResto = new Resto(naam, adres);
                MockRestoDataSource.getInstance().addResto(currentResto);

                index++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nRestoAdapter.setItems(MockRestoDataSource.getInstance().getRestos());
        nRestoAdapter.notifyDataSetChanged();
    }
}

