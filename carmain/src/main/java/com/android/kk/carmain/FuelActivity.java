package com.android.kk.carmain;

import android.app.Activity;

import org.sqlite.database.sqlite.SQLiteDatabase;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.kk.carapplication.MainActivity;
import com.android.kk.carapplication.R;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FuelActivity extends Activity implements Serializable {

    public static final String APOSTROF = "'";
    public static final int MAXDIST = 70;
    public static final String TAG = FuelActivity.class.getSimpleName();
    public static final int GRAD_METER_FACTOR = 40000 / 360 * 1000;
    public static FuelActivity activity;

    private class Station {
        public int id;
        public String name;
        public String country;
        public String postcode;
        public String city;
        public String street;
        public String houseNumber;
        public Double latitude;
        public Double longitude;

        public Station(int id, String name, String country, String postcode, String city, String street, String streetnumber, Double latitude, Double longitude) {
            this.id = id;
            this.name = name;
            this.country = country;
            this.postcode = postcode;
            this.city = city;
            this.street = street;
            this.houseNumber = streetnumber;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }


    private SQLiteDatabase fuelDatabase;
    private SQLiteDatabase poiDatabase;
    private TextView txtPart;
    private TextView txtStation;
    private EditText txtCountry;
    private TextView txtPostCode;
    private TextView txtCity;
    private TextView txtStreet;
    private TextView txtHouseNumber;
    private TextView txtLiter;
    private TextView txtSum;
    private TextView txtPrice;
    private TextView txtCurrency;
    private int id; // a következő fueling id
    private int stationId; // a következő station id (ha újat kell létrehozni)
    private String dateStr;
    private Integer total;
    private Station station = null;
    private boolean foundStation = false;
    private Double liter;
    private Double sum;
    private double latitude;
    private double longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(com.android.kk.carapplication.R.layout.activity_fuel);

        loadSqlite();
        MainActivity.activity.getTimeAndPos();
        latitude = MainActivity.activity.getLatitude();
        longitude = MainActivity.activity.getLongitude();
        this.txtPart = (TextView) findViewById(com.android.kk.carapplication.R.id.txtPart);
        this.txtStation = (TextView) findViewById(com.android.kk.carapplication.R.id.txtStation);
        this.txtCountry = (EditText) findViewById(com.android.kk.carapplication.R.id.txtCountry);
        this.txtPostCode = (TextView) findViewById(com.android.kk.carapplication.R.id.txtPostCode);
        this.txtCity = (TextView) findViewById(com.android.kk.carapplication.R.id.txtCity);
        this.txtStreet = (TextView) findViewById(com.android.kk.carapplication.R.id.txtStreet);
        this.txtHouseNumber = (TextView) findViewById(com.android.kk.carapplication.R.id.txtHouseNumber);
        this.txtLiter = (TextView) findViewById(com.android.kk.carapplication.R.id.txtLiter);
        this.txtSum = (TextView) findViewById(com.android.kk.carapplication.R.id.txtSum);
        this.txtPrice = (TextView) findViewById(com.android.kk.carapplication.R.id.txtPrice);
        this.txtCurrency = (TextView) findViewById(com.android.kk.carapplication.R.id.txtCurrency);

        String fuelSql = "SELECT id, total FROM fueling WHERE ID=(SELECT MAX(id) FROM fueling);";
        Cursor fuelCursor = fuelDatabase.rawQuery(fuelSql, new String[0]);
        fuelCursor.moveToNext();
        id = fuelCursor.getInt(0) + 1;

        String stationSql = "SELECT MAX(id) FROM station;";
        Cursor stationCursor = fuelDatabase.rawQuery(stationSql, new String[0]);
        stationCursor.moveToNext();
        stationId = stationCursor.getInt(0) + 1;

//        completeDb();

        createListeners(fuelCursor);

        TextView date = (TextView) findViewById(com.android.kk.carapplication.R.id.txtDate);
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        dateStr = df.format(new Date());
        date.setText(dateStr);

        txtCurrency.setText("€");

        double dist = findStation(latitude, longitude);
        if (dist > MAXDIST) {    // nincs a programm által ismertek között
//            dist = findStationPoi(latitude, longitude);
//        }
//        if (stationId == null) {    // nincs a POIk között sem
            dist = 0;
        }
        else {
            foundStation = true;
            fillStationData(station);
        }

        TextView position = (TextView) findViewById(com.android.kk.carapplication.R.id.txtPosition);
        position.setText(String.format("lat: %f, lon: %f,     távolság: %.2f m", latitude, longitude, dist));
        Log.d(TAG, "position: " + position.getText());
    }

    private void createListeners(Cursor fuelCursor) {
        final int oldTotal = fuelCursor.getInt(1);

        EditText txtTotal = (EditText) findViewById(com.android.kk.carapplication.R.id.txtTotal);
        txtTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nm = s.toString();
                if (s.length() > 0) {
                    total = Integer.valueOf(nm);
                    int part = total - oldTotal;
                    txtPart.setText(part+"");
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        txtLiter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nm = s.toString();
                if (s.length() > 0) {
                    liter = Double.valueOf(nm);
                    writePrice();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        txtSum.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nm = s.toString();
                if (s.length() > 0) {
                    sum = Double.valueOf(nm);
                    if (sum > 0 && liter != null) {
                        double price = sum / liter;
                        txtPrice.setText(String.format("%.2f " + txtCurrency.getText() + "/l", price));
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        txtCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nm = s.toString();
                String nmUpper = nm.toUpperCase();
                if (nm.equals(nmUpper)) {
                    if (s.length() > 0) {
                        String fuelSql = "SELECT currency FROM country WHERE code='" + nm + "';";
                        Cursor fuelCursor = fuelDatabase.rawQuery(fuelSql, new String[0]);
                        while (fuelCursor.moveToNext()) {
                            String currency = fuelCursor.getString(0);
                            txtCurrency.setText(currency);
                            writePrice();
                        }
                    }
                }
                else {
                    txtCountry.setText(nmUpper);
                    txtCountry.setSelection(txtCountry.getText().length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void writePrice() {
        if (liter  != null && liter > 0 && sum != null) {
            double price = sum / liter;
            txtPrice.setText(String.format("%.2f " + txtCurrency.getText() + "/l", price));
        }
    }

    /**
     * A POI adatbankból a legközelebbi benzinkutat keresi meg
     *
     * @param latitude szélesség
     * @param longitude hosszúság
     * @return távolság
     */
    private double findStationPoi(double latitude, double longitude) {
        String distanceSel = "(i.minLat - " + latitude + ")*(i.minLat - " + latitude + ")+(i.minLon - " + longitude + ")*(i.minLon - " + longitude + ") < 0.000005";
        String[] selectionArgs = {latitude +"", longitude +""};
        String sql = "SELECT d.data, i.minLat, i.minLon FROM poi_index i, poi_data d " +
                "WHERE d.id=i.id AND d.data LIKE '%amenity=fuel%' AND " + distanceSel;
        Cursor poiCursor = poiDatabase.rawQuery(sql, new String[0]);
        double distGrad = Double.MAX_VALUE;
        String bestData = null;
        while (poiCursor.moveToNext()) {
            String data = poiCursor.getString(0);
            double minLat = poiCursor.getDouble(1);
            double minLon = poiCursor.getDouble(2);
            Log.d(TAG, "data: " + data);
            Log.d(TAG, "minLat: " + minLat);
            Log.d(TAG, "minLon: " + minLon);
            double currDist = Math.pow (minLat - latitude, 2)  + Math.pow (minLon - longitude, 2);
            if (distGrad > currDist) {
                distGrad = currDist;
                bestData = data;
            }
        }
        Double dist = Double.MAX_VALUE;
        if (bestData != null) {
            dist = getDistInMeter(distGrad);
            Log.d(TAG, "dist: " + dist);
            getStation(bestData, dist);

        }
        return dist;
    }

    /**
     * Sqlite inicializálása
     */
    private void loadSqlite() {
        System.loadLibrary("sqliteX");

// get the SQLite version
        String query = "select sqlite_version() AS sqlite_version";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(":memory:", null);
        Cursor cursorV = db.rawQuery(query, null);
        String sqliteVersion = "";
        if (cursorV.moveToNext()) {
            sqliteVersion = cursorV.getString(0);
            Log.d(TAG, "sqliteVersion: " + sqliteVersion);
        }
        this.fuelDatabase = SQLiteDatabase.openDatabase("/sdcard/benzin.sqlite", null, SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * átszámítja a fokban mért különbséget méterre
     * @param distGrad
     * @return
     */
    private double getDistInMeter(double distGrad) {
        double dist;
        dist = Math.sqrt(distGrad) * GRAD_METER_FACTOR;  // távolság méterben
        return dist;
    }

    /**
     * Az adatbankban tárolt benzinkutak közül megkeresi az adott pozícióhoz legközelebbit.
     * Ha a távolság nem túl nagy, akkor ez a keresett  benzinkút
     * <code>station</code> az adott ponthoz legközelebbi benzinkút az adatbankban
     * @param latitude szélesség
     * @param longitude hosszúság
     * @return távolság
     */
    private double findStation(double latitude, double longitude) {
        String distanceSel =
                "(s.lat - " + latitude+ ")*" + GRAD_METER_FACTOR + " < " +  MAXDIST + " AND " +
                "(" + latitude+ " - s.lat)*" + GRAD_METER_FACTOR + " < " +  MAXDIST + " AND " +
                "(s.lon - " + longitude+ ")*" + GRAD_METER_FACTOR + " < " +  MAXDIST + " AND " +
                "(" + longitude+ " - s.lon)*" + GRAD_METER_FACTOR + " < " +  MAXDIST;
        String sql = "SELECT s.id, s.name, s.country, s.postcode, s.city, s.street, s.streetnumber, s.lat, s.lon FROM station s " +
                "WHERE s.lat NOT NULL AND s.lon NOT NULL AND " + distanceSel;
        Cursor stationCursor = fuelDatabase.rawQuery(sql, new String[0]);
        double distGrad = Double.MAX_VALUE;
        while (stationCursor.moveToNext()) {
            int id = stationCursor.getInt(0);
            String name = stationCursor.getString(1);
            String country = stationCursor.getString(2);
            String postcode = stationCursor.getString(3);
            String city = stationCursor.getString(4);
            String street = stationCursor.getString(5);
            String streetnumber = stationCursor.getString(6);
            double lat = stationCursor.getDouble(7);
            double lon = stationCursor.getDouble(8);
            Station currStation = new Station(id, name, country, postcode, city, street, streetnumber, lat, lon);
            Log.d(TAG, "data: " + id);
            Log.d(TAG, "minLat: " + lat);
            Log.d(TAG, "minLon: " + lon);
            double currDist = Math.pow (lat - latitude, 2)  + Math.pow (lon - longitude, 2);
            if (distGrad > currDist) {
                distGrad = currDist;
                station = currStation;
            }
        }
        double dist = getDistInMeter(distGrad);
        return dist;
    }

    private void fillStationData(Station station) {
        txtStation.setText(station.name);
        txtCity.setText(station.city);
        txtStreet.setText(station.street);
        txtHouseNumber.setText(station.houseNumber);
        txtPostCode.setText(station.postcode);
        if (station.country != null && !station.country.isEmpty()) {
            txtCountry.setText(station.country);
        String fuelSql = "SELECT currency FROM country WHERE code = '" + station.country + "';";
        Cursor fuelCursor = fuelDatabase.rawQuery(fuelSql, new String[0]);
        fuelCursor.moveToNext();
            String currency = fuelCursor.getString(0);
            ((TextView) findViewById(R.id.txtCurrency)).setText(currency);
        }
    }

    /**
     * A POI adata alapján keres az adatbankban
     * @param text
     * @param dist
     */
    private void getStation(String text, Double dist) {
        Station station = getStation(text);
        int stationId;
        if (dist > MAXDIST) { // a POI a túl messze van
            stationId = 0;
        }
        else { // a POI a keresett benzinkút
            fillStationData(station);
        }

        // Az adatbankban utánanézni, hogy van-e olyan benzinkút, aminek nincs pozíciója, de a többi adat megegyezik a POI-éval
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id, name, country, postcode, city, street, streetnumber FROM station " +
                "WHERE name='" + station.name +  "' AND country='" + station.country +  "'");
         sb.append(" AND postcode= " + station.postcode +  " AND street='" + station.street +  "' AND streetnumber='" + station.houseNumber +  "' ;");
        Cursor fuelCursor = fuelDatabase.rawQuery(sb.toString(), new String[0]);
        String stationSql;
        if (fuelCursor.getCount() == 0) {
            writeStation(station);
        }
        else {
            stationId = fuelCursor.getInt(0);
            stationSql = "UPDATE station SET lat = " + latitude + ", lon = " + longitude
                    + " WHERE id = " + stationId + ";";
            fuelDatabase.execSQL(stationSql);
        }
    }

    @NonNull
    private void writeStation(Station station) {
        String stationSql = "INSERT INTO station (id, name, country, postcode, city, street, streetnumber, lat, lon) VALUES ("
                + station.id +
                "," + getStringEsc(station.name) +
                "," + getStringEsc(station.country) +
                "," + getStringEsc(station.postcode) +
                "," + getStringEsc(station.city) +
                "," + getStringEsc(station.street) +
                "," + getStringEsc(station.houseNumber) +
                "," + station.latitude + "," + station.longitude + ");";
        fuelDatabase.execSQL(stationSql);
        foundStation = true;
    }

    @NonNull
    private Station getStation(String text) {
        String[] split = text.split("\r");
        Map<String,String> map = new HashMap<>();
        for (String part: split) {
            String[] keyValue = part.split("=");
            if (keyValue.length==2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        String name = map.get("name");
        if (name == null) {
            name = map.get("operator");
        }
        if (name == null) {
            name = "";
        }
        String country = map.get("addr:country");
        if (country == null) {
            country = "";
        }
        String postcode = map.get("addr:postcode");
        if (postcode == null) {
            postcode = "";
        }
        String city = map.get("addr:city");
        if (city == null) {
            city = "";
        }
        String street = map.get("addr:street");
        if (street == null) {
            street = "";
        }
        String streetnumber = map.get("addr:streetnumber");
        if (streetnumber == null) {
            streetnumber = "";
        }
        return new Station(stationId, name, country, postcode, city, street, streetnumber, null, null);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onClickSubmit(View view) {
        Log.d(TAG, "onClickSubmit: " + view.getTransitionName());

        try {
            String name = getText(this.txtStation);
            String country = getText(this.txtCountry);
            String postcode = getText(this.txtPostCode);
            String city = getText(this.txtCity);
            String street = getText(this.txtStreet);
            String streetnumber = getText(this.txtHouseNumber);
            Station poiStation = new Station(stationId, name, country, postcode, city, street, streetnumber, latitude, longitude);
            if (foundStation) {    // a benzinkút benne van az adatbankban
                updateStation(poiStation);
            }
            else {    // a benzinkút még nincs az adatbankban
                station = poiStation;
                writeStation(station);
            }
            if (total != null && liter != null && sum != null && station != null) {
                String insertSql = "INSERT INTO fueling (id,date,total,liter,cost,stationid) VALUES ("
                        + id + ",'" + dateStr + "'," + total + "," + liter + "," + sum + "," + station.id
                        + ");";
                fuelDatabase.execSQL(insertSql);
                finish();
            }
            else {
                String fieldName = "";
                if (total == null) {
                    fieldName = "total";
                }
                if (liter == null) {
                    fieldName = "liter";
                }
                if (sum == null) {
                    fieldName = "sum";
                }
                if (station == null) {
                    fieldName = "station";
                }
                Toast.makeText(this, "nincs " + fieldName +  " kitöltve", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Hiba: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    private String getText(TextView txtHouseNumber) {
        CharSequence text = txtHouseNumber.getText();
        if (text == null) {
            return null;
        }
        return text.toString();
    }
//
//    private Double readText(int txtId) {
//        TextView txtLiter = (TextView) findViewById(txtId);
//        CharSequence literStr = txtLiter.getText();
//        if (literStr !=null) {
//            return Double.valueOf(literStr.toString());
//        }
//        return null;
//    }

    public void onClickCancel(View view) {
        Log.d(TAG, "onClickCancel: " + view.getTag());
        finish();
    }

    public void completeDb() {
//        List<Station> listStation = new ArrayList<>();
//        String sql = "SELECT s.id, s.name, s.country, s.postcode, s.city, s.street, s.streetnumber, s.lat, s.lon FROM station s " +
//                "WHERE s.lat IS NULL OR s.lon IS NULL";
//        Cursor stationCursor = fuelDatabase.rawQuery(sql, new String[0]);
//        while (stationCursor.moveToNext()) {
//            int id = stationCursor.getInt(0);
//            String name = stationCursor.getString(1);
//            String country = stationCursor.getString(2);
//            int postcode = stationCursor.getInt(3);
//            String city = stationCursor.getString(4);
//            String street = stationCursor.getString(5);
//            String streetnumber = stationCursor.getString(6);
//            double lat = stationCursor.getDouble(7);
//            double lon = stationCursor.getDouble(8);
//            listStation.add(new Station(id,name, country,postcode,city,street,streetnumber,lat,lon));
//        }
        File pois = new File("/sdcard/poi");
        for (File poi: pois.listFiles()) {
            this.poiDatabase = SQLiteDatabase.openOrCreateDatabase(poi, null);

            int lastStationId = stationId;

            String sqlPoi = "SELECT d.data, i.minLat, i.minLon FROM poi_index i, poi_data d " +
                    "WHERE d.id=i.id AND d.data LIKE '%amenity=fuel%'";
            Cursor poiCursor = poiDatabase.rawQuery(sqlPoi, new String[0]);
            int i = 0;
            int k = 0;
            Set<Integer> updated = new HashSet<>();
            while (poiCursor.moveToNext()) {
                String data = poiCursor.getString(0);
                Station poiStation = getStation(data);
                poiStation.id = ++lastStationId;
                poiStation.latitude = poiCursor.getDouble(1);
                poiStation.longitude = poiCursor.getDouble(2);
                double dist = findStation(poiStation.latitude, poiStation.longitude);
                if (dist > MAXDIST) {    // nincs a a programm által ismertek között
                    writeStation(poiStation);
                }
                else {
                    boolean update = updateStation(poiStation);
                    if (update) {
                        updated.add(station.id);
                    }
                    else {
                        k++;
                    }
                }

//            List<Station> stations = new ArrayList<>();
//            for (Station currStation:listStation) {
//                if (station.name.equalsIgnoreCase(currStation.name) &&
//                    station.country.equalsIgnoreCase(currStation.country) &&
//                    station.city.equalsIgnoreCase(currStation.city) &&
//                    station.postcode == currStation.postcode &&
//                    station.street.equalsIgnoreCase(currStation.street) &&
//                    station.name.equalsIgnoreCase(currStation.name) &&
//                    station.name.equalsIgnoreCase(currStation.name)
//                   ) {
//                    stations.add(station);
//                }
//            }
//            if (stations.size()==1) {
//                String stationSql = "UPDATE station SET lat = "
//                        + latitude + ", lon = " + longitude + " WHERE id = " + stations.get(0).id + ";";
//                fuelDatabase.execSQL(stationSql);
//
//            }
//            else {
//                Log.d(TAG, "stations for POI: " + stations);
//            }
                i++;
            }
            Log.d(TAG, "POI stations in " + poi + ": " + i);
            Log.d(TAG, "update POI stations in " + poi + ": " + updated.size());
            Log.d(TAG, "duplicate POI stations in " + poi + ": " + k);
        }
    }

    private boolean updateStation(Station poiStation) {
        boolean update = false;
        if (poiStation.name != null && !poiStation.name.isEmpty()) {
            station.name = poiStation.name;
            update = true;
        }
        if (poiStation.country != null && !poiStation.country.isEmpty()) {
            station.country = poiStation.country;
            update = true;
        }
        if (poiStation.city != null && !poiStation.city.isEmpty()) {
            station.city = poiStation.city;
            update = true;
        }
        if (poiStation.postcode != null && !poiStation.postcode.isEmpty()) {
            station.postcode = poiStation.postcode;
            update = true;
        }
        if (poiStation.street != null && !poiStation.street.isEmpty()) {
            station.street = poiStation.street;
            update = true;
        }
        if (poiStation.houseNumber != null && !poiStation.houseNumber.isEmpty()) {
            station.houseNumber = poiStation.houseNumber;
            update = true;
        }
        if (update) {
            String stationSql = "UPDATE station SET name = " + getStringEsc(station.name)
                    + ", country = " + getStringEsc(station.country)
                    + ", city = " + getStringEsc(station.city)
                    + ", postcode = " + getStringEsc(station.postcode)
                    + ", street = " + getStringEsc(station.street)
                    + ", streetnumber = " + getStringEsc(station.houseNumber)
                    + " WHERE id = " + station.id + ";";
            fuelDatabase.execSQL(stationSql);
        }
        return update;
    }

    @NonNull
    private String getStringEsc(String name) {
        return APOSTROF + name.replaceAll(APOSTROF, APOSTROF+APOSTROF) + APOSTROF;
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        thinBTClient.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        thinBTClient.onPause();
//    }
}
