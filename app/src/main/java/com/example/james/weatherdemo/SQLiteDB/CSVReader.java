package com.example.james.weatherdemo.SQLiteDB;

import com.example.james.weatherdemo.Model.CityDto;
import com.example.james.weatherdemo.Utilities.AppContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CSVReader {

    DatabaseHelper myDatabase = new DatabaseHelper(AppContext.getContext());

    private InputStream inputStream;

    public boolean downFile(final String string) {
        try {
            URL url = new URL(string);
            inputStream = url.openStream();
            List<String[]> entries = read();
            System.out.println("Ftiaxtike to entries...!!!!!");
            if (prepareDB(entries)) {
                myDatabase.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<String[]> read() {
        List<String[]> resultList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split("\t");
                resultList.add(row);
            }
            System.out.println("DIAVASTIKE TO textFile ap to Link");
        } catch (IOException ex) {
            throw new RuntimeException("Can't read the Cities file" + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Couldn't close the Cities file. Error code" + e);
            }
        }
        return resultList;
    }

    private boolean prepareDB(List<String[]> entries) {

        List<CityDto> insertList = new ArrayList<>();

        for (int i = 1; i < entries.size(); i++) {
            CityDto cityDto = new CityDto();

            int a = Integer.parseInt(entries.get(i)[0]);
            cityDto.setALL_ID(a);
            cityDto.setALL_CITY_NAME(entries.get(i)[1]);
            cityDto.setALL_LAT(entries.get(i)[2]);
            cityDto.setALL_LONG(entries.get(i)[3]);
            cityDto.setALL_CITY_CODE(entries.get(i)[4]);

            insertList.add(cityDto);
        }
        return myDatabase.insertAllCities(insertList);
    }
}
