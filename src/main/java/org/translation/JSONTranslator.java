package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final JSONArray jsonArray;
    private final String alpha3Str = "alpha3";

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            this.jsonArray = new JSONArray(jsonString);
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        List<String> availableLangs = new ArrayList<>();
        for (int i = 0; i < this.jsonArray.length(); i++) {
            if (country.equals(this.jsonArray.getJSONObject(i).get(this.alpha3Str))) {
                for (Map.Entry<String, Object> s: this.jsonArray.getJSONObject(i).toMap().entrySet()) {
                    String keyValue = s.getKey();
                    if (!("id".equals(keyValue)) && !("alpha2".equals(keyValue))
                            && !(this.alpha3Str).equals(keyValue)) {
                        availableLangs.add(keyValue);
                    }
                }
            }
        }
        return availableLangs;
    }

    @Override
    public List<String> getCountries() {
        List<String> countryList = new ArrayList<>();
        for (int i = 0; i < this.jsonArray.length(); i++) {
            countryList.add((String) this.jsonArray.getJSONObject(i).get(this.alpha3Str));
        }
        return countryList;
    }

    @Override
    public String translate(String country, String language) {
        for (int i = 0; i < this.jsonArray.length(); i++) {
            if (country.equals(this.jsonArray.getJSONObject(i).get(this.alpha3Str))) {
                return (String) this.jsonArray.getJSONObject(i).get(language);
            }
        }
        return null;
    }
}
