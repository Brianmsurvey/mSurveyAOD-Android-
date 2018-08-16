package com.msurvey.projectm.msurveyaod.Utilities;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {


    public static String getAddress(Context context, Location location){

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();


        Geocoder geoCoder = new Geocoder(context, Locale.getDefault()); //it is Geocoder
        String finalAddress = "";
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i=0; i<maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            finalAddress = builder.toString(); //This is the complete address.
        } catch (IOException e) {}
        catch (NullPointerException e) {}


        return finalAddress;
    }

}
