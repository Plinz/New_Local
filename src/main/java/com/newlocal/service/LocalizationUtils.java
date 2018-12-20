package com.newlocal.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.newlocal.service.dto.LocationDTO;

@Component
public class LocalizationUtils {

    private List<LocalizationFrance> localizationsFrance;
    
    public LocalizationUtils() {
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource("FR.txt").getFile());
    	try(Stream<String> stream = Files.lines(file.toPath())){
    		localizationsFrance = stream.map(LocalizationFrance::new).collect(Collectors.toList());
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    private double deg2rad(double deg) {
        return deg * Math.PI / 180.0;
    }
    private double rad2deg(double rad) {
        return rad * 180 / Math.PI;
    }

    private double distance (double lat1, double lon1, double lat2, double lon2){
	    double dist = -1;
        double theta = lon1 - lon2;
        dist = Math.sin(this.deg2rad(lat1)) * Math.sin(this.deg2rad(lat2)) + Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) * Math.cos(this.deg2rad(theta));
        dist = Math.acos(dist);
        dist = this.rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344;
	    return dist;
    }
	
	public LocationDTO fillEntityFromZip(LocationDTO location){
		System.out.println("Localizations is null"+localizationsFrance == null);
		if (localizationsFrance != null){
			LocalizationFrance loc = localizationsFrance.parallelStream().filter(l -> 
				l.zip.trim().toLowerCase().equals(location.getZip().trim().toLowerCase())
			).findFirst().orElse(null);
			if (loc != null){
				location.setCity(loc.city);
				location.setZip(loc.zip);
				location.setCountry("France");
				location.setLat(loc.lat);
				location.setLon(loc.lon);
			}
		}
		return location;
	}

	public LocationDTO fillEntityFromCity(LocationDTO location){
		System.out.println("Localizations is null"+localizationsFrance == null);
		if (localizationsFrance != null){
			LocalizationFrance loc = localizationsFrance.parallelStream().filter(l -> 
				l.city.trim().toLowerCase().equals(location.getCity().trim().toLowerCase())
			).findFirst().orElse(null);
			if (loc != null){
				location.setCity(loc.city);
				location.setZip(loc.zip);
				location.setCountry("France");
				location.setLat(loc.lat);
				location.setLon(loc.lon);
			}
		}
		return location;
	}
	
	public LocationDTO fillEntityFromLonLat(LocationDTO location){
		System.out.println("Localizations is null"+localizationsFrance == null);
		if (localizationsFrance != null){
			LocalizationFrance loc = localizationsFrance.parallelStream().min((l1, l2) -> 
			Double.compare(this.distance(l1.lat, l1.lon, location.getLat(), location.getLon()), this.distance(l2.lat, l2.lon, location.getLat(), location.getLon()))
			).orElse(null);
			if (loc != null){
				location.setCity(loc.city);
				location.setZip(loc.zip);
				location.setCountry("France");
				System.out.println("Location in fillEntityFromLonLat:"+location);
			}
		}
		return location;
	}
	
	public class LocalizationFrance{
		String zip;
		String city;
		double lat;
		double lon;
		
		public LocalizationFrance(String line){
			String[] tokens = line.split("\t");
			this.zip = tokens[1];
			this.city = tokens[2];
			this.lat = Double.valueOf(tokens[9]);
			this.lon = Double.valueOf(tokens[10]);
		}
	}
	
}

