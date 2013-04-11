/**
 * VehicleData.co Client Demo - Java
 *
 * A demonstration of a few APIs in the VehicleData.co API.
 *
 * For more information, please visit http://www.vehicledata.co.
 *
 * Copyright 2013 Blacktop Ventures LLC
 */

class vdcDemo
{  
    public static void main(String args[]) {
        VehicleDataCo vdc = new VehicleDataCo("YOUR_API_KEY_HERE",
            "YOUR_SECRET_HERE",
            "0.2");
        System.out.println("Getting supported functions:");
        System.out.println(vdc.service_getSupportedFunctions().toString());
        System.out.println("Makes in 2008:");
        System.out.println(vdc.vehicles_getMakes("2008").toString());
        /*
        System.out.println("Audi models in 2008:");
        System.out.println(vdc.vehicles_getModels("2008", "Audi").toString());
        System.out.println("Audi A4 trims in 2008:");
        System.out.println(vdc.vehicles_getTrims("2008", "Audi", "A4").toString());
        System.out.println("Audi A4 2.0T styles & trims in 2008:");
        System.out.println(vdc.vehicles_getStyleTrims("2008", "Audi", "A4", "2.0T").toString());
        System.out.println("Audi A4 2.0T sedan transmissions in 2008:");
        System.out.println(vdc.vehicles_getTransmissions("2008", "Audi", "A4", "sedan", "2.0T").toString());
        */
        System.out.println("Details for VIN 1NXBU40E39Z099060:");
        System.out.println(vdc.vin_decode("1NXBU40E39Z099060").toString());
    }
}
