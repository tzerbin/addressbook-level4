package seedu.address.model.maps;

import java.io.IOException;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;

public class DistanceEstimate {
    public static String API_KEY = "AIzaSyAD8_oIBJlzOp30VA9mOvQKp6GZe8SFsYY";
    private GeoApiContext context;
    private String distOriginDest;
    private String travelTime;

    public static void main(String[] args) {
        DistanceEstimate estimate = new DistanceEstimate();
        estimate.calculateDistanceMatrix("820296","NUS",TravelMode.DRIVING);
        System.out.println(estimate.getDistOriginDest()+" "+estimate.getTravelTime());
    }

    public DistanceEstimate() {
        context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();
    }
    private static Duration extractDurationDetailsToString(DistanceMatrix matrixDetails) {
        DistanceMatrixRow[] dataRow = matrixDetails.rows;
        DistanceMatrixElement[] dataElements = dataRow[0].elements;

        return dataElements[0].duration;
    }

    private static Distance extractDistanceDetailsToString(DistanceMatrix matrixDetails) {
        DistanceMatrixRow[] dataRow = matrixDetails.rows;
        DistanceMatrixElement[] dataElements = dataRow[0].elements;

        return dataElements[0].distance;
    }

    private static DistanceMatrixApiRequest getApproveForRequest(GeoApiContext context) {
        return DistanceMatrixApi.newRequest(context);
    }

    public void calculateDistanceMatrix(String startPostalCode, String endPostalCode, TravelMode modeOfTravel) {
        DistanceMatrixApiRequest request = getApproveForRequest(context);
        DistanceMatrix estimate = null;
        try {
            estimate = request.origins(startPostalCode)
                    .destinations(endPostalCode)
                    .mode(modeOfTravel)
                    .language("en-EN")
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        distOriginDest = String.valueOf(extractDistanceDetailsToString(estimate));
        travelTime = String.valueOf(extractDurationDetailsToString(estimate));
    }

    public String getTravelTime(){
        return travelTime;
    }

    public String getDistOriginDest() {
        return distOriginDest;
    }
}
